package algeo.lib;

public class Matrix {
  private double[][] data;
  private int rows;
  private int cols;

  public Matrix(double[][] initialData) {
    this.rows = initialData.length;
    this.cols = initialData[0].length;
    this.data = new double[rows][cols];
    for (int i=0;i<rows;i++) {
      for (int j=0;j<cols;j++) {
        this.data[i][j] = initialData[i][j];
      }
    }
  }

  public Matrix(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    this.data = new double[rows][cols];
  }

  public int getRows() {
    return this.rows;
  }

  public int getCols() {
    return this.cols;
  }

  public double getElement(int row, int col) {
    if (row >= 0 && row < this.rows && col >= 0 && col < this.cols) {
      return this.data[row][col];
    }
    throw new IndexOutOfBoundsException("Indeks matriks tidak valid: (" + row + "," + col + ")");
  }

  public double setElement(int row, int col, double value) {
    if (row >= 0 && row < this.rows && col >= 0 && col < this.cols) {
      this.data[row][col] = value;
    }
    throw new IndexOutOfBoundsException("Indeks matriks tidak valid: (" + row + "," + col + ")");
  }

  public void swapRows(int row1, int row2) {
    double[] temp = this.data[row1];
    this.data[row1] = this.data[row2];
    this.data[row2] = temp;
  }

  public void multiplyRow(int row, double scalar) {
    for (int j=0; j<this.cols; j++) {
      this.data[row][j] *= scalar;
    }
  }

  public void addMultiplyOfRow(int targetRow, int sourceRow, double scalar) {
    for (int j = 0; j < this.cols; j++) {
      this.data[targetRow][j] += scalar * this.data[sourceRow][j];
    }
  }

  public Matrix copy() {
    return new Matrix(this.data);
  }

  public void display() {
    for (int i = 0; i < this.rows; i++) {
      System.out.println("| ");
      for (int j = 0; j < this.cols; j++) {
        System.out.printf("%8.3f ", this.data[i][j]);
      }
      System.out.println("|");
    }
  }
}
