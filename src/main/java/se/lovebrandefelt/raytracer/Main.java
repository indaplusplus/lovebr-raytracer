package se.lovebrandefelt.raytracer;

import java.io.File;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    Camera camera = new Camera(new Vector3(0, 0, 0), new Vector3(0, 0, 1), 10, 16, 12, 0, 100);
    Scene scene = new Scene(0.5, camera, new Vector3(0, 0, 0), 10);
    scene.addObject(
        new Plane(new Vector3(0, 0, 50), new Vector3(0, 0, 1), new Vector3(1, 1, 1), 0, 1));
    scene.addObject(new Sphere(new Vector3(0, 0, 17.5), 2.5, new Vector3(1, 0, 0), 0, 1));
    scene.addLightSource(new LightSource(new Vector3(0, 0, 10), 1, 1));
    scene.writeToPng(new File("test.png"));
  }
}
