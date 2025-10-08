package algeo.modules;

public final class Invers {

    private Invers() {}

    // Fungsi untuk menghitung invers matriks menggunakan metode Adjoin
    public static double[][] inversAdjoin(double[][] A) {
        if (!Determinan.isSquare(A)) {
            throw new IllegalArgumentException("Matriks harus persegi");
        }

        int n = A.length;

        // 1️⃣ Hitung Determinan Matriks
        double det = Determinan.detKofaktor(A);
        if (Determinan.isZero(det)) {
            throw new ArithmeticException("Matriks tidak memiliki invers (determinannya nol)");
        }

        // 2️⃣ Hitung Matriks Kofaktor
        double[][] kofaktor = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double[][] minor = Determinan.minorOf(A, i, j);
                double sign = ((i + j) % 2 == 0) ? 1.0 : -1.0;
                kofaktor[i][j] = sign * Determinan.detKofaktor(minor);
            }
        }

        // 3️⃣ Transpos Matriks Kofaktor → Matriks Adjoin
        double[][] adjoin = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjoin[i][j] = kofaktor[j][i];
            }
        }

        // 4️⃣ Hitung Invers Matriks (1/det) × adj(A)
        double[][] invers = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                invers[i][j] = adjoin[i][j] / det;
            }
        }

        return invers;
    }
}
