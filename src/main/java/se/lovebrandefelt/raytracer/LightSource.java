package se.lovebrandefelt.raytracer;

public class LightSource {
  private Vector3 position;
  private double brightness;

  public LightSource(Vector3 position, double brightness) {
    this.position = position;
    this.brightness = brightness;
  }

  public Vector3 getPosition() {
    return position;
  }

  public double getBrightness() {
    return brightness;
  }
}
