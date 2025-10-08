package algeo.lib;

public class Matrix {
  private double[][] data;
  private int rows;
  private int cols;

  // Konstruktor
  public Matrix(double[][] initialData) {
    if (initialData == null || initialData.length == 0 || initialData[0].length == 0) {
      throw new IllegalArgumentException("Data matriks tidak boleh kosong");
    }
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
    if (rows <= 0 || cols <= 0) {
      throw new IllegalArgumentException("Jumlah baris dan kolom harus positif");
    }
    this.rows = rows;
    this.cols = cols;
    this.data = new double[rows][cols];
  }

  // Selektor
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

  public void setElement(int row, int col, double value) {
    if (row >= 0 && row < this.rows && col >= 0 && col < this.cols) {
      this.data[row][col] = value;
      return;
    }
    throw new IndexOutOfBoundsException("Indeks matriks tidak valid: (" + row + "," + col + ")");
  }
  
  // Operasi Baris Elementer
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

  // Operasi Matrix : Copy, Display (Print)
  public Matrix copy() {
    return new Matrix(this.data);
  }

  public void display() {
    for (int i = 0; i < this.rows; i++) {
      System.out.print("| ");
      for (int j = 0; j < this.cols; j++) {
        System.out.printf("%8.3f ", this.data[i][j]);
      }
      System.out.println("|");
    }
  }

  // Operasi Matriks : Mengubah Matriks determinan menjadi Matriks Eselon Baris (Tidak mempunyai leading one!)
  public Matrix toEselonMatrix() {
    Matrix m = this.copy();
    int rows = m.getRows();

    for (int j = 0; j < rows; j++) {
      int pivot = j;
      for (int i = j + 1; i < rows; i++) {
        if(Math.abs(m.getElement(i, j)) > Math.abs(m.getElement(pivot, j))) {
          pivot = i;
        }
      }
      m.swapRows(j, pivot);

      for (int i = j + 1; i < rows; i++) {
        double factor = m.getElement(i, j) / m.getElement(j, j);
        m.addMultiplyOfRow(i, j, -factor);
      }
    }

    return m;
  }

  // Operasi Matriks : Perkalian Matriks
  public static Matrix multiply(Matrix a, Matrix b) {
    if (a.getCols() != b.getRows()) {
      System.out.println("Ukuran matriks tidak valid untuk perkalian");
      return null;
    }

    Matrix result = new Matrix(a.getRows(), b.getCols());

    for (int i=0; i<a.getRows(); i++) {
      for (int j = 0; j < b.getCols(); j++) {
        double sum = 0.0;
        for (int k = 0; k < a.getCols(); k++) {
          sum += a.getElement(i, k) * b.getElement(k, j);
        }
        result.setElement(i, j, sum);
      }
    }
    return result;
  }

  // Cek Matriks Persegi
  public static boolean isSquare(Matrix A) {
    if (A == null ||  A.rows == 0 || A.rows != A.cols) {
        return false;
    }   
    else return true;
  }

  // Mengecek nol (Untuk kemungkinan komputer tidak presisi)
  public static final double EPS = 1e-12;
  public static boolean isZero(double x) {
    return Math.abs(x) <= EPS;
  }

  // Matriks Minor
  public static Matrix minorOf (Matrix A, int row, int col) {
    int n = A.rows;
    Matrix minor = new Matrix (n-1, n-1);
    int mi = 0;
    for (int i = 0; i < n; i++) {
        if (i == row) continue;
        int mj = 0;
        for (int j = 0; j < n; j++) {
            if (j == col) continue; 
            minor.setElement(mi,mj,A.getElement(i, j));
            mj++;
        }
        mi++;
    }
    return minor;
  }

}
