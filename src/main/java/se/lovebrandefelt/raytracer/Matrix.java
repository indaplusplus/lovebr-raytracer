package se.lovebrandefelt.raytracer;

public class Matrix {

  private double[][] matrix;

  public Matrix(double[][] matrix) {
    this.matrix = matrix;
  }

  public static Matrix unitMatrix(int height, int width) {
    double[][] matrix = new double[height][width];
    for (int row = 0; row < height; row++) {
      for (int column = 0; column < width; column++) {
        if (row == column) {
          matrix[row][column] = 1;
        } else {
          matrix[row][column] = 0;
        }
      }
    }
    return new Matrix(matrix);
  }

  public static Matrix xRotationMatrix(double angle) {
    return new Matrix(
        new double[][] {
            {1, 0, 0}, {0, Math.cos(angle), -Math.sin(angle)}, {0, Math.sin(angle), Math.cos(angle)}
        });
  }

  public static Matrix yRotationMatrix(double angle) {
    return new Matrix(
        new double[][] {
            {Math.cos(angle), 0, Math.sin(angle)}, {0, 1, 0}, {-Math.sin(angle), 0, Math.cos(angle)}
        });
  }

  public static Matrix zRotationMatrix(double angle) {
    return new Matrix(
        new double[][] {
            {Math.cos(angle), -Math.sin(angle), 0}, {Math.sin(angle), Math.cos(angle), 0}, {0, 0, 1}
        });
  }

  public Matrix multiply(Matrix other) {
    double[][] result = new double[height()][other.width()];
    for (int row = 0; row < result.length; row++) {
      for (int column = 0; column < result[0].length; column++) {
        double value = 0;
        for (int i = 0; i < width(); i++) {
          value += get(row, i) * other.get(i, column);
        }
        result[row][column] = value;
      }
    }
    return new Matrix(result);
  }

  public double get(int row, int column) {
    return matrix[row][column];
  }

  public int height() {
    return matrix.length;
  }

  public int width() {
    return matrix[0].length;
  }
}
