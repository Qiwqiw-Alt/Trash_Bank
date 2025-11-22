package Database;

import Model.BankSampah;

import java.io.*;
import java.util.ArrayList;

public class DatabaseBankSampah {
    private ArrayList<BankSampah> daftarSemuaBankSampah = new ArrayList<BankSampah>();

    public void addBankSampah(BankSampah bankSampahBaru){ // untuk nambah bank sampah yang dipakai di SignIn
        daftarSemuaBankSampah.add(bankSampahBaru);
    }

    //filepath = src/Database/BankSampah/data.txt
    // Load/baca data sebelum Sign In dan Login
    String delim = "\\|";

    public String generateBankId() {
        int max = 0;

        for (BankSampah a : daftarSemuaBankSampah) {
            String id = a.getIdBank().substring(2); // ambil bagian angkanya
            int num = Integer.parseInt(id);
            if (num > max) max = num;
        }

        int next = max + 1;
        return String.format("BS%03d", next); // UA001, UA002, dst
    }

    public ArrayList<BankSampah> loadData(String filepath) {
        daftarSemuaBankSampah.clear();
        File file = new File(filepath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            if (!file.exists()) {
                throw new IOException("File" + filepath + "tidak ada");
            }

            
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                String[] parts = line.split(delim);
                if (parts.length >= 2) {
                    daftarSemuaBankSampah.add(new BankSampah(parts[0], parts[1]));
                    // 0 = idBank
                    // 1 = nama bank
                }
            }
            return daftarSemuaBankSampah;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return daftarSemuaBankSampah;
        }
    }

    //Tulis data
    public void writeData(String filepath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {

            for (BankSampah bankSampah : daftarSemuaBankSampah) {
                String data = bankSampah.getIdBank() + "|" +
                        bankSampah.getNamaBank();

                writer.write(data);
                writer.newLine();
            }
            System.out.println("--- Data berhasil disimpan ke file ---");

        } catch (IOException e) {
            System.err.println("Error: Gagal menyimpan data ke file. " + e.getMessage());
        }
    }
}
