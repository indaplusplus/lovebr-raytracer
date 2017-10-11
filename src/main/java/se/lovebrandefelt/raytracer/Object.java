package se.lovebrandefelt.raytracer;

public abstract class Object {
  private Vector3 color;
  private double diffusivity;
  private double refractionIndex;

  public Object(Vector3 color, double diffusivity, double refractionIndex) {
    this.color = color;
    this.diffusivity = diffusivity;
    this.refractionIndex = refractionIndex;
  }

  public abstract Vector3 normal(Vector3 incomingDirection, Vector3 intersection);

  public Vector3 getColor() {
    return color;
  }

  protected void setColor(Vector3 color) {
    this.color = color;
  }

  public double getDiffusivity() {
    return diffusivity;
  }

  protected void setDiffusivity(double diffusivity) {
    this.diffusivity = diffusivity;
  }

  public double getRefractionIndex() {
    return refractionIndex;
  }

  protected void setRefractionIndex(double refractionIndex) {
    this.refractionIndex = refractionIndex;
  }
}
