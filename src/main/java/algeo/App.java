package algeo;

import java.io.*;
import java.util.*;
import algeo.lib.*;

/**
 * Kelas Main menjalankan program utama Tugas Besar 1 IF2123 Aljabar Linier dan Geometri.
 * Menyediakan fitur:
 * 1. Sistem Persamaan Linear (SPL)
 * 2. Determinan
 * 3. Matriks Balikan
 * 4. Interpolasi (Polinomial dan Bézier)
 * 5. Regresi Polinomial
 *
 * Seluruh perhitungan dilakukan melalui kelas pada package algeo.lib.
 */
public class App {

    private static final Scanner sc = new Scanner(System.in);
    private static final String OUTPUT_FOLDER = "output";
    private static final String OUTPUT_FILE = OUTPUT_FOLDER + "/hasil.txt";

    public static void main(String[] args) {
        buatFolderOutput();

        while (true) {
            System.out.println("\n=== MENU UTAMA ===");
            System.out.println("1. Sistem Persamaan Linier (SPL)");
            System.out.println("2. Determinan");
            System.out.println("3. Matriks Balikan");
            System.out.println("4. Interpolasi");
            System.out.println("5. Regresi Polinomial");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            int pilih = inputInt();

            switch (pilih) {
                case 1 -> menuSPL();
                case 2 -> menuDeterminan();
                case 3 -> menuInvers();
                case 4 -> menuInterpolasi();
                case 5 -> menuRegresi();
                case 0 -> {
                    System.out.println("Terima kasih! Program selesai.");
                    return;
                }
                default -> System.out.println("Pilihan tidak valid!");
            }
        }
    }

    // ===========================================================
    // MENU 1 - SISTEM PERSAMAAN LINEAR
    // ===========================================================
    private static void menuSPL() {
        System.out.println("\n--- SISTEM PERSAMAAN LINEAR ---");
        System.out.println("1. Eliminasi Gauss");
        System.out.println("2. Eliminasi Gauss-Jordan");
        System.out.println("3. Kaidah Cramer");
        System.out.println("4. Metode Matriks Balikan");
        System.out.print("Pilih metode: ");
        int metode = inputInt();

        Matrix aug = inputMatrix(true);
        SPLSolver solver = new SPLSolver();
        String hasil;

        switch (metode) {
            case 1 -> {
                double[] sol = SPLSolver.solveWithGauss(aug);
                hasil = arrayToString(sol);
            }
            case 2 -> hasil = SPLSolver.solveWithGaussJordan(aug).toString();
            case 3 -> {
                Matrix A = aug.subMatrix(0, 0, aug.getRows(), aug.getCols() - 1);
                Matrix b = aug.getColVector(aug.getCols() - 1);
                double[] sol = solver.solveWithCramer(A, b);
                hasil = arrayToString(sol);
            }
            case 4 -> {
                Matrix A = aug.subMatrix(0, 0, aug.getRows(), aug.getCols() - 1);
                Matrix b = aug.getColVector(aug.getCols() - 1);
                Matrix x = solver.solveWithInverse(A, b);
                hasil = (x == null) ? "Matriks tidak memiliki invers unik" : x.toString();
            }
            default -> hasil = "Metode tidak valid!";
        }

        System.out.println("\nHasil SPL:\n" + hasil);
        String[] output = new String[4];
        output[0] = "=== HASIL SPL ===";
        output[1] = "Metode: " + switch (metode) {
            case 1 -> "Eliminasi Gauss";
            case 2 -> "Eliminasi Gauss-Jordan";
            case 3 -> "Kaidah Cramer";
            case 4 -> "Matriks Balikan";
            default -> "Tidak valid";
        };
        output[2] = "Matriks Awal:\n" + aug.toString();
        output[3] = "Hasil:\n" + hasil;
        simpanOutput(output);
    }

    // ===========================================================
    // MENU 2 - DETERMINAN
    // ===========================================================
    private static void menuDeterminan() {
        System.out.println("\n--- DETERMINAN MATRKS ---");
        System.out.println("1. Metode Kofaktor");
        System.out.println("2. Metode Reduksi Baris (OBE)");
        System.out.print("Pilih metode: ");
        int metode = inputInt();

        Matrix A = inputMatrix(false);
        double hasil = (metode == 1)
                ? Determinan.detKofaktor(A)
                : Determinan.detReduksiBaris(A);

        System.out.println("Determinan = " + hasil);
        String[] output = new String[4];
        output[0] = "=== HASIL DETERMINAN ===";
        output[1] = "Metode: " + (metode == 1 ? "Kofaktor" : "Reduksi Baris");
        output[2] = "Matriks:\n" + A.toString();
        output[3] = "Determinan = " + hasil;
        simpanOutput(output);
    }

    // ===========================================================
    // MENU 3 - INVERS MATRKS
    // ===========================================================
    private static void menuInvers() {
        System.out.println("\n--- MATRIKS BALIKAN ---");
        System.out.println("1. Metode OBE (Gauss–Jordan)");
        System.out.println("2. Metode Adjoin");
        System.out.print("Pilih metode: ");
        int metode = inputInt();

        Matrix A = inputMatrix(false);
        Matrix hasil = null;
        
        String [] output = new String[4];
        try {
            if (metode == 1) hasil = Invers.inversOBE(A);
            else if (metode == 2) hasil = Invers.inversAdjoin(A);
            else {
                System.out.println("Pilihan tidak valid.");
                return;
            }

            if (hasil == null) {
                System.out.println("\n Matriks tidak memiliki invers unik (singular).");
                output[0] = "=== HASIL INVERS ===";
                output[1] = "Metode: " + (metode == 1 ? "OBE (Gauss–Jordan)" : "Adjoin");
                output[2] = "Matriks awal:\n" + A.toString();
                output[3] = "Matriks tidak memiliki invers unik.";
                simpanOutput(output);
                return;
            }

            System.out.println("\n Invers Matriks:");
            hasil.display();
            output[0] = "=== HASIL INVERS ===";
            output[1] = "Metode: " + (metode == 1 ? "OBE (Gauss–Jordan)" : "Adjoin");
            output[2] = "Matriks awal:\n" + A.toString();
            output[3] = "Matriks invers:\n" + hasil.toString();
            simpanOutput(output);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ===========================================================
    // MENU 4 - INTERPOLASI
    // ===========================================================
    private static void menuInterpolasi() {
        System.out.println("\n--- INTERPOLASI ---");
        System.out.println("1. Interpolasi Polinomial");
        System.out.println("2. Interpolasi Splina Bézier Kubik");
        System.out.print("Pilih metode: ");
        int metode = inputInt();
        
        String [] output = new String[4];

        if (metode == 1) {
            System.out.print("Masukkan jumlah titik data: ");
            int n = inputInt();
            double[] x = new double[n];
            double[] y = new double[n];

            for (int i = 0; i < n; i++) {
                System.out.print("x" + (i + 1) + " y" + (i + 1) + ": ");
                x[i] = sc.nextDouble();
                y[i] = sc.nextDouble();
            }

            Matrix coeff = Interpolasi.interpolasiPolinomial(x, y);
            if (coeff == null) {
                System.out.println("Interpolasi gagal (matriks tidak dapat diinvers).");
                return;
            }

            System.out.println("\nKoefisien Polinomial:");
            coeff.display();
            Interpolasi.printPolynomial(coeff);

            System.out.print("\nMasukkan nilai x yang ingin dihitung: ");
            double xEval = sc.nextDouble();
            double yEval = Interpolasi.evaluatePolynomial(coeff, xEval);
            System.out.printf("P(%.4f) = %.6f\n", xEval, yEval);

            output[0] = "=== HASIL INTERPOLASI POLINOMIAL ===";
            output[1] = "Metode: Polinomial";
            output[2] = ""; // Input yang digunakan
            output[3] = ""; // Domain Interpolasi
            output[4] = "=== HASIL INTERPOLASI POLINOMIAL ===\n" + coeff.toString() + "\nP(" + xEval + ") = " + yEval;
            simpanOutput(output);

        } else if (metode == 2) {
            System.out.print("Masukkan jumlah titik sampel: ");
            int n = inputInt();
            Matrix titik = new Matrix(n, 2);

            for (int i = 0; i < n; i++) {
                System.out.print("x" + (i + 1) + " y" + (i + 1) + ": ");
                titik.setElement(i, 0, sc.nextDouble());
                titik.setElement(i, 1, sc.nextDouble());
            }

            Matrix B = Interpolasi.interpolasiBezierKubik(titik);
            if (B != null) {
                System.out.println("\nTitik Kontrol Bézier Kubik:");
                B.display();
                output[0] = "=== HASIL INTERPOLASI BÉZIER ===";
                output[1] = "Metode: Bézier Kubik"; 
                output[2] = "" ; // Input yang digunakan
                output[3] = "" ; // Domain Interpolasi
                output[4] = "=== HASIL INTERPOLASI BÉZIER ===\n" + B.toString();
                simpanOutput(output);
            }
        } else {
            System.out.println("Pilihan tidak valid.");
        }
    }

    // ===========================================================
    // MENU 5 - REGRESI POLINOMIAL
    // ===========================================================
    private static void menuRegresi() {
        System.out.println("\n--- REGRESI POLINOMIAL ---");
        System.out.print("Masukkan jumlah titik data: ");
        int n = inputInt();
        System.out.print("Masukkan jumlah variabel independen: ");
        int k = inputInt();
        System.out.print("Masukkan derajat polinomial: ");
        int d = inputInt();

        Matrix X = new Matrix(n, k);
        Matrix Y = new Matrix(n, 1);

        System.out.println("Masukkan data (setiap baris: x1 x2 ... xk y):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) X.setElement(i, j, sc.nextDouble());
            Y.setElement(i, 0, sc.nextDouble());
        }

        Regresi regresi = new Regresi();
        Matrix koef = regresi.regresi(X, Y, d);

        System.out.println("\nHasil Regresi:");
        Regresi.printHasil(koef, regresi.getBasis());

        String[] output = new String[4];
        output[0] = "=== HASIL REGRESI POLINOMIAL ===";
        output[1] = "Input yang digunakan :";
        output[2] = "Matriks X:\n" + X.toString() + "Matriks Y:\n" + Y.toString();
        output[3] = "Persamaan hasil: " + koef.toString();
        simpanOutput(output);
    }

    // ===========================================================
    // UTILITAS INPUT/OUTPUT
    // ===========================================================

    /** Membuat folder output jika belum ada */
    private static void buatFolderOutput() {
        File folder = new File(OUTPUT_FOLDER);
        if (!folder.exists()) folder.mkdirs();
    }

    /** Membaca matriks dari input manual atau file */
    private static Matrix inputMatrix(boolean augmented) {
        System.out.println("1. Input manual");
        System.out.println("2. Baca dari file .txt");
        System.out.print("Pilih metode input: ");
        int mode = inputInt();

        if (mode == 1) {
            System.out.print("Masukkan jumlah baris: ");
            int m = inputInt();
            System.out.print("Masukkan jumlah kolom: ");
            int n = inputInt();
            Matrix M = new Matrix(m, n);
            System.out.println("Masukkan elemen matriks (pisahkan dengan spasi):");
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    String inp = sc.next();
                    M.setElement(i, j, parseDouble(inp));
                }
            }
            return M;
        } else {
            System.out.print("Masukkan path file: ");
            String path = sc.next();
            try {
                return Matrix.fromFile(path);
            } catch (Exception e) {
                System.out.println("Gagal membaca file! Pastikan format sesuai.");
                return new Matrix(0, 0);
            }
        }
    }

    /** Menyimpan hasil ke dalam file hasil.txt */
    private static void simpanOutput(String[] isi) {
        try (PrintWriter out = new PrintWriter(new FileWriter(OUTPUT_FILE, true))) {
            for (int i=0;i<isi.length;i++) out.println(isi);
            out.println("--------------------------------------------------");
            System.out.println(" Hasil disimpan ke " + OUTPUT_FILE);
        } catch (IOException e) {
            System.out.println(" Gagal menyimpan hasil!");
        }
    }

    /** Membaca integer dari input pengguna dengan validasi */
    private static int inputInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.print("Input tidak valid, coba lagi: ");
            }
        }
    }

    /** Mengubah array solusi menjadi format string */
    private static String arrayToString(double[] arr) {
        if (arr == null) return "Tidak ada solusi (det = 0)";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++)
            sb.append(String.format("x%d = %.6f\n", i + 1, arr[i]));
        return sb.toString();
    }

    /* Handle Kasus Input Koma, Titik, dan Pecahan */
    private static double parseDouble(String input){
        input = input.trim();               // Hapus spasi
        input = input.replace(",", ".");    // Ganti koma dengan titik
        if (input.contains("/")){
            String[] bagi = input.split("/");
            double first = Double.parseDouble(bagi[0]);
            double second = Double.parseDouble(bagi[1]);
            if (second == 0) throw new ArithmeticException("Pembagi tidak boleh nol");
            return first/second;
        }
        return Double.parseDouble(input);
    }

}
