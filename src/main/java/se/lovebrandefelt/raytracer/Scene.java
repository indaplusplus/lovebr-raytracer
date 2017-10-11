package se.lovebrandefelt.raytracer;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class Scene {
  private List<Object> objects;
  private List<LightSource> lightSources;
  private double ambientLight;
  private Camera camera;
  private Vector3 background;
  private int reflectionDepth;

  public Scene(double ambientLight, Camera camera, Vector3 background, int reflectionDepth) {
    this.objects = new ArrayList<>();
    this.lightSources = new ArrayList<>();
    this.ambientLight = ambientLight;
    this.camera = camera;
    this.background = background;
    this.reflectionDepth = reflectionDepth;
  }

  public void writeToPng(File file) throws IOException {
    BufferedImage image =
        new BufferedImage(camera.getImageWidth(), camera.getImageHeight(), TYPE_INT_ARGB);
    for (int x = 0; x < camera.getImageWidth(); x++) {
      for (int y = 0; y < camera.getImageHeight(); y++) {
        Vector3 color = pixelToColor(x, y);
        image.setRGB(
            x,
            y,
            new Color(
                (float) (1 - Math.exp(-color.getX())),
                (float) (1 - Math.exp(-color.getY())),
                (float) (1 - Math.exp(-color.getZ())))
                .getRGB());
      }
    }
    ImageIO.write(image, "png", file);
  }

  public Vector3 pixelToColor(double x, double y) {
    Ray primaryRay = camera.rayThroughPixel(x, y);
    Hit hit = closestHit(primaryRay);
    if (hit.getObject() != null) {
      return hitColor(primaryRay, hit, 0);
    }
    return background.multiply(ambientLight);
  }

  public Hit closestHit(Ray ray) {
    double minDistance = Double.MAX_VALUE;
    Object closestObject = null;
    Vector3 closestIntersection = null;
    for (Object object : objects) {
      List<Vector3> intersections = ray.intersections(object);
      for (int i = 0; i < intersections.size(); i++) {
        if (intersections.get(i).subtract(ray.getOrigin()).norm()
            < 1 / camera.getPixelToWorldUnitRatio()) {
          intersections.remove(i);
        }
      }
      for (Vector3 intersection : intersections) {
        double distance = intersection.subtract(ray.getOrigin()).norm();
        if (distance < minDistance) {
          minDistance = distance;
          closestObject = object;
          closestIntersection = intersection;
        }
      }
    }
    return new Hit(closestObject, closestIntersection);
  }

  public Vector3 hitColor(Ray ray, Hit hit, int recursionDepth) {
    Vector3 color = new Vector3(0, 0, 0);
    Vector3 normal = hit.getObject().normal(ray.getDirection(), hit.getIntersection()).normalize();
    for (LightSource lightSource : lightSources) {
      Ray shadowRay =
          new Ray(
              hit.getIntersection(),
              lightSource.getPosition().subtract(hit.getIntersection()),
              1,
              false);
      boolean inShadow = normal.dotProduct(shadowRay.getDirection().normalize()) < 0;
      if (!inShadow) {
        for (Object object : objects) {
          List<Vector3> intersections = shadowRay.intersections(object);
          for (int i = 0; i < intersections.size(); i++) {
            if (intersections.get(i).subtract(hit.getIntersection()).norm()
                < 1 / camera.getPixelToWorldUnitRatio()
                || lightSource
                .getPosition()
                .subtract(intersections.get(i))
                .dotProduct(shadowRay.getDirection())
                < 0) {
              intersections.remove(i);
            }
          }
          if (!intersections.isEmpty()) {
            inShadow = true;
            break;
          }
        }
      }
      if (!inShadow) {
        color =
            color.add(
                hit.getObject()
                    .getColor()
                    .multiply(
                        ambientLight
                            + lightSource.getBrightness()
                            * normal.dotProduct(shadowRay.getDirection().normalize())));
      }
    }
    if (color.equals(new Vector3(0, 0, 0))) {
      color = color.add(hit.getObject().getColor().multiply(ambientLight));
    }
    if (recursionDepth < reflectionDepth && hit.getObject().getDiffusivity() < 1) {
      Vector3 normalizedRay = ray.getDirection().normalize();
      double normalParallelProjection = normalizedRay.dotProduct(normal);
      Vector3 normalParallelRay = normal.multiply(normalParallelProjection);
      Vector3 normalPerpendicularRay = normalizedRay.subtract(normalParallelRay);
      Vector3 reflectedDirection = normalPerpendicularRay.subtract(normalParallelRay);
      double n1 = ray.getCurrentRefractionIndex();
      double n2 = ray.isInsideObject() ? 1 : hit.getObject().getRefractionIndex();
      double cosI = Math.abs(normalParallelProjection);
      double sinI = Math.sqrt(1 - cosI * cosI);
      double sinT = n1 / n2 * sinI;
      double reflectance;
      double cosT;
      if (sinT * sinT >= 1) {
        reflectance = 1;
        cosT = 0;
      } else {
        cosT = Math.sqrt(1 - sinT * sinT);
        double reflectancePerpendicular = (n2 * cosT - n1 * cosI) / (n2 * cosT + n1 * cosI);
        double reflectanceParallel = (n1 * cosT - n2 * cosI) / (n1 * cosT + n2 * cosI);
        reflectance =
            0.5
                * (reflectancePerpendicular * reflectancePerpendicular
                + reflectanceParallel * reflectanceParallel);
      }
      Ray reflectionRay =
          new Ray(hit.getIntersection(), reflectedDirection, n1, ray.isInsideObject());
      Hit reflectionHit = closestHit(reflectionRay);
      Ray refractionRay =
          new Ray(
              hit.getIntersection(),
              normalizedRay
                  .add(normal.multiply(cosI))
                  .multiply(n1 / n2)
                  .add(normal.multiply(-cosT)),
              n2,
              !ray.isInsideObject());
      Hit refractionHit = closestHit(refractionRay);
      if (reflectionHit.getObject() != null) {
        if (refractionHit.getObject() != null) {
          color =
              color
                  .multiply(hit.getObject().getDiffusivity())
                  .add(
                      hitColor(reflectionRay, reflectionHit, recursionDepth + 1)
                          .multiply((1 - hit.getObject().getDiffusivity()) * reflectance))
                  .add(
                      hitColor(refractionRay, refractionHit, recursionDepth + 1)
                          .multiply((1 - hit.getObject().getDiffusivity()) * cosT));
        } else {
          color =
              color
                  .multiply(
                      hit.getObject().getDiffusivity()
                          + (1 - hit.getObject().getDiffusivity()) * cosT)
                  .add(
                      hitColor(reflectionRay, reflectionHit, recursionDepth + 1)
                          .multiply((1 - hit.getObject().getDiffusivity()) * reflectance));
        }
      } else if (refractionHit.getObject() != null) {
        color =
            color
                .multiply(
                    hit.getObject().getDiffusivity()
                        + (1 - hit.getObject().getDiffusivity()) * reflectance)
                .add(
                    hitColor(refractionRay, refractionHit, recursionDepth + 1)
                        .multiply((1 - hit.getObject().getDiffusivity()) * cosT));
      }
    }
    return color;
  }

  public void addObject(Object object) {
    objects.add(object);
  }

  public void addLightSource(LightSource lightSource) {
    lightSources.add(lightSource);
  }
}
