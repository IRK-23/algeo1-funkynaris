package algeo.lib;

/**
 * Kelas Interpolasi menyediakan dua metode utama:
 * 1. Interpolasi Polinomial
 * 2. Interpolasi Splina Bézier Kubik
 */
public class Interpolasi {

    private Interpolasi() {} // prevent instantiation

    // ==========================================================
    //   INTERPOLASI POLINOMIAL
    // ==========================================================

    /**
     * Melakukan interpolasi polinomial berdasarkan titik (x_i, y_i)
     * dan mengembalikan koefisien polinomial Pn(x) = a0 + a1x + a2x^2 + ... + anx^n
     *
     */
    public static Matrix interpolasiPolinomial(double[] x, double[] y) {
        int n = x.length;
        if (n != y.length) {
            throw new IllegalArgumentException("Panjang array x dan y harus sama");
        }

        // Matriks Vandermonde
        Matrix A = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            double xi = x[i];
            for (int j = 0; j < n; j++) {
                A.setElement(i, j, Math.pow(xi, j));
            }
        }

        // Matriks Y (kolom vektor)
        Matrix Y = new Matrix(n, 1);
        for (int i = 0; i < n; i++) {
            Y.setElement(i, 0, y[i]);
        }

        // Koefisien = Invers(A) * Y
        Matrix A_inv = Invers.inversOBE(A);
        if (A_inv == null) {
            System.out.println("Matriks tidak dapat diinvers, interpolasi gagal.");
            return null;
        }

        return Matrix.multiply(A_inv, Y);
    }

    /** Mengevaluasi hasil interpolasi polinomial pada titik xEval */
    public static double evaluatePolynomial(Matrix coeff, double xEval) {
        double result = 0.0;
        int n = coeff.getRows();
        for (int i = 0; i < n; i++) {
            result += coeff.getElement(i, 0) * Math.pow(xEval, i);
        }
        return result;
    }

    /** Menampilkan polinomial dalam bentuk persamaan Pn(x) */
    public static void printPolynomial(Matrix coeff) {
        System.out.print("Pn(x) = ");
        for (int i = 0; i < coeff.getRows(); i++) {
            double a = coeff.getElement(i, 0);
            if (i == 0) System.out.printf("%.4f", a);
            else System.out.printf(" + (%.4f)x^%d", a, i);
        }
        System.out.println();
    }

    // ==========================================================
    //   INTERPOLASI SPLINA BÉZIER KUBIK
    // ==========================================================

    /**
     * Menghitung titik kontrol B1...Bn-1 untuk kurva Bézier kubik
     * berdasarkan titik interpolasi S0...Sn (untuk masing-masing sumbu x dan y).
     *
     */
    public static double[][] interpolasiBezierKubik(double[][] S) {
        int n = S.length - 1; // jumlah segmen
        if (n < 2) {
            throw new IllegalArgumentException("Minimal diperlukan 3 titik untuk splina Bézier kubik");
        }

        int m = n - 1; // jumlah titik kontrol yang akan dicari: b1..bn-1

        // Matriks A (koefisien)
        Matrix A = new Matrix(m, m);
        for (int i = 0; i < m; i++) {
            if (i > 0) A.setElement(i, i - 1, 1); // elemen bawah diagonal
            A.setElement(i, i, 4);                // diagonal utama
            if (i < m - 1) A.setElement(i, i + 1, 1); // elemen atas diagonal
        }

        // Matriks SX dan SY (hasil sisi kanan)
        Matrix SX = new Matrix(m, 1);
        Matrix SY = new Matrix(m, 1);

        // Mengisi matriks hasil berdasarkan persamaan dari soal
        for (int i = 0; i < m; i++) {
            double sx, sy;
            if (i == 0) {
                sx = 6 * S[1][0] - S[0][0];
                sy = 6 * S[1][1] - S[0][1];
            } else if (i == m - 1) {
                sx = 6 * S[n - 1][0] - S[n][0];
                sy = 6 * S[n - 1][1] - S[n][1];
            } else {
                sx = 6 * S[i + 1][0];
                sy = 6 * S[i + 1][1];
            }
            SX.setElement(i, 0, sx);
            SY.setElement(i, 0, sy);
        }

        // Selesaikan A * BX = SX dan A * BY = SY
        Matrix A_inv = Invers.inversOBE(A);
        if (A_inv == null) {
            System.out.println("Matriks tidak dapat diinvers, interpolasi Bézier gagal.");
            return null;
        }

        Matrix BX = Matrix.multiply(A_inv, SX);
        Matrix BY = Matrix.multiply(A_inv, SY);

        // Gabungkan hasil menjadi array titik kontrol penuh B0..Bn
        double[][] B = new double[n + 1][2];
        B[0] = S[0]; // titik awal = S0
        for (int i = 1; i < n; i++) {
            B[i][0] = BX.getElement(i - 1, 0);
            B[i][1] = BY.getElement(i - 1, 0);
        }
        B[n] = S[n]; // titik akhir = Sn

        return B;
    }

    /** Menampilkan titik-titik kontrol Bézier Kubik */
    public static void printBezierPoints(double[][] B) {
        System.out.println("Titik kontrol Bézier Kubik:");
        for (int i = 0; i < B.length; i++) {
            System.out.printf("B%d = (%.4f, %.4f)\n", i, B[i][0], B[i][1]);
        }
    }
}
