package se.lovebrandefelt.raytracer;

public class Camera {
  private Vector3 position;
  private Vector3 direction;
  private double viewDistance;
  private double viewWidth;
  private double viewHeight;
  private double viewRotation;
  private int imageWidth;
  private int imageHeight;
  private Matrix pixelToWorldUnitMatrix;

  public Camera(
      Vector3 position,
      Vector3 direction,
      double viewDistance,
      double viewWidth,
      double viewHeight,
      double viewRotation,
      int imageWidth,
      int imageHeight) {
    this.position = position;
    this.direction = direction;
    this.viewDistance = viewDistance;
    this.viewWidth = viewWidth;
    this.viewHeight = viewHeight;
    this.viewRotation = viewRotation;
    this.imageWidth = imageWidth;
    this.imageHeight = imageHeight;
    Vector3 screenCenter = direction.normalize().multiply(viewDistance);
    this.pixelToWorldUnitMatrix =
        Matrix.yRotationMatrix(Math.atan2(screenCenter.getZ(), screenCenter.getX()) - Math.PI / 2)
            .multiply(
                Matrix.xRotationMatrix(
                    Math.atan2(
                        Math.sqrt(
                            screenCenter.getX() * screenCenter.getX()
                                + screenCenter.getZ() * screenCenter.getZ()),
                        screenCenter.getY())
                        - Math.PI / 2));
  }

  private Vector3 pixelToDirection(int x, int y) {
    return new Vector3(
        -viewWidth / 2 + x / (imageWidth / viewWidth),
        -viewHeight / 2 + y / (imageHeight / viewHeight),
        viewDistance)
        .transform(pixelToWorldUnitMatrix);
  }

  public Ray rayThroughPixel(int x, int y) {
    return new Ray(position, pixelToDirection(x, y));
  }

  public Vector3 getPosition() {
    return position;
  }

  public Vector3 getDirection() {
    return direction;
  }

  public double getViewWidth() {
    return viewWidth;
  }

  public double getViewHeight() {
    return viewHeight;
  }

  public double getViewRotation() {
    return viewRotation;
  }

  public int getImageWidth() {
    return imageWidth;
  }

  public int getImageHeight() {
    return imageHeight;
  }
}
