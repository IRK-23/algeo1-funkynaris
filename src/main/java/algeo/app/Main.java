package algeo.app;

import java.io.*;
import java.util.*;
import algeo.lib.*;

/**
 * Kelas Main menjalankan program utama Tugas Besar 1 IF2123 Aljabar Linier dan Geometri.
 * Menyediakan fitur:
 * 1. Sistem Persamaan Linear
 * 2. Determinan
 * 3. Matriks Balikan
 * 4. Interpolasi (Polinomial dan Bézier)
 * 5. Regresi Polinomial
 *
 * Seluruh perhitungan dilakukan melalui kelas pada package algeo.lib.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
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
                hasil = (x == null) ? "Matriks tidak memiliki invers" : x.toString();
            }
            default -> hasil = "Metode tidak valid!";
        }

        System.out.println("\nHasil SPL:\n" + hasil);
        simpanOutput("SPL", hasil);
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
        simpanOutput("Determinan", String.valueOf(hasil));
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
        Matrix hasil;

        try {
            hasil = (metode == 1) ? Invers.inversOBE(A) : Invers.inversAdjoin(A);
            System.out.println("Invers Matriks:\n");
            hasil.display();
            simpanOutput("Invers", hasil.toString());
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

        if (metode == 1) {
            // Interpolasi Polinomial
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

            String hasil = coeff.toString() + "\nP(" + xEval + ") = " + yEval;
            simpanOutput("InterpolasiPolinomial", hasil);

        } else if (metode == 2) {
            // Interpolasi Bézier Kubik
            System.out.print("Masukkan jumlah titik sampel: ");
            int n = inputInt();
            double[][] titik = new double[n][2];
            for (int i = 0; i < n; i++) {
                System.out.print("x" + (i + 1) + " y" + (i + 1) + ": ");
                titik[i][0] = sc.nextDouble();
                titik[i][1] = sc.nextDouble();
            }

            double[][] B = Interpolasi.interpolasiBezierKubik(titik);
            if (B != null) {
                Interpolasi.printBezierPoints(B);
                StringBuilder sb = new StringBuilder();
                sb.append("Titik Kontrol Bézier Kubik:\n");
                for (int i = 0; i < B.length; i++) {
                    sb.append(String.format("B%d = (%.4f, %.4f)\n", i, B[i][0], B[i][1]));
                }
                simpanOutput("InterpolasiBezier", sb.toString());
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

        double[][] xData = new double[n][k];
        double[][] yData = new double[n][1];

        for (int i = 0; i < n; i++) {
            System.out.print("Data ke-" + (i + 1) + " (" + k + " x dan 1 y): ");
            for (int j = 0; j < k; j++) xData[i][j] = sc.nextDouble();
            yData[i][0] = sc.nextDouble();
        }

        Matrix X = new Matrix(xData);
        Matrix Y = new Matrix(yData);

        Regresi regresi = new Regresi();
        Matrix koef = regresi.regresi(X, Y, d);

        System.out.println("\nHasil Regresi:");
        Regresi.printHasil(koef, regresi.getBasis());

        StringBuilder hasil = new StringBuilder();
        hasil.append("Koefisien Regresi:\n").append(koef.toString());
        simpanOutput("Regresi", hasil.toString());
    }

    // ===========================================================
    // UTILITAS INPUT/OUTPUT
    // ===========================================================

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
            double[][] data = new double[m][n];
            for (int i = 0; i < m; i++) {
                System.out.print("Baris ke-" + (i + 1) + ": ");
                for (int j = 0; j < n; j++) data[i][j] = sc.nextDouble();
            }
            return new Matrix(data);
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

    private static void simpanOutput(String prefix, String isi) {
        System.out.print("Apakah hasil ingin disimpan ke file? (y/n): ");
        if (sc.next().equalsIgnoreCase("y")) {
            System.out.print("Masukkan nama file: ");
            String nama = sc.next();
            try (PrintWriter out = new PrintWriter(prefix + "_" + nama + ".txt")) {
                out.println(isi);
                System.out.println("Berhasil disimpan ke " + prefix + "_" + nama + ".txt");
            } catch (IOException e) {
                System.out.println("Gagal menyimpan file!");
            }
        }
    }

    private static int inputInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.print("Input tidak valid, coba lagi: ");
            }
        }
    }

    private static String arrayToString(double[] arr) {
        if (arr == null) return "Tidak ada solusi (det = 0)";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++)
            sb.append(String.format("x%d = %.6f\n", i + 1, arr[i]));
        return sb.toString();
    }
}
