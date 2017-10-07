package se.lovebrandefelt.raytracer;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

public class Scene {
  private List<Sphere> objects;
  private List<LightSource> lightSources;
  private Camera camera;

  public Scene(List<Sphere> objects, List<LightSource> lightSources, Camera camera) {
    this.objects = objects;
    this.lightSources = lightSources;
    this.camera = camera;
  }

  public void writeToPng(File file) throws IOException {
    BufferedImage image =
        new BufferedImage(camera.getImageWidth(), camera.getImageHeight(), TYPE_INT_ARGB);
    for (int x = 0; x < camera.getImageWidth(); x++) {
      for (int y = 0; y < camera.getImageHeight(); y++) {
        image.setRGB(x, y, camera.pixelToColor(x, y).getRGB());
      }
    }
    ImageIO.write(image, "png", file);
  }
}
