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
          new Ray(hit.getIntersection(), lightSource.getPosition().subtract(hit.getIntersection()));
      boolean inShadow = normal.dotProduct(shadowRay.getDirection().normalize()) < 0;
      if (!inShadow) {
        for (Object object : objects) {
          List<Vector3> intersections = shadowRay.intersections(object);
          for (int i = 0; i < intersections.size(); i++) {
            if (intersections.get(i).subtract(hit.getIntersection()).norm()
                < 1 / camera.getPixelToWorldUnitRatio()) {
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
    if (recursionDepth < reflectionDepth && hit.getObject().getReflexivity() > 0) {
      Vector3 normalizedRay = ray.getDirection().normalize();
      Vector3 normalParallelRay = normal.multiply(normalizedRay.dotProduct(normal));
      Vector3 normalPerpendicularRay = normalizedRay.subtract(normalParallelRay);
      Vector3 reflectedDirection = normalPerpendicularRay.subtract(normalParallelRay);
      Ray reflectionRay = new Ray(hit.getIntersection(), reflectedDirection);
      Hit reflectionHit = closestHit(reflectionRay);
      if (reflectionHit.getObject() != null) {
        color =
            color
                .multiply(1 - hit.getObject().getReflexivity())
                .add(
                    hitColor(reflectionRay, reflectionHit, recursionDepth + 1)
                        .multiply(hit.getObject().getReflexivity()));
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
