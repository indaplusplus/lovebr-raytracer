package se.lovebrandefelt.raytracer;

import java.io.File;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    Camera camera = new Camera(new Vector3(-10, 0, 0), new Vector3(1, 0, 1), 10, 10, 10, 0, 800, 800);
    Scene scene = new Scene(camera);
    scene.addObject(new Sphere(new Vector3(0, 0, 40), 4, new Vector3(1, 0, 0)));
    scene.addObject(new Sphere(new Vector3(0, 5, 20), 4, new Vector3(1, 0, 0)));
    scene.addLightSource(new LightSource(new Vector3(10, 10, 10), 1));
    scene.writeToPng(new File("test.png"));
  }
}
