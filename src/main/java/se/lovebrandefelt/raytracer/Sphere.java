package se.lovebrandefelt.raytracer;

public class Sphere extends Object {
  private Vector3 center;
  private double radius;

  public Sphere(
      Vector3 center, double radius, Vector3 color, double diffusivity, double refractionIndex) {
    super(color, diffusivity, refractionIndex);
    this.center = center;
    this.radius = radius;
  }

  @Override
  public Vector3 normal(Vector3 incomingDirection, Vector3 intersection) {
    return intersection.subtract(center).normalize();
  }

  public Vector3 getCenter() {
    return center;
  }

  public double getRadius() {
    return radius;
  }
}
