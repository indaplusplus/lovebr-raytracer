package se.lovebrandefelt.raytracer;

public class Hit {
  private Sphere object;
  private Vector3 intersection;

  public Hit(Sphere object, Vector3 intersection) {
    this.object = object;
    this.intersection = intersection;
  }

  public Sphere getObject() {
    return object;
  }

  public Vector3 getIntersection() {
    return intersection;
  }
}
