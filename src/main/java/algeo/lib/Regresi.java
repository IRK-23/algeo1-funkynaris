package algeo.lib;

import java.util.ArrayList;
import java.util.List;

public class Regresi {
    // Basis Polinomial 
    private List<int[]> basisPol; 
    public List<int[]> getBasis() {
        return this.basisPol;
    }
    // Generate Basis
    private static List<int[]> generateBasis(int p, int degree) {
        List<int[]> out = new ArrayList<>();
        int[] cur = new int[p];
        for (int d = 0; d <= degree; d++) {
            gen(0, p, d, cur, out);  
        }
        return out;
    }
    private static void gen(int idx, int p, int rem, int[] cur, List<int[]> out) {
        if (idx == p - 1) {
            for (int e = 0; e <= rem; e++) {
                cur[idx] = e;
                out.add(cur.clone());
            }
            return;
        }
        for (int e = 0; e <= rem; e++) {
            cur[idx] = e;
            gen(idx + 1, p, rem - e, cur, out);
        }
    }
    // Regresi Polinomial Berganda
    public Matrix regresi(Matrix X, Matrix Y, int degree) {
        int var = X.getCols();
        this.basisPol = generateBasis(var, degree);
        // Buat Matriks Desain dan Matriks Yang lainnya
        System.out.println("Membuat Matriks Desain..."); // Tahap nomor 1
        Matrix design = designMatrix(X, degree, var);
        design.display();
        Matrix Xt = design.transpose();
        Matrix XtX = Matrix.multiply(Xt,design);
        Matrix Xty = Matrix.multiply(Xt,Y);
        if (XtX == null || Xty == null) {
            throw new IllegalStateException("Perkalian matriks gagal (cek dimensi).");
        }
        System.out.println("Perkalian Matriks sesuai dengan persamaan..."); // Tahap nomor 2
        // Hitung totalnya dengan Error Handling
        try {
            Matrix Xinv = Invers.inversOBE(XtX);
            Matrix result = Matrix.multiply(Xinv, Xty);
            Xinv.display();
            System.out.println("Matriks di atas akan dikalikan dengan Matriks di bawah ini: ");
            result.display();
            return result;
        } catch (ArithmeticException e) {
            // Tanpa invers
            int m = XtX.getRows();
            Matrix aug = new Matrix (m, m + 1);
            for (int i=0;i<m;i++){
                for (int j=0;j<m;j++){
                    aug.setElement(i, j, XtX.getElement(i, j));
                }
                aug.setElement(i, m, Xty.getElement(i, 0));
            }
            double[] solution = SPLSolver.solveWithGauss(aug);
            Matrix result = new Matrix(m, 1);
            for (int i=0;i<m;i++){
                result.setElement(i, 0, solution[i]);
            }
            return result;
        }
    }    
    // Mengubah ke matriks desain
    private Matrix designMatrix(Matrix X, int degree, int var) {
        int kom = kombinasi(degree + var, var);
        Matrix result = new Matrix(X.getRows(), kom);
        for (int i=0;i<X.getRows();i++){
            double[] rows = new double[var];
            for (int j=0;j<var;j++){
                rows[j] = X.getElement(i, j);
            }
            double[] newRows = transformRows(rows, degree, var);
            for (int j=0;j<kom;j++){    
                result.setElement(i, j, newRows[j]);
            }
        }
        return result;
    }
    // Print Hasil Regresi
    public static void printHasil(Matrix koef, List<int[]> basisPol){
        StringBuilder res = new StringBuilder();
        res.append("y = ");
        List<String> basisString = buildBasisString(basisPol);
        for (int i=0;i<koef.getRows();i++){
            double val = koef.getElement(i, 0);
            String var = basisString.get(i);
            if (i == 0) {
                res.append(String.format("%.3f", val));
            } else {
                if (val >= 0) {
                    res.append(" + ").append(String.format("%.3f * %s", val, var));
                } else {
                    res.append(" - ").append(String.format("%.3f * %s", Math.abs(val), var));
                }
            }
        }
        System.out.println(res);
    }
    private static List<String> buildBasisString(List<int[]> basisPol){
        List<String> string = new ArrayList<>();
        for (int[] basis : basisPol){
            boolean allzero = true;
            for (int cek : basis) if (cek != 0) {allzero = false; break;}
            if (allzero) {
                string.add("1");
                continue;
            }
            StringBuilder temp = new StringBuilder();
            boolean first = true;
            for (int i=0;i<basis.length;i++){
                int val = basis[i];
                if (val != 0){
                    if (!first) temp.append("*");
                    temp.append("x").append(i+1);
                    if (val > 1) temp.append("^").append(val);
                    first = false;
                }
            }
            string.add(temp.toString());
        }
        return string;
    }    
    // Method Pembantu
    // Menghitung kombinasi
    private static int kombinasi (int n, int r) {
        if (r > n - r) r = n - r; 
        int hasil = 1;
        for (int i = 0; i < r; i++) {
            hasil *= (n - i);
            hasil /= (i + 1);
        }
        return hasil;
    }

    // Transformasi baris
    private double[] transformRows (double[] rows, int degree, int var) {
        double[] newRows = new double[kombinasi(degree + var, var)];
        for (int i=0;i<newRows.length;i++) {
            int[] x = basisPol.get(i);
            double val = 1.0;
            for (int j=0;j<var;j++){
                int a = x[j];
                if (a != 0) {
                    val *= Math.pow(rows[j], a);
                }
            }
            newRows[i] = val;
        }
        return newRows;
    }
}
