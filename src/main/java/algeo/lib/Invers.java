package algeo.lib;

/**
 * Kelas Invers menyediakan dua metode utama:
 * 1. Metode Adjoin (Kofaktor)
 * 2. Metode OBE (Gauss–Jordan)
 *
 * Dilengkapi deteksi matriks singular dan penanganan error numerik.
 */
public final class Invers {

    private Invers() {}

    // ==========================================================
    //  INVERS METODE ADJOIN
    // ==========================================================
    public static Matrix inversAdjoin(Matrix A) {
        if (!Matrix.isSquare(A)) {
            throw new IllegalArgumentException("Matriks harus persegi untuk dihitung inversnya.");
        }

        System.out.println("\n===== Metode Adjoin untuk Invers =====");
        A.display();

        double detA = Determinan.detKofaktor(A);
        System.out.printf("\nDeterminan(A) = %.10f\n", detA);

        // Gunakan toleransi untuk floating point
        if (Math.abs(detA) < 1e-10) {
            System.out.println(" Matriks singular — determinan nol, tidak memiliki invers unik.");
            return null;
        }

        int n = A.getRows();
        Matrix cofactor = new Matrix(n, n);

        System.out.println("\n Langkah-langkah pembentukan matriks kofaktor:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Matrix minor = Matrix.minorOf(A, i, j);
                double detMinor = Determinan.detKofaktor(minor);
                double sign = ((i + j) % 2 == 0) ? 1.0 : -1.0;
                double cof = sign * detMinor;
                cofactor.setElement(i, j, cof); // simpan dulu tanpa transpose
                System.out.printf("Cofaktor C[%d][%d] = %.6f (minor det = %.6f)\n", i + 1, j + 1, cof, detMinor);
            }
        }

        System.out.println("\n Matriks Kofaktor:");
        cofactor.display();

        // Transpose matriks kofaktor menjadi adjoin
        Matrix adj = cofactor.transpose();
        System.out.println("\n Matriks Adjoin (transpose dari kofaktor):");
        adj.display();

        // Invers = (1/det(A)) * adj(A)
        Matrix inv = new Matrix(n, n);
        double scale = 1.0 / detA;
        System.out.println("\n Langkah akhir: Invers = (1/det(A)) * Adjoin(A)");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inv.setElement(i, j, adj.getElement(i, j) * scale);
            }
        }

        System.out.println("\n Matriks Invers (metode Adjoin):");
        inv.display();

        return inv;
    }

    // ==========================================================
    //  INVERS METODE OBE (GAUSS–JORDAN)
    // ==========================================================
    public static Matrix inversOBE(Matrix A) {
        if (!Matrix.isSquare(A)) {
            throw new IllegalArgumentException("Matriks harus persegi untuk dihitung inversnya.");
        }

        System.out.println("\n===== Metode OBE (Gauss–Jordan) untuk Invers =====");
        A.display();

        int n = A.getRows();
        Matrix aug = new Matrix(n, 2 * n);

        // Bentuk matriks [A | I]
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                aug.setElement(i, j, A.getElement(i, j));
            }
            aug.setElement(i, i + n, 1.0);
        }

        System.out.println("\n Matriks Augmentasi [A | I]:");
        aug.display();

        // Proses Gauss–Jordan dengan pivoting
        for (int i = 0; i < n; i++) {
            // Cari pivot terbesar (untuk stabilitas numerik)
            int pivot = i;
            double max = Math.abs(aug.getElement(i, i));
            for (int r = i + 1; r < n; r++) {
                double val = Math.abs(aug.getElement(r, i));
                if (val > max) {
                    max = val;
                    pivot = r;
                }
            }

            if (Matrix.isZero(max)) {
                System.out.printf("\nKolom %d tidak memiliki pivot. Matriks singular.\n", i + 1);
                return null;
            }

            if (pivot != i) {
                System.out.printf("\nMenukar baris R%d dengan R%d (pivoting)\n", i + 1, pivot + 1);
                aug.swapRows(i, pivot);
                aug.display();
            }

            // Normalisasi pivot
            double pivotVal = aug.getElement(i, i);
            aug.multiplyRow(i, 1.0 / pivotVal);
            System.out.printf("\nNormalisasi pivot R%d dengan %.10f\n", i + 1, pivotVal);
            aug.display();

            // Eliminasi kolom lainnya
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = aug.getElement(k, i);
                    if (!Matrix.isZero(factor)) {
                        aug.addMultiplyOfRow(k, i, -factor);
                        System.out.printf("Eliminasi: R%d = R%d - (%.6f)*R%d\n", k + 1, k + 1, factor, i + 1);
                        aug.display();
                    }
                }
            }
        }

        System.out.println("\n===== Matriks telah direduksi menjadi [I | A⁻¹] =====");
        aug.display();

        Matrix inv = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inv.setElement(i, j, aug.getElement(i, j + n));
            }
        }

        System.out.println("\n Matriks Invers (metode OBE):");
        inv.display();

        return inv;
    }
}
