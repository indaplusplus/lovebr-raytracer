package se.lovebrandefelt.raytracer;

public class LightSource {
  private Vector3 position;
  private double brightness;
  private double spread;

  public LightSource(Vector3 position, double brightness, double spread) {
    this.position = position;
    this.brightness = brightness;
    this.spread = spread;
  }

  public Vector3 getPosition() {
    return position;
  }

  public double getBrightness() {
    return brightness;
  }

  public double getSpread() {
    return spread;
  }
}
