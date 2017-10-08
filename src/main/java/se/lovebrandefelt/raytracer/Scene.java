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
  private List<Sphere> objects;
  private List<LightSource> lightSources;
  private double ambientLight;
  private Camera camera;
  private Vector3 background;

  public Scene(double ambientLight, Camera camera, Vector3 background) {
    this.objects = new ArrayList<>();
    this.lightSources = new ArrayList<>();
    this.ambientLight = ambientLight;
    this.camera = camera;
    this.background = background;
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
            new Color((float) color.getX(), (float) color.getY(), (float) color.getZ()).getRGB());
      }
    }
    ImageIO.write(image, "png", file);
  }

  public Vector3 pixelToColor(double x, double y) {
    Ray primaryRay = camera.rayThroughPixel(x, y);
    double minDistance = Double.MAX_VALUE;
    Sphere closestObject = null;
    Vector3 closestIntersection = null;
    for (Sphere object : objects) {
      List<Vector3> intersections = primaryRay.intersections(object);
      for (Vector3 intersection : intersections) {
        double distance = intersection.subtract(primaryRay.getOrigin()).norm();
        if (distance < minDistance) {
          minDistance = distance;
          closestObject = object;
          closestIntersection = intersection;
        }
      }
    }
    if (closestObject != null) {
      Vector3 normal =
          closestIntersection
              .subtract(closestObject.getCenter().add(primaryRay.getDirection()))
              .normalize();
      Vector3 normalParallelDirection =
          new Vector3(0, 0, 0)
              .subtract(normal.multiply(primaryRay.getDirection().dotProduct(normal)));
      Vector3 reflectedDirection =
          normalParallelDirection.add(primaryRay.getDirection().add(normalParallelDirection));
      double brightness = 0;
      for (LightSource lightSource : lightSources) {
        Ray shadowRay =
            new Ray(closestIntersection, lightSource.getPosition().subtract(closestIntersection));
        boolean inShadow = false;
        for (Sphere object : objects) {
          List<Vector3> intersections = shadowRay.intersections(object);
          for (int i = 0; i < intersections.size(); i++) {
            if (intersections.get(i).subtract(closestIntersection).norm()
                < 1 / camera.getPixelToWorldUnitRatio()) {
              intersections.remove(i);
            }
          }
          if (!intersections.isEmpty()) {
            inShadow = true;
            break;
          }
        }
        if (!inShadow) {
          brightness +=
              lightSource.getBrightness()
                  * (reflectedDirection.normalize().dotProduct(shadowRay.getDirection().normalize())
                  + 1)
                  / 2;
        }
      }
      Vector3 result =
          closestObject
              .getColor()
              .multiply(
                  Math.max(
                      brightness,
                      ambientLight * (reflectedDirection.normalize().dotProduct(normal) + 1) / 2));
      return new Vector3(
          Math.min(result.getX(), 1), Math.min(result.getY(), 1), Math.min(result.getZ(), 1));
    }
    return background.multiply(ambientLight);
  }

  public void addObject(Sphere object) {
    objects.add(object);
  }

  public void addLightSource(LightSource lightSource) {
    lightSources.add(lightSource);
  }
}
