package Database;

import Model.Penyetor;

import java.io.*;
import java.util.ArrayList;

public class DatabasePenyetor {
    private static String DELIM = "\\|";
    private static final String DATA_PENYETOR = "src\\Database\\Penyetor\\dataSemuaPenyetor.txt";

    // public static void addPenyetor(Penyetor penyetorBaru, String filePath){ // untuk nambah penyetor yang dipakai di SignIn
    //     daftarSemuaPenyetor.add(penyetorBaru);
    //     writeData(filePath);
    // }

    // public static String getFinalPath(){
    //     return DATA_PENYETOR;
    // }

    public static String generatePenyetorId() {
        ArrayList<Penyetor> allData = loadData(DATA_PENYETOR);
        int max = 0;

        for (Penyetor p : allData) {
            try {
                String idStr = p.getIdPenyetor().substring(2); // Ambil angka setelah "UP"
                int num = Integer.parseInt(idStr);
                if (num > max) max = num;
            } catch (NumberFormatException e) {
                continue; 
            }
        }

        return String.format("UP%03d", max + 1); // UP001, UP002, dst
    }

    public static ArrayList<Penyetor> loadData(String filePath) {
        daftarSemuaPenyetor.clear();
        File file = new File(filePath);

        try {
            if (!file.exists()) {
                System.out.println("File tidak ditemukan, membuat file baru.");
                file.createNewFile();
                return daftarSemuaPenyetor;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                String[] parts = line.split(delim);
                if (parts.length >= 7) {
                    Penyetor penyetorBaru = new Penyetor(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                    
                    if(!parts[6].equalsIgnoreCase("null")){
                        penyetorBaru.setIdBankSampah(parts[6]);
                    }
                    daftarSemuaPenyetor.add(penyetorBaru);
                }
            }
            br.close();
            return daftarSemuaPenyetor;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return daftarSemuaPenyetor;
        }
    }

    //Tulis data
    public static void writeData(){
        writeData(DATA_PENYETOR);
    }

    public static void writeData(String filePath){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Penyetor penyetor : daftarSemuaPenyetor) {
                String data = penyetor.getIdPenyetor() + "|" +
                        penyetor.getRole() + "|" +
                        penyetor.getUsername() + "|" +
                        penyetor.getPassword() + "|" +
                        penyetor.getNamaLengkap() + "|" +
                        penyetor.getNoHp() + "|" +
                        (penyetor.getIdBankSampah() == null ? "null" : penyetor.getIdBankSampah());;

                writer.write(data);
                writer.newLine();
            }
            System.out.println("--- Data berhasil disimpan ke file ---");

        } catch (IOException e) {
            System.err.println("Error: Gagal menyimpan data ke file. " + e.getMessage());
        }
    }

    public static boolean assignUserToBank(String userId, String idBank) {
        ArrayList<Penyetor> list = loadData(); // Load global data
        boolean found = false;
        
        for (Penyetor p : list) {
            if (p.getIdPenyetor().equals(userId)) {
                p.setIdBankSampah(idBank); // UPDATE ID BANK DI SINI
                found = true;
                break;
            }
        }
        
        if (found) {
            writeData(); // SIMPAN PERUBAHAN KE FILE GLOBAL
            return true;
        }
        return false;
    }
}
