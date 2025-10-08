package algeo.modules;

public class Main {
    public static void main(String[] args) {
        double[][] A = {
            {3, 2, -1},
            {1, 6, 3},
            {2, -4, 0}
        };

        System.out.println("Matriks A:");
        printMatrix(A);

        double[][] invers = Invers.inversAdjoin(A);

        System.out.println("\nInvers dari Matriks A (metode Adjoin):");
        printMatrix(invers);
    }

    public static void printMatrix(double[][] M) {
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                System.out.printf("%10.4f ", M[i][j]);
            }
            System.out.println();
        }
    }
}
