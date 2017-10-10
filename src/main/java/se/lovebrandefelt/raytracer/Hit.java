package se.lovebrandefelt.raytracer;

public class Hit {
  private Object object;
  private Vector3 intersection;

  public Hit(Object object, Vector3 intersection) {
    this.object = object;
    this.intersection = intersection;
  }

  public Object getObject() {
    return object;
  }

  public Vector3 getIntersection() {
    return intersection;
  }
}
