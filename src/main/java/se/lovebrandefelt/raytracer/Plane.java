package se.lovebrandefelt.raytracer;

public class Plane extends Object {
  private Vector3 position;
  private Vector3 normal;

  public Plane(
      Vector3 position, Vector3 normal, Vector3 color, double reflexivity, double refractionIndex) {
    super(color, reflexivity, refractionIndex);
    this.position = position;
    this.normal = normal;
  }

  @Override
  public Vector3 normal(Vector3 incomingDirection, Vector3 intersection) {
    return incomingDirection.dotProduct(normal) > 0
        ? new Vector3(0, 0, 0).subtract(normal)
        : normal;
  }

  public Vector3 getPosition() {
    return position;
  }

  public Vector3 getNormal() {
    return normal;
  }
}
