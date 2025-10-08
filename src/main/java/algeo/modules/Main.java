package algeo.modules;

public class Main {
    public static void main(String[] args) {
        double[][] A = {
            {1, 1, 0},
            {2, 3, 2},
            {2, 1, 3}
        };

        System.out.println("Matriks A:");
        printMatrix(A);

        // Invers dengan metode Adjoin
        double[][] inversAdjoin = Invers.inversAdjoin(A);
        System.out.println("\nInvers dari Matriks A (metode Adjoin):");
        printMatrix(inversAdjoin);

        // Invers dengan metode Augment (Gauss–Jordan)
        double[][] inversAugment = Invers.augment(A);
        System.out.println("\nInvers dari Matriks A (metode Augment / Gauss–Jordan):");
        printMatrix(inversAugment);
    }

    // Fungsi bantu untuk mencetak matriks
    public static void printMatrix(double[][] M) {
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                System.out.printf("%10.4f ", M[i][j]);
            }
            System.out.println();
        }
    }
}
