package Database;

import Model.BankSampah;

import java.io.*;
import java.util.ArrayList;


public class DatabaseBankSampah {
    private static final String DATA_BANK_SAMPAH_GLOBAL = "src\\Database\\DataBankSampah\\databanksampah.txt";
    private static String DELIM = "\\|";

    public static String generateBankId() {
        // 1. Load data terbaru dari file (bukan dari memori static)
        ArrayList<BankSampah> allData = loadData(); 
        
        int max = 0;
        for (BankSampah b : allData) {
            try {
                // Ambil angka setelah "BS"
                String idStr = b.getIdBank().substring(2); 
                int num = Integer.parseInt(idStr);
                if (num > max) max = num;
            } catch (Exception e) {
                continue; 
            }
        }
        return String.format("BS%03d", max + 1); // BS001, BS002, dst
    }

    public static ArrayList<BankSampah> loadData() {
        ArrayList<BankSampah> listHasil = new ArrayList<>();
        File file = new File(DATA_BANK_SAMPAH_GLOBAL);

        File parentDir = file.getParentFile();
       
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try {
            if (!file.exists()) {
                file.createNewFile();
                return listHasil; // Return list kosong, jangan throw error
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(DELIM);
                if (parts.length >= 3) {
                    listHasil.add(new BankSampah(parts[0], parts[1], parts[2]));
                    // 0 = idBank
                    // 1 = nama bank
                    // 2 = alamat bank
                }
            }
            br.close();
        } catch (IOException e) {
            System.err.println("Error Load Bank: " + e.getMessage());
        }
        
        return listHasil;
    }

    // Tulis data
    public static void writeData(ArrayList<BankSampah> listBank) {
        writeData(listBank, DATA_BANK_SAMPAH_GLOBAL);
    }

    public static void writeData(ArrayList<BankSampah> listBank, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_BANK_SAMPAH_GLOBAL))) {

            for (BankSampah bankSampah : listBank) {
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

    public static void addBankSampah(BankSampah bankSampahBaru){ 
        ArrayList<BankSampah> currentList = loadData();
        currentList.add(bankSampahBaru);
        writeData(currentList);
    }
}
