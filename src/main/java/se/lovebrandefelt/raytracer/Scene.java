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
  private Camera camera;

  public Scene(Camera camera) {
    this.objects = new ArrayList<>();
    this.lightSources = new ArrayList<>();
    this.camera = camera;
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
                (int) (color.getX() * 255),
                (int) (color.getY() * 255),
                (int) (color.getZ() * 255))
                .getRGB());
      }
    }
    ImageIO.write(image, "png", file);
  }

  public Vector3 pixelToColor(int x, int y) {
    Ray primaryRay = camera.rayThroughPixel(x, y);
    double minDistance = Double.MAX_VALUE;
    Sphere closestObject = null;
    Vector3 closestIntersection = null;
    for (Sphere object : objects) {
      Vector3[] intersections = primaryRay.intersections(object);
      if (intersections.length > 0) {
        for (Vector3 intersection : intersections) {
          double distance = intersection.subtract(primaryRay.getOrigin()).norm();
          if (distance < minDistance) {
            closestObject = object;
            closestIntersection = intersection;
          }
        }
      }
    }
    if (closestObject != null) {
      Vector3 color = closestObject.getColor();
      boolean illuminated = false;
      for (LightSource lightSource : lightSources) {
        Ray shadowRay = new Ray(closestIntersection, lightSource.getPosition().subtract(closestIntersection));
        boolean inShadow = false;
        for (Sphere object : objects) {
          if (object != closestObject) {
            Vector3[] intersections = shadowRay.intersections(object);
            if (intersections.length > 0) {
              inShadow = true;
              break;
            }
          }
        }
        if (!inShadow) {
          color = color.multiply(lightSource.getBrightness());
          illuminated = true;
        }
      }
      if (!illuminated) {
        return new Vector3(0, 0, 0);
      } else {
        return color;
      }
    }
    return new Vector3(0, 0, 0);
  }

  public void addObject(Sphere object) {
    objects.add(object);
  }

  public void addLightSource(LightSource lightSource) {
    lightSources.add(lightSource);
  }
}
