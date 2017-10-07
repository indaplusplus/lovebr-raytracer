package se.lovebrandefelt.raytracer;

public class Ray {
  private Vector3 origin;
  private Vector3 direction;

  public Ray(Vector3 origin, Vector3 direction) {
    this.origin = origin;
    this.direction = direction;
  }

  public Vector3[] intersections(Sphere sphere) {
    Vector3 difference = origin.subtract(sphere.getCenter());
    double a = direction.dotProduct(direction);
    double b = direction.multiply(2).dotProduct(difference);
    double c = difference.dotProduct(difference) - sphere.getRadius() * sphere.getRadius();
    double discriminant = b * b - 4 * a * c;
    if (discriminant < 0) {
      return new Vector3[] {};
    }
    if (discriminant > 0) {
      return new Vector3[] {
          origin.add(direction.multiply((-b + Math.sqrt(discriminant)) / (2 * a))),
          origin.add(direction.multiply((-b - Math.sqrt(discriminant)) / (2 * a)))
      };
    }
    return new Vector3[] {origin.add(direction.multiply(-b / (2 * a)))};
  }

  public Vector3 getOrigin() {
    return origin;
  }

  public Vector3 getDirection() {
    return direction;
  }
}
