package algeo.lib;

/**
 * Kelas Invers menyediakan dua metode utama:
 * 1. Metode Adjoin (Kofaktor)
 * 2. Metode OBE (Gauss–Jordan)
 *
 * Keduanya telah diperkuat untuk mendeteksi kasus singular dan parametrik.
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
        System.out.printf("\nDeterminan(A) = %.6f\n", detA);

        if (Matrix.isZero(detA)) {
            System.out.println(" Matriks singular — determinan nol, tidak memiliki invers unik.");
            return null;
        }

        int n = A.getRows();
        Matrix adj = new Matrix(n, n);

        System.out.println("\n Langkah-langkah pembentukan matriks kofaktor:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Matrix minor = Matrix.minorOf(A, i, j);
                double detMinor = Determinan.detKofaktor(minor);
                double sign = ((i + j) % 2 == 0) ? 1.0 : -1.0;
                double cof = sign * detMinor;
                adj.setElement(j, i, cof); // langsung transpose
                System.out.printf("Cofaktor C[%d][%d] = %.6f (minor det = %.6f)\n", i + 1, j + 1, cof, detMinor);
            }
        }

        System.out.println("\n Matriks Adjoin (Transpose dari Kofaktor):");
        adj.display();

        Matrix inv = new Matrix(n, n);
        System.out.println("\n Langkah akhir: Invers = (1/det(A)) * Adjoin(A)");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inv.setElement(i, j, adj.getElement(i, j) / detA);
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

        // Proses Gauss–Jordan dengan deteksi singularitas
        for (int i = 0; i < n; i++) {
            // Pivoting
            int pivot = i;
            while (pivot < n && Matrix.isZero(aug.getElement(pivot, i))) {
                pivot++;
            }

            if (pivot == n) {
                System.out.printf("\n Kolom %d tidak memiliki pivot. Matriks tidak memiliki invers (kasus parametrik/singular).\n", i + 1);
                return null;
            }

            if (pivot != i) {
                System.out.printf("\n Menukar baris R%d dengan R%d karena pivot nol.\n", i + 1, pivot + 1);
                aug.swapRows(i, pivot);
                aug.display();
            }

            // Normalisasi pivot
            double pivotVal = aug.getElement(i, i);
            if (Matrix.isZero(pivotVal)) {
                System.out.printf("\n Pivot di baris %d masih nol, matriks tidak dapat dibalik.\n", i + 1);
                return null;
            }

            System.out.printf("\nNormalisasi pivot R%d dengan membagi seluruh baris dengan %.6f\n", i + 1, pivotVal);
            aug.multiplyRow(i, 1.0 / pivotVal);
            aug.display();

            // Eliminasi kolom atas dan bawah pivot
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = aug.getElement(k, i);
                    if (!Matrix.isZero(factor)) {
                        System.out.printf("Eliminasi R%d -> R%d = R%d - (%.6f)*R%d\n", k + 1, k + 1, k + 1, factor, i + 1);
                        aug.addMultiplyOfRow(k, i, -factor);
                        aug.display();
                    }
                }
            }
        }

        // Cek apakah sisi kiri sudah menjadi identitas
        boolean isIdentity = true;
        for (int i = 0; i < n && isIdentity; i++) {
            for (int j = 0; j < n; j++) {
                double val = aug.getElement(i, j);
                if ((i == j && Math.abs(val - 1.0) > 1e-9) ||
                    (i != j && Math.abs(val) > 1e-9)) {
                    isIdentity = false;
                    break;
                }
            }
        }

        if (!isIdentity) {
            System.out.println("\n Matriks gagal menjadi identitas sempurna di sisi kiri — kemungkinan parametrik atau singular.");
            return null;
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
