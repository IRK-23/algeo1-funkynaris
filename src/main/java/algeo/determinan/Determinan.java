package algeo.determinan;
public final class Determinan {
    private static final double EPS = 1e-12;

    private Determinan() {} 
    
    // Cek Matriks Persegi
    private static boolean isSquare(double[][] A) {
        if (A == null ||  A.length == 0 || A.length != A[0].length) {
            return false;
        }   
        else return true;
    }

    // Menyalin Matriks
    private static double[][] copyMatrix(double[][] A) {
        double[][] B = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            System.arraycopy(A[i], 0, B[i], 0, A[0].length);
        }
        return B;   
    }

    // Mengecek nol (Untuk kemungkinan komputer tidak presisi)
    private static boolean isZero(double x) {
        return Math.abs(x) <= EPS;
    }

    // Matriks Minor
    private static double[][] minorOf (double[][] A, int row, int col) {
        int n = A.length;
        double[][] minor = new double[n - 1][n - 1];
        int mi = 0;
        for (int i = 0; i < n; i++) {
            if (i == row) continue;
            int mj = 0;
            for (int j = 0; j < n; j++) {
                if (j == col) continue; 
                minor[mi][mj] = A[i][j];
                mj++;
            }
            mi++;
        }
        return minor;
    }

    // Determinan : Ekspansi Kofaktor
    public static double detKofaktor(double [][] A){
        if (isSquare(A) == false) {
            throw new IllegalArgumentException("Matriks harus persegi");
        }else{
            int n = A.length;
            // Basis
            if (n == 1) return A[0][0];
            if (n == 2) return A[0][0]*A[1][1] - A[0][1]*A[1][0];
            // Rekurens
            double det = 0.0;
            for (int i = 0; i < n; i++) {
                double a0 = A[0][i];
                if (isZero(a0)) continue;
                double[][] minor = minorOf(A, 0, i);
                double kof = ((i % 2 == 0) ? +1.0 : -1.0) * a0 * detKofaktor(minor);
                det += kof;
            }
            return det;
        }
    }

    // Determinan : Reduksi Baris
    public static double detReduksiBaris(double [][] A){
        if (isSquare(A) == false) {
            throw new IllegalArgumentException("Matriks harus persegi");
        }else{
            double n = A.length;
            return n;
        }
    }

    
}