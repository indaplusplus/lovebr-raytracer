package se.lovebrandefelt.raytracer;

public class Sphere {
  private Vector3 center;
  private double radius;
  private Vector3 color;
  private double reflexivity;
  private double refractionIndex;

  public Sphere(Vector3 center, double radius, Vector3 color, double reflexivity, double refractionIndex) {
    this.center = center;
    this.radius = radius;
    this.color = color;
    this.reflexivity = reflexivity;
    this.refractionIndex = refractionIndex;
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

  public double getReflexivity() {
    return reflexivity;
  }

  public double getRefractionIndex() {
    return refractionIndex;
  }
}
