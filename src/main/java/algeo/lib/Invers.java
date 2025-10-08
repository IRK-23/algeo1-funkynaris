package algeo.lib;

public final class Invers {

    public Invers() {}

    /**
     * Menghitung invers matriks menggunakan metode OBE (augmentasi A|I → I|A⁻¹)
     * Langkah:
     * 1. Bentuk matriks augmented (A | I)
     * 2. Lakukan transformasi baris elementer hingga sisi kiri menjadi matriks identitas
     * 3. Sisi kanan hasilnya adalah invers dari A
     */
    public static Matrix augment(Matrix A) {
        if (!Matrix.isSquare(A)) {
            throw new IllegalArgumentException("Matriks harus persegi");
        }

        int n = A.getRows();

        // Bentuk matriks identitas I
        Matrix I = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            I.setElement(i, i, 1.0);
        }

        // Bentuk matriks augmented [A | I]
        Matrix aug = new Matrix(n, 2 * n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                aug.setElement(i, j, A.getElement(i, j));           // sisi kiri A
                aug.setElement(i, j+n, I.getElement(i, j));         // sisi kanan I
            }
        }

        // OBE untuk mengubah (A|I) → (I|A^-1)
        for (int i = 0; i < n; i++) {
            // Pastikan pivot tidak nol
            double pivot = aug.getElement(i, i);
            if (Matrix.isZero(pivot)) {
                // Tukar dengan baris di bawah yang pivot-nya tidak nol
                int swapRow = i + 1;
                while (swapRow < n && Matrix.isZero(aug.getElement(swapRow, i))) {
                    swapRow++;
                }
                if (swapRow == n) {
                    throw new ArithmeticException("Matriks tidak memiliki invers (pivot nol)");
                }
                
                for (int a=0; a < n ; a++) {
                    double temp = aug.getElement(i, a);
                    aug.setElement(i, a, aug.getElement(swapRow, a));
                    aug.setElement(swapRow, a, temp);
                }
                pivot = aug.getElement(i, i);
            }

            // Skala baris i agar pivot menjadi 1 (1/pivot * Bi)
            for (int j = 0; j < 2 * n; j++) {
                aug.setElement(i, j, (aug.getElement(i, j) / pivot) ); 
            }

            // Eliminasi kolom lain (buat nol di atas dan bawah pivot)
            for (int k = 0; k < n; k++) {
                if (k == i) continue;
                double factor = aug.getElement(k, i);
                for (int j = 0; j < 2 * n; j++) {
                    aug.setElement(k,j, (aug.getElement(k, j) - factor * aug.getElement(i, j)) );
                }
            }
        }

        // Ambil sisi kanan hasil (A^-1)
        Matrix invers = new Matrix(n,n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                invers.setElement(i, j, aug.getElement(i, j+n));
            }
        }

        return invers;
    }

    // Fungsi untuk menghitung invers matriks menggunakan metode Adjoin
    public static Matrix inversAdjoin(Matrix A) {
        if (!Matrix.isSquare(A)) {
            throw new IllegalArgumentException("Matriks harus persegi");
        }

        int n = A.getRows();

        // 1️⃣ Hitung Determinan Matriks
        double det = Determinan.detKofaktor(A);
        if (Matrix.isZero(det)) {
            throw new ArithmeticException("Matriks tidak memiliki invers (determinannya nol)");
        }

        // 2️⃣ Hitung Matriks Kofaktor
        Matrix kofaktor = new Matrix(n,n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Matrix minor = Matrix.minorOf(A, i, j);
                double sign = ((i + j) % 2 == 0) ? 1.0 : -1.0;
                kofaktor.setElement(i, j, sign * Determinan.detKofaktor(minor));
            }
        }

        // 3️⃣ Transpos Matriks Kofaktor → Matriks Adjoin
        Matrix adjoin = new Matrix(n,n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjoin.setElement(i, j, kofaktor.getElement(j, i));
            }
        }

        // 4️⃣ Hitung Invers Matriks (1/det) × adj(A)
        Matrix invers = new Matrix(n,n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                invers.setElement(i, j, adjoin.getElement(i, j) / det);
            }
        }

        return invers;
    }

}
