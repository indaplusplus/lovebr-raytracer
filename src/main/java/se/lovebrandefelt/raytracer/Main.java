package se.lovebrandefelt.raytracer;

import java.io.File;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    Camera camera = new Camera(new Vector3(0, 0, 0), new Vector3(-5, 0, 20), 10, 16, 12, 0, 100);
    Scene scene = new Scene(0.75, camera, new Vector3(0.25, 0.25, 0.25));
    scene.addObject(new Sphere(new Vector3(2, 0, 18), 5, new Vector3(0.5, 0.75, 0.5)));
    scene.addObject(new Sphere(new Vector3(-10, -2, 20), 4, new Vector3(1, 0.5, 0)));
    scene.addObject(new Sphere(new Vector3(-4, -5, 18), 2, new Vector3(1, 0.5, 0.75)));
    scene.addLightSource(new LightSource(new Vector3(-10, -1, 10), 1));
    scene.writeToPng(new File("test.png"));
  }
}
