package se.lovebrandefelt.raytracer;

import java.io.File;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    Camera camera = new Camera(new Vector3(5, -15, 0), new Vector3(1, -3, 3), 10, 16, 12, 0, 100);
    Scene scene = new Scene(0.5, camera, new Vector3(0, 0, 0), 10);
    scene.addObject(new Sphere(new Vector3(20, -5, 20), 5, new Vector3(1, 0, 0), 1, 1));
    scene.addObject(new Sphere(new Vector3(0, -10, 15), 10, new Vector3(1, 1, 0), 1, 1));
    scene.addObject(new Sphere(new Vector3(15, -10, 0), 5, new Vector3(0.5, 1, 0.25), 1, 1));
    scene.addLightSource(new LightSource(new Vector3(15, -100, 15), 1, 1));
    scene.writeToPng(new File("test.png"));
  }
}
