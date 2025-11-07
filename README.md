# Aljabar Linier dan Geometri Tubes 1 Template
Proyek ini mengimplementasikan algoritma-algoritma aljabar linier secara modular dalam bahasa Java:
1. SPL: Eliminasi Gauss, Gauss–Jordan, Cramer, dan Matriks Balikan
2. Determinan: Ekspansi Kofaktor, Reduksi Baris
3. Invers: Adjoin (Kofaktor), Augment (OBE/Gauss–Jordan)
4. Interpolasi: Polinomial (Vandermonde), Splina Bézier Kubik
5. Regresi: Regresi Polinomial Berganda (Least Squares / Persamaan Normal)
Semua fitur tersedia sebagai library (class publik) dan juga melalui CLI (program utama) untuk membaca input dari konsol atau file, serta menyimpan hasil ke file .txt.

## Panduan Program
1. Pilih jenis masalah
      1) SPL 2) Determinan 3) Invers 4) Interpolasi 5) Regresi 0) Keluar
2. Pilih metode (bergantung jenis masalah, mis. Gauss/Gauss–Jordan/Cramer/Inverse untuk SPL)
3. Input data
   - Dari konsol: ketik dimensi & elemen
   - Dari file: path ke file teks berformat matriks (baris dipisah newline, elemen dipisah spasi)
5. Proses & tampilkan hasil
  - Cetak hasil di konsol (dan langkah OBE jika diminta)
  - Simpan hasil ke file output .txt
  - Ulang (program kembali ke menu utama)
  - Catatan: Untuk SPL, program mendeteksi jenis solusi (unik/tak hingga/tak ada) dan membangun solusi parametrik bila ada variabel bebas.

## Format Input
Rincian Umum
    - Semua modul menerima input manual (diketik) atau melalui satu berkas .txt.
    - Pemisah bilangan: spasi tunggal (disarankan desimal .).
    - Error handling wajib: apabila format isi file tidak sesuai spesifikasi, program harus menolak dengan pesan yang jelas.
    - Nama file hanya deskripsi; jangan gunakan nama file sebagai dasar validasi isi.
    - Setiap modul dapat menyimpan hasil ke .txt (memuat: metode, input, dan hasil).
A. Sistem Persamaan Linier (SPL)
Input:
  - Matriks augmented SPL melalui ketik manual atau file .txt.
  - Metode: sesuai daftar (Gauss, Gauss–Jordan, Cramer, Balikan).

B. Determinan
Input:
  - Matriks persegi melalui ketik/file .txt.
  - Metode: Kofaktor / Reduksi Baris.

C. Matriks Balikan (Invers)
Input:
  - Matriks persegi melalui ketik/file .txt.
  - Metode: Adjoin (Kofaktor) / Augment (OBE).

D. Interpolasi
Input:
  - Jumlah titik n.
  - Data titik (x1, y1), …, (xn, yn) via ketik/file.
  - Metode: (1) Polinomial (Vandermonde) atau (2) Splina Bézier Kubik.

E. Regresi Polinomial Berganda
Input:
  - Jumlah sampel n.
  - Derajat regresi m.
  - Sampel: (x_{i,1}, …, x_{i,k}, y_i) dengan k ≤ 5 (via ketik/file).


## Requirements ( Java and Maven)

Before building and running the **Matrix Calculator**, make sure you have the following installed:

### Java
- **Version:** 17 or higher
- **Download links:**
  - [Oracle JDK 17](https://www.oracle.com/java/technologies/downloads)

### Maven
- **Version:** 3.2.5 or higher (recommended 3.6.3+)
- **Download links:**
  - [Direct Apache Maven Official Downloads](https://dlcdn.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.zip)

### Additional installation info

### Windows
For maven installation, download the .zip and it should contain a directory with
```
apache-maven-<version>/
├── bin/               <-- executable scripts (mvn, mvn.cmd)
├── boot/         
├── conf/          
├── lib/          
├── NOTICE
├── LICENSE
├── README.txt
```

Put bin/ in environment PATH to use in terminal. [Add folder to PATH tutorial](https://www.youtube.com/watch?v=pGRw1bgb1gU)

### Linux
```bash
sudo apt update
sudo apt install openjdk-17-jdk -y
sudo apt install maven -y
```

### MacOS
```bash
brew install openjdk@17
brew install maven
```

## How to develop

Using maven, the root development directory is `src/main/java/algeo`
There should not be any coding outside of that directory other than `test` using JUnit or other libraries.

Inside `src/main/java/algeo`, develop modules that are modular to use in the main program (`App.java`)

When running `mvn exec:java` later, `App.java` main program will be the one that is run.

## How to run
1. Compiling the program
The following command will produce a `target` directory with `matrix-calculator-1.0-SNAPSHOT.jar` in it
```bash
mvn clean package
```

alternatively, if you don't want to make a .jar file, you can use
```bash
mvn clean compile
```

2. Running the program
To run CLI, run:
```bash
mvn exec:java
```

To run GUI, be sure to uncomment the main GUI and run:
```bash
mvn clean javafx:run
```

when the program is first run, it should print in terminal:
```bash
Hai 
Halo Algeo!
```
