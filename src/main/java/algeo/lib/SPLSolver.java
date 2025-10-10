package algeo.lib;

public class SPLSolver {

  // Metode Eliminasi Gauss-Jordan
  public static Matrix solveWithGaussJordan(Matrix matrix) {
    Matrix m = matrix.copy();
    int rows = m.getRows();
    int cols = m.getCols();
    int pivotRow = 0;

    System.out.println("\n==== Memulai Eliminasi Gauss Jordan =====");
    System.out.println("Matriks Awal:");
    m.display();
    System.out.println("------------------------------------");


    for (int j = 0; j < cols - 1 && pivotRow< rows; j++) {
      int i = pivotRow;
      while (i < rows && m.getElement(i, j) == 0) {
        i++;
      }

      if (i < rows) {
        if (pivotRow != i) {
          System.out.printf("\nLangkah: Menukar baris R%d dengan R%d untuk mendapatkan pivot.\n", pivotRow + 1, i + 1);
          m.swapRows(pivotRow, i);
          m.display();
        }
        
        double pivotValue = m.getElement(pivotRow, j);
        if (Math.abs(pivotValue - 1.0) > Matrix.EPS) {
          System.out.printf("\nNormalisasi baris R%d -> R%d = R%d / %.3f\n", pivotRow + 1, pivotRow + 1, pivotRow + 1, pivotValue);
          m.multiplyRow(pivotRow, 1.0 / pivotValue);
          m.display();
        }

        for (int k = 0; k < rows; k++) {
          if (k != pivotRow) {
            double factor = m.getElement(k, j);
            if (!Matrix.isZero(factor)) {
              System.out.printf("\nLangkah: Eliminasi di R%d, K%d -> R%d = R%d - (%.3f) * R%d\n", k + 1, j + 1, k + 1, k + 1, factor, pivotRow + 1);
              m.addMultiplyOfRow(k, pivotRow, -factor);
              m.display();
            }

          }
        }
        pivotRow++;

      }
    }
    System.out.println("\n===== Proses Eliminasi Selssai =====");
    return m;

  }

  // Metode Eliminasi Gauss
  public static double[] solveWithGauss(Matrix matrix) {
    int rows = matrix.getRows();
    int cols = matrix.getCols();

    // Mengubah ke matriks eselon baris (tidak leading one)
    Matrix m = matrix.toEselonMatrixWithSteps();

    System.out.println("\n===== Memulai Back Substitution =====");
    double[] solution = new double[rows];
    for (int i = rows - 1; i >= 0; i--) {
      double sum = 0.0;
      System.out.printf("Menghitung x%d:\n", i + 1);
      System.out.printf("  Persamaan dari baris %d: ", i + 1);

      // Membangun string persamaan untuk ditampilkan
      StringBuilder equation = new StringBuilder();
      for(int k=i; k<rows; k++){
          equation.append(String.format("(%.2fx%d) + ", m.getElement(i,k), k+1));
      }
      equation.delete(equation.length()-2, equation.length()); // Hapus "+ " terakhir
      equation.append(String.format("= %.2f", m.getElement(i, cols-1)));
      System.out.println(equation.toString());

      // Menghitung jumlah dari variabel yang sudah diketahui
      for (int j = i + 1; j< rows; j++) {
        sum += m.getElement(i, j) * solution[j];
      }
      double constant = m.getElement(i, cols - 1);
      double pivot = m.getElement(i, i);
      solution[i] = (constant - sum) / pivot;
      System.out.printf("  x%d = (%.3f - %.3f) / %.3f = %.3f\n", i + 1, constant, sum, pivot, solution[i]);
    }
    System.out.println("===== Back Substitution Selesai =====");
    return solution;
  }

  // Metode Kaidah Cramer
  public double[] solveWithCramer(Matrix a, Matrix b) {
    System.out.println("\n===== Memulai Penyelesaian dengan Kaidah Cramer =====");
    System.out.println("Langkah 1: Menghitung determinan matriks koefisien A.");
    double detA = Determinan.detReduksiBaris(a);
    if (detA == 0) {
      System.out.println("Determinan matriks A adalah 0, kaidah Cramer tidak dapat digunakan");
      return null;
    }
    System.out.printf("\nHasil det(A) = %.4f\n", detA);

    double[] solution = new double[a.getRows()];
    for (int j = 0; j < a.getCols(); j++) {
      System.out.printf("\nLangkah %d: Menghitung x%d\n", j + 2, j + 1);
      Matrix a_i = a.copy();
      for (int i = 0; i < a.getRows(); i++) {
        a_i.setElement(i, j, b.getElement(i,0));
      }
      System.out.printf("Membentuk matriks A%d:\n", j + 1);
      a_i.display();
      
      System.out.printf("Menghitung det(A%d):\n", j + 1);
      double detAi = Determinan.detReduksiBarisWithSteps(a_i);
      System.out.printf("\nHasil det(A%d) = %.4f\n", j + 1, detAi);
      
      solution[j] = Determinan.detReduksiBaris(a_i) / detA;
      System.out.printf("x%d = det(A%d) / det(A) = %.4f / %.4f = %.4f\n", j + 1, j + 1, detAi, detA, solution[j]);
    }
    System.out.println("=====================================================");
    return solution;
  }


  // Metode Invers Matriks
  public Matrix solveWithInverse(Matrix a, Matrix b) {
    System.out.println("\n===== Memulai Penyelesaian dengan Matriks Balikan =====");
    System.out.println("Langkah 1: Mencari matriks balikan (invers) dari A.");
    Matrix a_inv = Invers.inversOBE(a);
    if (a_inv == null) {
      System.out.println("Matrix A tidak memiliki invers, metode ini tidak dapat digunakan");
      return null;
    }
    System.out.println("\nLangkah 2: Mengalikan matriks invers A⁻¹ dengan matriks B.");
    System.out.println("X = A⁻¹ * B");
    
    Matrix result = Matrix.multiply(a_inv, b);
    System.out.println("=======================================================");
    return result;
  }

  //Metode untuk mendeteksi solusi unik, banyak, atau tidak ada solusi
  public static void displaySolution(Matrix rrefMatrix) {
    int rows = rrefMatrix.getRows();
    int varCount = rrefMatrix.getCols()-1;

    //Mengecek kasus tidak ada solusi
    for (int i = 0; i < rows; i++) {
      boolean isAllZeroInVar = true;
      for (int j = 0; j < varCount; j++) {
        if (!Matrix.isZero(rrefMatrix.getElement(i, j))) {
          isAllZeroInVar = false;
          break;
        }
      }

      // Jika semua di kolom variabel nol, tapi di kolom konstanta tidak nol
      if (isAllZeroInVar && !Matrix.isZero(rrefMatrix.getElement(i, varCount))) {
        System.out.println("Sistem persamaan linear tidak memiliki solusi");
        return;
      }
    }

    boolean[] isPivotColumn = new boolean[varCount];
    int[] pivotRowForCol = new int[varCount];
    for(int i=0; i<varCount; i++) pivotRowForCol[i] = -1;

    // Mengidentifikasi kolom pivot
    int pivotCount = 0;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < varCount; j++) {
        // Mengecek apakah elemen ini adalah pivot
        if (Math.abs(rrefMatrix.getElement(i, j) - 1.0) < Matrix.EPS) {
          isPivotColumn[j] = true;
          pivotRowForCol[j] = i;
          pivotCount++;
          break;
        }
      }
    }

    // Menentukan solusi unik atau banyak solusi
    if (pivotCount == varCount) {
      // Solusi Unik
      System.out.println("Solusi Unik:");
      for (int j = 0; j < varCount; j++) {
        System.out.printf("x%d = %.3f\n", (j+1), rrefMatrix.getElement(pivotRowForCol[j], varCount));
      }
    } else {
      // Banyak Solusi
      System.out.println("Banyak solusi");

      // Array untuk menyimpan representasi string dari setiap variabel
      String[] parameterName = {"t", "s", "r", "p", "q", "a", "b", "c"};
      int paramIdx = 0;

      String[] solutions = new String[varCount];

      // Membuat parameter untuk variabel bebas
      for (int j=0; j<varCount; j++) {
        if (!isPivotColumn[j]) {
          solutions[j] = parameterName[paramIdx++];
        }
      }

      //Membuat persamaan untuk variabel terikat
      for (int j = 0; j < varCount; j++) {
        if (isPivotColumn[j]) {
          int pivotRow = pivotRowForCol[j];
          StringBuilder sb = new StringBuilder();
          double constant = rrefMatrix.getElement(pivotRow, varCount);
          if (!Matrix.isZero(constant)) {
            sb.append(String.format("%.3f", constant));
          }

          for (int k = j + 1; k < varCount; k++) {
            if (!isPivotColumn[k]) {
              double coefficient = rrefMatrix.getElement(pivotRow, k);
              if (!Matrix.isZero(coefficient)) {
                if(sb.length() > 0) {
                  sb.append(((-coefficient > 0) ? "+" : "-"));
                } else if (-coefficient < 0) {
                  sb.append("-");
                }
                sb.append(String.format("%.3f%s", Math.abs(coefficient), solutions[k]));
              }   
            }
          }
          if (sb.length() == 0) {
            sb.append("0.000");
          }
          solutions[j] = sb.toString();
        }
      }

      for (int i = 0; i < varCount; i++) {
        System.out.printf("x%d = %s\n", (i+1), solutions[i]);
      }
    }
  }

  public static String getSolutionAsString(Matrix rrefMatrix) {
    if (rrefMatrix == null) {
        return "Proses eliminasi gagal menghasilkan matriks.";
    }
    int rows = rrefMatrix.getRows();
    int varCount = rrefMatrix.getCols() - 1;
    StringBuilder result = new StringBuilder();

    // Mengecek kasus tidak ada solusi
    for (int i = 0; i < rows; i++) {
        boolean isAllZeroInVar = true;
        for (int j = 0; j < varCount; j++) {
            if (!Matrix.isZero(rrefMatrix.getElement(i, j))) {
                isAllZeroInVar = false;
                break;
            }
        }
        if (isAllZeroInVar && !Matrix.isZero(rrefMatrix.getElement(i, varCount))) {
            return "Sistem persamaan linear tidak memiliki solusi.\n";
        }
    }

    // Mengidentifikasi kolom pivot
    boolean[] isPivotColumn = new boolean[varCount];
    int[] pivotRowForCol = new int[varCount];
    java.util.Arrays.fill(pivotRowForCol, -1);
    int pivotCount = 0;
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < varCount; j++) {
            // Cek apakah ini leading one
            if (Math.abs(rrefMatrix.getElement(i, j) - 1.0) < Matrix.EPS) {
                isPivotColumn[j] = true;
                pivotRowForCol[j] = i;
                pivotCount++;
                break; // Pindah ke baris berikutnya setelah menemukan pivot
            }
            // Jika menemukan elemen non-nol sebelum leading one, ini bukan RREF sejati
            else if (!Matrix.isZero(rrefMatrix.getElement(i, j))) {
                break;
            }
        }
    }

    if (pivotCount == varCount) {
        result.append("Solusi Unik:\n");
        for (int j = 0; j < varCount; j++) {
            result.append(String.format("x%d = %.6f\n", (j + 1), rrefMatrix.getElement(j, varCount)));
        }
    } else {
        result.append("Banyak Solusi (Solusi Parametrik):\n");
        String[] parameterName = {"t", "s", "r", "p", "q"};
        int paramIdx = 0;
        String[] solutions = new String[varCount];

        for (int j = 0; j < varCount; j++) {
            if (!isPivotColumn[j]) {
                solutions[j] = parameterName[paramIdx++];
            }
        }

        for (int j = varCount - 1; j >= 0; j--) {
            if (isPivotColumn[j]) {
                int pivotRow = pivotRowForCol[j];
                StringBuilder sb = new StringBuilder();
                double constant = rrefMatrix.getElement(pivotRow, varCount);
                if (!Matrix.isZero(constant)) {
                    sb.append(String.format("%.3f", constant));
                }

                for (int k = j + 1; k < varCount; k++) {
                    if (!isPivotColumn[k]) {
                        double coefficient = rrefMatrix.getElement(pivotRow, k);
                        if (!Matrix.isZero(coefficient)) {
                            if (sb.length() > 0) {
                                sb.append((-coefficient > 0) ? " + " : " - ");
                            } else if (-coefficient < 0) {
                                sb.append("-");
                            }
                            sb.append(String.format("%.3f%s", Math.abs(coefficient), solutions[k]));
                        }
                    }
                }
                if (sb.length() == 0) sb.append("0.000");
                solutions[j] = sb.toString();
            }
        }
        for (int i = 0; i < varCount; i++) {
            result.append(String.format("x%d = %s\n", (i + 1), solutions[i]));
        }
    }
    return result.toString();
}

  
}
