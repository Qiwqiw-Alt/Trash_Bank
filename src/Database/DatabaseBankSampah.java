package Database;

import Model.BankSampah;

import java.io.*;
import java.util.ArrayList;

public class DatabaseBankSampah {
    private static ArrayList<BankSampah> daftarSemuaBankSampah = new ArrayList<BankSampah>();
    private static final String DATA_BANK_SAMPAH = "src\\Database\\DataBankSampah\\databanksampah.txt";

    public static void addBankSampah(BankSampah bankSampahBaru) { // untuk nambah bank sampah yang dipakai di SignIn
        daftarSemuaBankSampah.add(bankSampahBaru);
        writeData();
    }

    // filepath = src/Database/BankSampah/data.txt
    // Load/baca data sebelum Sign In dan Login
    static String delim = "\\|";

    public static String generateBankId() {
        int max = 0;

        for (BankSampah a : daftarSemuaBankSampah) {
            String id = a.getIdBank().substring(2); // ambil bagian angkanya
            int num = Integer.parseInt(id);
            if (num > max)
                max = num;
        }

        int next = max + 1;
        return String.format("BS%03d", next); // UA001, UA002, dst
    }

    public static ArrayList<BankSampah> loadData() {
        daftarSemuaBankSampah.clear();
        File file = new File(DATA_BANK_SAMPAH);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (!file.exists()) {
                throw new IOException("File" + DATA_BANK_SAMPAH + "tidak ada");
            }

            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                String[] parts = line.split(delim);
                if (parts.length >= 3) {
                    daftarSemuaBankSampah.add(new BankSampah(parts[0], parts[1], parts[2]));
                    // 0 = idBank
                    // 1 = nama bank
                    // 2 = alamat bank
                }
            }
            return daftarSemuaBankSampah;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return daftarSemuaBankSampah;
        }
    }

    // Tulis data
    public static void writeData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_BANK_SAMPAH))) {

            for (BankSampah bankSampah : daftarSemuaBankSampah) {
                String data = bankSampah.getIdBank() + "|" +
                        bankSampah.getNamaBank() + "|" +
                        bankSampah.getAlamat();

                writer.write(data);
                writer.newLine();
            }
            System.out.println("--- Data berhasil disimpan ke file ---");

        } catch (IOException e) {
            System.err.println("Error: Gagal menyimpan data ke file. " + e.getMessage());
        }
    }
}
