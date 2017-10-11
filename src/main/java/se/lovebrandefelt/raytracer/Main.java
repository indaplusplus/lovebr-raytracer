package se.lovebrandefelt.raytracer;

import java.io.File;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    Camera camera = new Camera(new Vector3(0, -10, 0), new Vector3(0, -0.5, 1), 10, 16, 9, 0, 120);
    Scene scene = new Scene(0.5, camera, new Vector3(0, 0, 0), 10);
    scene.addObject(
        new Plane(new Vector3(0, 0, 0), new Vector3(0, 1, 0), new Vector3(1, 0, 0), 1, 1));
    scene.addObject(
        new Plane(new Vector3(0, -20, 0), new Vector3(0, 1, 0), new Vector3(1, 0, 0), 1, 1));
    scene.addObject(
        new Plane(new Vector3(-10, 0, 0), new Vector3(1, 0, 0), new Vector3(0, 1, 0), 1, 1));
    scene.addObject(
        new Plane(new Vector3(10, 0, 0), new Vector3(1, 0, 0), new Vector3(0, 1, 0), 1, 1));
    scene.addObject(
        new Plane(new Vector3(0, 0, 20), new Vector3(0, 0, 1), new Vector3(1, 1, 1), 0, 2.417));
    scene.addObject(
        new Plane(new Vector3(0, 0, 0), new Vector3(0, 0, 1), new Vector3(1, 1, 1), 1, 1));
    scene.addObject(new Sphere(new Vector3(0, -2, 10), 2, new Vector3(1, 1, 1), 0.02, 1.51));
    scene.addObject(new Sphere(new Vector3(-6, -2, 3), 2, new Vector3(1, 0, 1), 1, 1));
    scene.addObject(new Sphere(new Vector3(6, -2, 3), 2, new Vector3(0, 1, 1), 1, 1));
    scene.addLightSource(new LightSource(new Vector3(-9, -19, 19), 0.5, 1));
    scene.addLightSource(new LightSource(new Vector3(9, -19, 19), 0.5, 1));
    scene.addLightSource(new LightSource(new Vector3(-9, -19, 1), 0.5, 1));
    scene.addLightSource(new LightSource(new Vector3(9, -19, 1), 0.5, 1));
    scene.writeToPng(new File("test.png"));
  }
}
