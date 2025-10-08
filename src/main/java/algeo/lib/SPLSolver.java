package algeo.lib;

public class SPLSolver {

  public Matrix solveWithGaussJordan(Matrix matrix) {
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

  public double[] solveWithGauss(Matrix matrix) {
    Matrix m = matrix.copy();
    int rows = m.getRows();
    int cols = m.getCols();

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

  public double[] solveWithCramer(Matrix a, Matrix b) {
    double detA = determinantByRowReduction(a);
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
      solution[j] = determinantByRowReduction(a_i) / detA;
    }
    return solution;
  }

  public Matrix solveWithInverse(Matrix a, Matrix b) {
    Matrix a_inv = inverseByGaussJordan(a);
    if (a_inv == null) {
      System.out.println("Matrix A tidak memiliki invers, metode ini tidak dapat digunakan")
    }
    return multiply(a_inv, b);
  }

  private Matrix multiply(Matrix a, Matrix b) {
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

  private double determinantByRowReduction(Matrix matrix) {
    if (matrix.getRows() != matrix.getCols()) {
      throw new IllegalArgumentException("Matriks harus persegi untuk menghitung determinan");
    }

    Matrix m = matrix.copy();
    int n = m.getRows();
    double det = 1.0;
    final double EPSILON = 1e-9;

    for (int j=0; j<n; j++) {
      int pivotRow = j;
      for (int i = j+1; i<n; i++) {
        if (Math.abs(m.getElement(i,j)) > Math.abs(m.getElement(pivotRow, j))) {
          pivotRow = i;
        }
      }

      if (pivotRow != j) {
        m.swapRows(j, pivotRow);
        det *= -1;
      }

      double pivotValue = m.getElement(j, j);

      if (Math.abs(pivotValue) < EPSILON) {
        return 0.0;
      }

      det *= pivotValue;
      for (int i = j + 1; i < n; i++) {
        double factor = m.getElement(i, j) / pivotValue;
        m.addMultiplyOfRow(i, j, -factor);
      }
    }
    return det;
  }

  private Matrix inverseByGaussJordan(Matrix matrix) {
    if (matrix.getRows() != matrix.getCols()) {
      System.out.println("Matriks harus persegi untuk diinvers");
      return null;
    }
    if (Math.abs(determinantByRowReduction(matrix)) < 1e-9) {
      System.out.println("Determinan matriks nol, tidak memiliiki invers");
      return null;
    }

    int n = matrix.getRows();
    Matrix augmented = new Matrix(n, 2 * n);

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        augmented.setElement(i, j, matrix.getElement(i, j));
        if ( i == j) {
          augmented.setElement(i, j+n, 1);
        }
      }
    }

    Matrix rref = solveWithGaussJordan(augmented);

    Matrix inverse = new Matrix(n,n);
    for (int i=0; i<n; i++) {
      for (int j=0; j<n; j++) {
        inverse.setElement(i, j, rref.getElement(i, j+n));
      }
    }
    return inverse;
  }
}
