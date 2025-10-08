package algeo.lib;
public final class Determinan {
    private Determinan() {} 
    // Determinan : Ekspansi Kofaktor
    public static double detKofaktor(Matrix A){
        if (Matrix.isSquare(A) == false) {
            throw new IllegalArgumentException("Matriks harus persegi");
        }else{
            int n = A.getRows();
            // Basis
            if (n == 1) return A.getElement(0, 0);
            if (n == 2) return (A.getElement(0, 0))*(A.getElement(1, 1)) - (A.getElement(0, 1))*(A.getElement(1, 0));
            // Rekurens
            double det = 0.0;
            for (int i = 0; i < n; i++) {
                double a0 = A.getElement(0, i);
                if (Matrix.isZero(a0)) continue;
                Matrix minor = Matrix.minorOf(A, 0, i);
                double kof = ((i % 2 == 0) ? +1.0 : -1.0) * a0 * detKofaktor(minor);
                det += kof;
            }
            return det;
        }
    }

    // Determinan : Reduksi Baris
    public static double detReduksiBaris(Matrix A){
        if (Matrix.isSquare(A) == false) {
            throw new IllegalArgumentException("Matriks harus persegi");
        }else{
            Matrix.isSquare(A);
            int n = A.getRows();
            Matrix m = A.copy();
            int sign = 1;

            for (int i=0; i<n; i++) {
                // Partial pivoting
                int pivot = i;
                double pick = Math.abs(m.getElement(pivot, i));
                for (int j=i+1; j<n; j++){
                    double temp = Math.abs(m.getElement(j, i));
                    if (temp > pick) {
                        pick = temp;
                        pivot = j;
                    }
                }

                if (pick <= Matrix.EPS) {
                    return 0.0; // Singular matriks - determinan nol
                }

                if (pivot != i) {
                    m.swapRows(pivot, i);
                    sign = -sign; // Tukar baris mengubah tanda determinan
                }

                for (int j=i+1; j<n; j++) {
                    double factor = m.getElement(j, i) / m.getElement(i, i);
                    if (Math.abs(factor) > Matrix.EPS) m.addMultiplyOfRow(j, i, -factor);
                }
            }
            double det = (sign == 1) ? 1.0 : -1.0;
            for (int k=0; k<n; k++) {
                det *= m.getElement(k, k);
            }

            return det;
        }
    }   
}