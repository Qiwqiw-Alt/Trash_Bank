package Database;

import Model.BankSampah;

import java.io.*;
import java.util.ArrayList;

public class DataBankSampah {
    private ArrayList<BankSampah> daftarSemuaBankSampah = new ArrayList<BankSampah>();

    public void addBankSampah(BankSampah bankSampahBaru){ // untuk nambah bank sampah yang dipakai di SignIn
        daftarSemuaBankSampah.add(bankSampahBaru);
    }

    //filepath = src/Database/BankSampah/data.txt
    // Load/baca data sebelum Sign In dan Login
    String delim = "\\|";

    public ArrayList<BankSampah> loadData(String filepath) {
        daftarSemuaBankSampah.clear();
        File file = new File(filepath);

        try {
            if (!file.exists()) {
                throw new IOException("File" + filepath + "tidak ada");
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()).trim() != null) {
                String[] parts = line.split(delim);
                if (parts.length >= 4) {
                    daftarSemuaBankSampah.add(new BankSampah(parts[0], parts[1], parts[2], parts[3]));
                    // 0 = username biasa
                    // 1 = nama admin -> buat login/sigin
                    // 2 = nomor hp
                    // 3 = password -> buat login/signin
                }
            }
            return BankSampah;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return BankSampah;
        }
    }

    //Tulis data
    public void writeData(String filepath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {

            for (BankSampah penyetor : BankSampah) {
                String data = penyetor.getUsername() + "|" +
                        penyetor.getNamaLengkap() + "|" +
                        penyetor.getNohp() + "|" +
                        penyetor.getPassword();

                writer.write(data);
                writer.newLine();
            }
            System.out.println("--- Data berhasil disimpan ke file ---");

        } catch (IOException e) {
            System.err.println("Error: Gagal menyimpan data ke file. " + e.getMessage());
        }
    }
}
