package se.lovebrandefelt.raytracer;

public class Camera {
  private Vector3 position;
  private Vector3 direction;
  private double viewWidth;
  private double viewHeight;
  private double viewRotation;
  private int imageWidth;
  private int imageHeight;
  private Matrix pixelToWorldUnitMatrix;

  public Camera(
      Vector3 position,
      Vector3 direction,
      double viewWidth,
      double viewHeight,
      double viewRotation,
      int imageWidth,
      int imageHeight) {
    this.position = position;
    this.direction = direction;
    this.viewWidth = viewWidth;
    this.viewHeight = viewHeight;
    this.viewRotation = viewRotation;
    this.imageWidth = imageWidth;
    this.imageHeight = imageHeight;
    this.pixelToWorldUnitMatrix =
        Matrix.yRotationMatrix(Math.atan2(-direction.getZ(), direction.getX()) - Math.PI / 2)
            .multiply(
                Matrix.xRotationMatrix(
                    Math.atan2(
                        Math.sqrt(
                            direction.getX() * direction.getX()
                                + direction.getZ() * direction.getZ()),
                        direction.getY())
                        - Math.PI / 2));
  }

  private Vector3 pixelToWorldUnit(int x, int y) {
    return position.add(
        new Vector3(
            -viewWidth / 2 + x * imageWidth / viewWidth,
            -viewHeight / 2 + y * imageHeight / viewHeight,
            0)
            .transform(pixelToWorldUnitMatrix));
  }

  //public Color pixelToColor(int x, int y) {
  //}

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
