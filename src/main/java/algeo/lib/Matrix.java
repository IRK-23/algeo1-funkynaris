package algeo.lib;

import java.io.*;
import java.util.*;

/**
 * Kelas Matrix menyediakan operasi dasar matriks
 * seperti OBE, perkalian, transpose, dan utilitas tambahan.
 * Kelas ini digunakan di seluruh fitur (SPL, Determinan, Invers, Interpolasi, dan Regresi).
 */
public class Matrix {
  private double[][] data;
  private int rows;
  private int cols;

  // ==========================================================
  // KONSTRUKTOR
  // ==========================================================

  public Matrix(double[][] initialData) {
    if (initialData == null || initialData.length == 0 || initialData[0].length == 0) {
      throw new IllegalArgumentException("Data matriks tidak boleh kosong");
    }
    this.rows = initialData.length;
    this.cols = initialData[0].length;
    this.data = new double[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
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

  // ==========================================================
  // SELEKTOR
  // ==========================================================
  public int getRows() { return this.rows; }

  public int getCols() { return this.cols; }

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

  // ==========================================================
  // OPERASI BARIS ELEMENTER (OBE)
  // ==========================================================

  public void swapRows(int row1, int row2) {
    double[] temp = this.data[row1];
    this.data[row1] = this.data[row2];
    this.data[row2] = temp;
  }

  public void multiplyRow(int row, double scalar) {
    for (int j = 0; j < this.cols; j++) {
      this.data[row][j] *= scalar;
    }
  }

  public void addMultiplyOfRow(int targetRow, int sourceRow, double scalar) {
    for (int j = 0; j < this.cols; j++) {
      this.data[targetRow][j] += scalar * this.data[sourceRow][j];
    }
  }

  // ==========================================================
  // OPERASI DASAR MATRIX
  // ==========================================================

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

  // Mengubah menjadi bentuk eselon baris (tanpa leading one)
  public Matrix toEselonMatrix() {
    Matrix m = this.copy();
    int rows = m.getRows();

    for (int j = 0; j < rows; j++) {
      int pivot = j;
      for (int i = j + 1; i < rows; i++) {
        if (Math.abs(m.getElement(i, j)) > Math.abs(m.getElement(pivot, j))) {
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

  public Matrix toEselonMatrixWithSteps() {
    Matrix m = this.copy();
    int rows = m.getRows();
    int cols = m.getCols();

    System.out.println("\n===== Memulai Forward Elimination (Gauss) =====");
    System.out.println("Matriks Awal:");
    m.display();
    System.out.println("------------------------------------");

    int pivotRow = 0;
    for (int j = 0; j < cols - 1 && pivotRow < rows; j++) {
        // Cari baris dengan pivot (elemen non-nol)
        int i = pivotRow;
        while (i < rows && Matrix.isZero(m.getElement(i, j))) {
            i++;
        }

        if (i < rows) {
            if (pivotRow != i) {
                System.out.printf("\nLangkah: Menukar baris R%d dengan R%d untuk mendapatkan pivot.\n", pivotRow + 1, i + 1);
                m.swapRows(pivotRow, i);
                m.display();
            }

            // Buat nol di semua baris di bawah pivot
            for (int k = pivotRow + 1; k < rows; k++) {
                double factor = m.getElement(k, j) / m.getElement(pivotRow, j);
                if (!Matrix.isZero(factor)) {
                    System.out.printf("\nLangkah: Eliminasi di R%d, K%d -> R%d = R%d - (%.3f) * R%d\n", k + 1, j + 1, k + 1, k + 1, factor, pivotRow + 1);
                    m.addMultiplyOfRow(k, pivotRow, -factor);
                    m.display();
                }
            }
            pivotRow++;
        }
    }
    System.out.println("\n===== Forward Elimination Selesai, Matriks Eselon Baris Terbentuk =====");
    m.display();
    return m;
  }

  // Perkalian dua matriks
  public static Matrix multiply(Matrix a, Matrix b) {
    if (a.getCols() != b.getRows()) {
      System.out.println("Ukuran matriks tidak valid untuk perkalian");
      return null;
    }

    Matrix result = new Matrix(a.getRows(), b.getCols());
    for (int i = 0; i < a.getRows(); i++) {
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

  // ==========================================================
  // OPERASI MATRIKS TAMBAHAN
  // ==========================================================

  public static boolean isSquare(Matrix A) {
    return A != null && A.rows == A.cols;
  }

  public static final double EPS = 1e-12;

  public static boolean isZero(double x) {
    return Math.abs(x) <= EPS;
  }

  // Menghasilkan matriks minor
  public static Matrix minorOf(Matrix A, int row, int col) {
    int n = A.rows;
    Matrix minor = new Matrix(n - 1, n - 1);
    int mi = 0;
    for (int i = 0; i < n; i++) {
      if (i == row) continue;
      int mj = 0;
      for (int j = 0; j < n; j++) {
        if (j == col) continue;
        minor.setElement(mi, mj, A.getElement(i, j));
        mj++;
      }
      mi++;
    }
    return minor;
  }

  // Mengembalikan transpose dari matriks ini
  public Matrix transpose() {
    Matrix result = new Matrix(this.cols, this.rows);
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.cols; j++) {
        result.setElement(j, i, this.data[i][j]);
      }
    }
    return result;
  }

  // ==========================================================
  // FUNGSI TAMBAHAN UNTUK MAIN
  // ==========================================================

  // Mengambil submatriks dari baris/kolom tertentu (end tidak inklusif)
  public Matrix subMatrix(int startRow, int startCol, int endRow, int endCol) {
    int newRows = endRow - startRow;
    int newCols = endCol - startCol;
    Matrix result = new Matrix(newRows, newCols);
    for (int i = 0; i < newRows; i++) {
      for (int j = 0; j < newCols; j++) {
        result.setElement(i, j, this.data[startRow + i][startCol + j]);
      }
    }
    return result;
  }

  // Mengambil satu kolom sebagai vektor kolom
  public Matrix getColVector(int colIndex) {
    if (colIndex < 0 || colIndex >= this.cols) {
      throw new IndexOutOfBoundsException("Indeks kolom tidak valid");
    }
    Matrix result = new Matrix(this.rows, 1);
    for (int i = 0; i < this.rows; i++) {
      result.setElement(i, 0, this.data[i][colIndex]);
    }
    return result;
  }

  // Membaca matriks dari file teks
  // Format: setiap baris = elemen dipisahkan spasi
  public static Matrix fromFile(String path) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line;
    List<double[]> rows = new ArrayList<>();

    while ((line = reader.readLine()) != null) {
      line = line.trim();
      if (line.isEmpty()) continue;
      String[] tokens = line.split("\\s+");
      double[] row = new double[tokens.length];
      for (int i = 0; i < tokens.length; i++) {
        row[i] = Double.parseDouble(tokens[i]);
      }
      rows.add(row);
    }
    reader.close();

    if (rows.isEmpty()) throw new IOException("File kosong atau format salah");

    int m = rows.size();
    int n = rows.get(0).length;
    double[][] data = new double[m][n];
    for (int i = 0; i < m; i++) {
      if (rows.get(i).length != n)
        throw new IOException("Baris memiliki panjang berbeda");
      data[i] = rows.get(i);
    }
    return new Matrix(data);
  }

  // Override toString untuk tampilan matriks rapi
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.rows; i++) {
      sb.append("| ");
      for (int j = 0; j < this.cols; j++) {
        sb.append(String.format("%8.3f ", this.data[i][j]));
      }
      sb.append("|\n");
    }
    return sb.toString();
  }
}
