package se.lovebrandefelt.raytracer;

import java.util.ArrayList;
import java.util.List;

public class Ray {
  private Vector3 origin;
  private Vector3 direction;

  public Ray(Vector3 origin, Vector3 direction) {
    this.origin = origin;
    this.direction = direction;
  }

  public List<Vector3> intersections(Object object) {
    List<Vector3> result = new ArrayList<>();
    if (object instanceof Sphere) {
      Sphere sphere = (Sphere) object;
      Vector3 difference = origin.subtract(sphere.getCenter());
      double a = direction.dotProduct(direction);
      double b = direction.multiply(2).dotProduct(difference);
      double c = difference.dotProduct(difference) - sphere.getRadius() * sphere.getRadius();
      double discriminant = b * b - 4 * a * c;
      if (discriminant > 0) {
        double q =
            (b > 0) ? -0.5 * (b + Math.sqrt(discriminant)) : -0.5 * (b - Math.sqrt(discriminant));
        double x0 = q / a;
        double x1 = c / q;
        if (x0 > 0) {
          result.add(origin.add(direction.multiply(x0)));
        }
        if (x1 > 0) {
          result.add(origin.add(direction.multiply(x1)));
        }
      } else if (discriminant == 0) {
        double x = -0.5 * b / a;
        if (x > 0) {
          result.add(origin.add(direction.multiply(x)));
        }
      }
    } else if (object instanceof Plane) {
      Plane plane = (Plane) object;
      double directionProduct = direction.normalize().dotProduct(plane.getNormal().normalize());
      if (directionProduct != 0) {
        double t =
            plane.getPosition().subtract(origin).dotProduct(plane.getNormal().normalize())
                / directionProduct;
        if (t > 0) {
          result.add(origin.add(direction.multiply(t)));
        }
      }
    }
    return result;
  }

  public Vector3 getOrigin() {
    return origin;
  }

  public Vector3 getDirection() {
    return direction;
  }
}
