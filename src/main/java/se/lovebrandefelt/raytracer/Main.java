package se.lovebrandefelt.raytracer;

import java.io.File;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    Camera camera = new Camera(new Vector3(-5, 0, 0), new Vector3(0, 0, 1), 10, 10, 10, 0, 800, 800);
    Scene scene = new Scene(0.3, camera, new Vector3(0.1, 0.1, 0.1));
    scene.addObject(new Sphere(new Vector3(0, 0, 20), 5, new Vector3(0.75, 0.75, 1)));
    scene.addObject(new Sphere(new Vector3(-10, 0, 20), 5, new Vector3(1, 0, 0)));
    scene.addObject(new Sphere(new Vector3(-5, -1, 19), 2, new Vector3(0, 0.5, 0)));
    //scene.addLightSource(new LightSource(new Vector3(0, -5, 10), 0.8));
    scene.addLightSource(new LightSource(new Vector3(-5, -1, 15), 1));
    scene.writeToPng(new File("test.png"));
  }
}
