package algeo.modules;

public final class Invers {

    public Invers() {}

    /**
     * Menghitung invers matriks menggunakan metode OBE (augmentasi A|I → I|A⁻¹)
     * Langkah:
     * 1. Bentuk matriks augmented (A | I)
     * 2. Lakukan transformasi baris elementer hingga sisi kiri menjadi matriks identitas
     * 3. Sisi kanan hasilnya adalah invers dari A
     */
    public static double[][] augment(double[][] A) {
        if (!Determinan.isSquare(A)) {
            throw new IllegalArgumentException("Matriks harus persegi");
        }

        int n = A.length;

        // Bentuk matriks identitas I
        double[][] I = new double[n][n];
        for (int i = 0; i < n; i++) {
            I[i][i] = 1.0;
        }

        // Bentuk matriks augmented [A | I]
        double[][] aug = new double[n][2 * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                aug[i][j] = A[i][j];           // sisi kiri A
                aug[i][j + n] = I[i][j];       // sisi kanan I
            }
        }

        // OBE untuk mengubah (A|I) → (I|A^-1)
        for (int i = 0; i < n; i++) {
            // Pastikan pivot tidak nol
            double pivot = aug[i][i];
            if (Determinan.isZero(pivot)) {
                // Tukar dengan baris di bawah yang pivot-nya tidak nol
                int swapRow = i + 1;
                while (swapRow < n && Determinan.isZero(aug[swapRow][i])) {
                    swapRow++;
                }
                if (swapRow == n) {
                    throw new ArithmeticException("Matriks tidak memiliki invers (pivot nol)");
                }
                double[] temp = aug[i];
                aug[i] = aug[swapRow];
                aug[swapRow] = temp;
                pivot = aug[i][i];
            }

            // Skala baris i agar pivot menjadi 1 (1/pivot * Bi)
            for (int j = 0; j < 2 * n; j++) {
                aug[i][j] /= pivot;
            }

            // Eliminasi kolom lain (buat nol di atas dan bawah pivot)
            for (int k = 0; k < n; k++) {
                if (k == i) continue;
                double factor = aug[k][i];
                for (int j = 0; j < 2 * n; j++) {
                    aug[k][j] -= factor * aug[i][j];
                }
            }
        }

        // Ambil sisi kanan hasil (A^-1)
        double[][] invers = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                invers[i][j] = aug[i][j + n];
            }
        }

        return invers;
    }

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

    // Fungsi bantu untuk menampilkan matriks ke layar
    public static void printMatrix(double[][] M) {
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                System.out.printf("%10.4f ", M[i][j]);
            }
            System.out.println();
        }
    }
}
