package se.lovebrandefelt.raytracer;

public class Sphere {
  private Vector3 center;
  private double radius;
  private Vector3 color;

  public Sphere(Vector3 center, double radius) {
    this.center = center;
    this.radius = radius;
  }

  public Sphere(Vector3 center, double radius, Vector3 color) {
    this.center = center;
    this.radius = radius;
    this.color = color;
  }

  public Vector3 getCenter() {
    return center;
  }

  public double getRadius() {
    return radius;
  }

  public Vector3 getColor() {
    return color;
  }
}
