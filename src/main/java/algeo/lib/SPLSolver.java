package algeo.lib;

public class SPLSolver {

  // Metode Eliminasi Gauss-Jordan
  public static Matrix solveWithGaussJordan(Matrix matrix) {
    Matrix m = matrix.copy();
    int rows = m.getRows();
    int cols = m.getCols();
    int pivotRow = 0;

    for (int j = 0; j < cols - 1 && pivotRow< rows; j++) {
      int i = pivotRow;
      while (i < rows && m.getElement(i, j) == 0) {
        i++;
      }

      if (i < rows) {
        m.swapRows(pivotRow, i);
        double pivotValue = m.getElement(pivotRow, j);
        m.multiplyRow(pivotRow, 1.0 / pivotValue);

        for (int k = 0; k < rows; k++) {
          if (k != pivotRow) {
            double factor = m.getElement(k, j);
            m.addMultiplyOfRow(k, pivotRow, -factor);
          }
        }
        pivotRow++;

      }
    }
    return m;

  }

  // Metode Eliminasi Gauss
  public static double[] solveWithGauss(Matrix matrix) {
    int rows = matrix.getRows();
    int cols = matrix.getCols();

    // Mengubah ke matriks eselon baris (tidak leading one)
    Matrix m = matrix.toEselonMatrix();

    double[] solution = new double[rows];
    for (int i = rows - 1; i >= 0; i--) {
      double sum = 0.0;
      for (int j = i + 1; j< rows; j++) {
        sum += m.getElement(i, j) * solution[j];
      }
      solution[i] = (m.getElement(i, cols - 1) - sum) / m.getElement(i, i);
    }
    return solution;
  }

  // Metode Kaidah Cramer
  public double[] solveWithCramer(Matrix a, Matrix b) {
    double detA = Determinan.detReduksiBaris(a);
    if (detA == 0) {
      System.out.println("Determinan matriks A adalah 0, kaidah Cramer tidak dapat digunakan");
      return null;
    }

    double[] solution = new double[a.getRows()];
    for (int j = 0; j < a.getCols(); j++) {
      Matrix a_i = a.copy();
      for (int i = 0; i < a.getRows(); i++) {
        a_i.setElement(i, j, b.getElement(i,0));
      }
      solution[j] = Determinan.detReduksiBaris(a_i) / detA;
    }
    return solution;
  }


  // Metode Invers Matriks
  public Matrix solveWithInverse(Matrix a, Matrix b) {
    Matrix a_inv = Invers.inversOBE(a);
    if (a_inv == null) {
      System.out.println("Matrix A tidak memiliki invers, metode ini tidak dapat digunakan");
    }
    return Matrix.multiply(a_inv, b);
  }

  
}
