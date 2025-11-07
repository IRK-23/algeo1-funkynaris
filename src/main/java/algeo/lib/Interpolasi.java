package algeo.lib;

/**
 * Kelas Interpolasi menyediakan dua metode utama:
 * 1. Interpolasi Polinomial
 * 2. Interpolasi Splina Bézier Kubik
 *
 * Semua operasi matriks dilakukan menggunakan kelas Matrix.
 */
public class Interpolasi {

    private Interpolasi() {} // Mencegah instansiasi

    // ==========================================================
    //   INTERPOLASI POLINOMIAL
    // ==========================================================

    /**
     * Melakukan interpolasi polinomial berdasarkan titik (x_i, y_i)
     * dan mengembalikan koefisien polinomial Pn(x) = a0 + a1x + a2x^2 + ... + anx^n
     *
     * @param x array nilai x
     * @param y array nilai y
     * @return Matriks kolom berisi koefisien polinomial
     */
    public static Matrix interpolasiPolinomial(double[] x, double[] y) {
        int n = x.length;
        if (n != y.length) {
            throw new IllegalArgumentException("Panjang array x dan y harus sama");
        }

        System.out.println("=== Langkah-langkah Interpolasi Polinomial ===");

        // 1. Bentuk Matriks Vandermonde
        System.out.println("1. Membentuk matriks Vandermonde berdasarkan titik x:");
        Matrix A = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            double xi = x[i];
            for (int j = 0; j < n; j++) {
                A.setElement(i, j, Math.pow(xi, j));
            }
        }
        A.display();

        // 2. Bentuk matriks Y (kolom vektor)
        System.out.println("2. Membentuk matriks Y berdasarkan titik y:");
        Matrix Y = new Matrix(n, 1);
        for (int i = 0; i < n; i++) {
            Y.setElement(i, 0, y[i]);
        }
        Y.display();

        // 3. Hitung invers dari A
        System.out.println("3. Menghitung invers dari matriks Vandermonde...");
        Matrix A_inv = Invers.inversOBE(A);
        if (A_inv == null) {
            System.out.println("Matriks tidak dapat diinvers (kasus parametrik atau singular). Interpolasi gagal.");
            return null;
        }

        // 4. Kalikan A_inv * Y untuk mendapatkan koefisien
        System.out.println("4. Mengalikan A⁻¹ dengan Y untuk mendapatkan koefisien polinomial:");
        Matrix coeff = Matrix.multiply(A_inv, Y);
        coeff.display();

        System.out.println("=== Selesai ===\n");
        return coeff;
    }

    /**
     * Mengevaluasi hasil interpolasi polinomial pada titik tertentu
     */
    public static double evaluatePolynomial(Matrix coeff, double xEval) {
        double result = 0.0;
        int n = coeff.getRows();
        for (int i = 0; i < n; i++) {
            result += coeff.getElement(i, 0) * Math.pow(xEval, i);
        }
        return result;
    }

    /**
     * Menampilkan polinomial dalam bentuk persamaan Pn(x)
     */
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
     * @param S Matriks Nx2 berisi titik (x, y)
     * @return Matriks (n+1)x2 berisi titik kontrol Bézier
     */
    public static Matrix interpolasiBezierKubik(Matrix S) {
        int n = S.getRows() - 1; // jumlah segmen
        if (n < 2) {
            throw new IllegalArgumentException("Minimal diperlukan 3 titik untuk splina Bézier kubik");
        }

        System.out.println("=== Langkah-langkah Interpolasi Bézier Kubik ===");

        int m = n - 1; // jumlah titik kontrol tengah

        // 1. Bentuk matriks koefisien A
        System.out.println("1. Membentuk matriks koefisien A:");
        Matrix A = new Matrix(m, m);
        for (int i = 0; i < m; i++) {
            if (i > 0) A.setElement(i, i - 1, 1);
            A.setElement(i, i, 4);
            if (i < m - 1) A.setElement(i, i + 1, 1);
        }
        A.display();

        // 2. Bentuk matriks SX dan SY
        System.out.println("2. Membentuk matriks SX dan SY:");
        Matrix SX = new Matrix(m, 1);
        Matrix SY = new Matrix(m, 1);

        for (int i = 0; i < m; i++) {
            double sx, sy;
            if (i == 0) {
                sx = 6 * S.getElement(1, 0) - S.getElement(0, 0);
                sy = 6 * S.getElement(1, 1) - S.getElement(0, 1);
            } else if (i == m - 1) {
                sx = 6 * S.getElement(n - 1, 0) - S.getElement(n, 0);
                sy = 6 * S.getElement(n - 1, 1) - S.getElement(n, 1);
            } else {
                sx = 6 * S.getElement(i + 1, 0);
                sy = 6 * S.getElement(i + 1, 1);
            }
            SX.setElement(i, 0, sx);
            SY.setElement(i, 0, sy);
        }

        System.out.println("SX:");
        SX.display();
        System.out.println("SY:");
        SY.display();

        // 3. Hitung invers A
        System.out.println("3. Menghitung invers dari matriks A...");
        Matrix A_inv = Invers.inversOBE(A);
        if (A_inv == null) {
            System.out.println("Matriks tidak dapat diinvers (kasus parametrik atau singular). Interpolasi gagal.");
            return null;
        }

        // 4. Hitung titik kontrol BX dan BY
        System.out.println("4. Menghitung titik kontrol BX dan BY:");
        Matrix BX = Matrix.multiply(A_inv, SX);
        Matrix BY = Matrix.multiply(A_inv, SY);
        System.out.println("BX:");
        BX.display();
        System.out.println("BY:");
        BY.display();

        // 5. Gabungkan hasil ke dalam matriks B penuh
        System.out.println("5. Menggabungkan hasil menjadi titik kontrol Bézier penuh:");
        Matrix B = new Matrix(n + 1, 2);
        B.setElement(0, 0, S.getElement(0, 0));
        B.setElement(0, 1, S.getElement(0, 1));
        for (int i = 1; i < n; i++) {
            B.setElement(i, 0, BX.getElement(i - 1, 0));
            B.setElement(i, 1, BY.getElement(i - 1, 0));
        }
        B.setElement(n, 0, S.getElement(n, 0));
        B.setElement(n, 1, S.getElement(n, 1));

        B.display();

        System.out.println("=== Selesai ===\n");
        return B;
    }

    /**
     * Menampilkan titik-titik kontrol Bézier Kubik
     */
    public static void printBezierPoints(Matrix B) {
        System.out.println("Titik kontrol Bézier Kubik:");
        for (int i = 0; i < B.getRows(); i++) {
            System.out.printf("B%d = (%.4f, %.4f)\n", i, B.getElement(i, 0), B.getElement(i, 1));
        }
    }
}
