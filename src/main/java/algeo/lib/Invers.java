package algeo.lib;

public final class Invers {

  private Invers() {} // Konstruktor privat agar tidak bisa diinstansiasi

  // ==============================
  // Metode Invers dengan Adjoin
  // ==============================
  public static Matrix inversAdjoin(Matrix A) {
    int n = A.getRows();
    if (n != A.getCols()) {
      throw new IllegalArgumentException("Matriks harus persegi untuk dapat dihitung inversnya.");
    }

    double det = Determinan.detKofaktor(A);
    if (Matrix.isZero(det)) {
      throw new ArithmeticException("Matriks tidak memiliki invers karena determinannya 0.");
    }

    // Matriks kofaktor
    Matrix kofaktor = new Matrix(n, n);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        Matrix minor = Matrix.minorOf(A, i, j);
        double minorVal = Determinan.detKofaktor(minor);
        double cofactor = ((i + j) % 2 == 0 ? 1 : -1) * minorVal;
        kofaktor.setElement(i, j, cofactor);
      }
    }

    // Transpose kofaktor (Adjoint)
    Matrix adjoint = kofaktor.transpose();

    // Hasil invers = 1/det(A) * adj(A)
    Matrix inverse = new Matrix(n, n);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        inverse.setElement(i, j, adjoint.getElement(i, j) / det);
      }
    }

    return inverse;
  }

  // ======================================
  // Metode Invers dengan OBE (Gauss–Jordan)
  // ======================================
  public static Matrix inversOBE(Matrix A) {
    int n = A.getRows();

    if (A.getRows() != A.getCols()) {
      throw new IllegalArgumentException("Matriks harus persegi untuk dapat diinvers.");
    }

    // Buat matriks augmentasi [A | I]
    Matrix augmented = new Matrix(n, 2 * n);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        augmented.setElement(i, j, A.getElement(i, j));
      }
      for (int j = n; j < 2 * n; j++) {
        augmented.setElement(i, j, (i == (j - n)) ? 1.0 : 0.0);
      }
    }

    // Proses eliminasi Gauss–Jordan
    for (int i = 0; i < n; i++) {
      // Jika pivot 0, tukar dengan baris lain
      if (Matrix.isZero(augmented.getElement(i, i))) {
        int swapRow = i + 1;
        while (swapRow < n && Matrix.isZero(augmented.getElement(swapRow, i))) {
          swapRow++;
        }
        if (swapRow == n) {
          throw new ArithmeticException("Matriks tidak memiliki invers (determinannya 0).");
        }
        augmented.swapRows(i, swapRow);
      }

      // Normalisasi pivot jadi 1
      double pivot = augmented.getElement(i, i);
      if (!Matrix.isZero(pivot)) {
        augmented.multiplyRow(i, 1.0 / pivot);
      }

      // Hilangkan elemen di kolom pivot selain pivot itu sendiri
      for (int k = 0; k < n; k++) {
        if (k != i) {
          double factor = augmented.getElement(k, i);
          if (!Matrix.isZero(factor)) {
            augmented.addMultiplyOfRow(k, i, -factor);
          }
        }
      }
    }

    // Ambil hasil invers dari sisi kanan [A | I] → [I | A^-1]
    Matrix inverse = new Matrix(n, n);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        inverse.setElement(i, j, augmented.getElement(i, j + n));
      }
    }

    return inverse;
  }
}
