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
            if (n == 1) {
                System.out.println("Determinan matriks 1x1 adalah elemen itu sendiri");
                return A.getElement(0, 0);
            }
            if (n == 2) {
                System.out.println("Determinan matriks 2x2 dihitung dengan rumus ad - bc");
                return (A.getElement(0, 0))*(A.getElement(1, 1)) - (A.getElement(0, 1))*(A.getElement(1, 0));
            }
            // Rekurens
            double det = 0.0;
            for (int i = 0; i < n; i++) {
                double a0 = A.getElement(0, i);
                if (Matrix.isZero(a0)) continue;
                System.out.println("Pembuatan Matriks Minor");
                Matrix minor = Matrix.minorOf(A, 0, i);
                minor.display();
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
            int n = A.getRows();
            Matrix m = A.copy();
            int sign = 1;
            
            System.out.println("\n===== Memulai Perhitungan Determinan dengan Reduksi Baris =====");
            System.out.println("Matriks Awal:");
            m.display();

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
                    System.out.println("\nMatriks menjadi singular (diagonal nol), determinan adalah 0.");
                    return 0.0; // Singular matriks - determinan nol
                }

                if (pivot != i) {
                    System.out.printf("\nLangkah: Menukar R%d dengan R%d. Tanda determinan dikali -1.\n", i+1, pivot+1);
                    m.swapRows(pivot, i);
                    sign = -sign; // Tukar baris mengubah tanda determinan
                    m.display();
                }

                for (int j=i+1; j<n; j++) {

                    double factor = m.getElement(j, i) / m.getElement(i, i);
                    if (Math.abs(factor) > Matrix.EPS) {
                        m.addMultiplyOfRow(j, i, -factor);
                        System.out.printf("\nLangkah: R%d = R%d - (%.3f) * R%d\n", j + 1, j + 1, factor, i + 1);
                        m.display();
                    }
                }
            }
            double det = (sign == 1) ? 1.0 : -1.0;
            System.out.println("\nMatriks segitiga atas terbentuk. Determinan adalah perkalian diagonal:");
            for (int k=0; k<n; k++) {
                det *= m.getElement(k, k);
            }

            return det;
        }
    } 
    
    public static double detReduksiBarisWithSteps(Matrix A) {
        if (!Matrix.isSquare(A)) {
            throw new IllegalArgumentException("Matriks harus persegi");
        }
    
        System.out.println("\n===== Memulai Perhitungan Determinan dengan Reduksi Baris =====");
        Matrix m = A.copy();
        System.out.println("Matriks Awal:");
        m.display();

        int n = m.getRows();
        int sign = 1;
    
        for (int i = 0; i < n; i++) {
            int pivot = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(m.getElement(j, i)) > Math.abs(m.getElement(pivot, i))) {
                    pivot = j;
                }
            }
    
            if (Matrix.isZero(m.getElement(pivot, i))) {
                System.out.println("\nMatriks menjadi singular (diagonal nol), determinan adalah 0.");
                return 0.0;
            }
    
            if (pivot != i) {
                System.out.printf("\nLangkah: Menukar R%d dengan R%d. Tanda determinan dikali -1.\n", i + 1, pivot + 1);
                m.swapRows(pivot, i);
                sign = -sign;
                m.display();
            }
    
            for (int j = i + 1; j < n; j++) {
                double factor = m.getElement(j, i) / m.getElement(i, i);
                if (!Matrix.isZero(factor)) {
                    System.out.printf("\nLangkah: R%d = R%d - (%.3f) * R%d\n", j + 1, j + 1, factor, i + 1);
                    m.addMultiplyOfRow(j, i, -factor);
                    m.display();
                }
            }
        }
        
        double det = (sign == 1) ? 1.0 : -1.0;
        System.out.println("\nMatriks segitiga atas terbentuk. Determinan adalah perkalian diagonal:");
        StringBuilder diagonalMultiply = new StringBuilder();
        for (int k = 0; k < n; k++) {
            det *= m.getElement(k, k);
            diagonalMultiply.append(String.format("%.3f", m.getElement(k,k)));
            if (k < n - 1) diagonalMultiply.append(" * ");
        }
        System.out.printf("Determinan = %d * (%s) = %.4f\n", sign, diagonalMultiply.toString(), det);
        System.out.println("===============================================================");
        
        return det;
    }

}