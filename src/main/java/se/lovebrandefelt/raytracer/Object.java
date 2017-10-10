package se.lovebrandefelt.raytracer;

public abstract class Object {
  private Vector3 color;
  private double reflexivity;
  private double refractionIndex;

  public Object(Vector3 color, double reflexivity, double refractionIndex) {
    this.color = color;
    this.reflexivity = reflexivity;
    this.refractionIndex = refractionIndex;
  }

  public abstract Vector3 normal(Vector3 incomingDirection, Vector3 intersection);

  public Vector3 getColor() {
    return color;
  }

  protected void setColor(Vector3 color) {
    this.color = color;
  }

  public double getReflexivity() {
    return reflexivity;
  }

  protected void setReflexivity(double reflexivity) {
    this.reflexivity = reflexivity;
  }

  public double getRefractionIndex() {
    return refractionIndex;
  }

  protected void setRefractionIndex(double refractionIndex) {
    this.refractionIndex = refractionIndex;
  }
}
