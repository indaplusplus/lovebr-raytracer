package se.lovebrandefelt.raytracer;

public class Vector3 {
  private double x;
  private double y;
  private double z;

  public Vector3(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector3 add(Vector3 other) {
    return new Vector3(x + other.x, y + other.y, z + other.z);
  }

  public Vector3 subtract(Vector3 other) {
    return new Vector3(x - other.x, y - other.y, z - other.z);
  }

  public Vector3 multiply(double scalar) {
    return new Vector3(x * scalar, y * scalar, z * scalar);
  }

  public double norm() {
    return Math.sqrt(x * x + y * y + z * z);
  }

  public Vector3 normalize() {
    return multiply(1 / norm());
  }

  public double dotProduct(Vector3 other) {
    return x * other.x + y * other.y + z * other.z;
  }

  public Vector3 transform(Matrix matrix) {
    Matrix result = new Matrix(new double[][] {{x, y, z}}).multiply(matrix);
    return new Vector3(result.get(0, 0), result.get(0, 1), result.get(0, 2));
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getZ() {
    return z;
  }
}
