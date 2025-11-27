package Database;

import Model.Penyetor;

import java.io.*;
import java.util.ArrayList;

public class DatabasePenyetor {
    private static ArrayList<Penyetor> daftarSemuaPenyetor = new ArrayList<Penyetor>();
    private static final String DATA_PENYETOR = "src\\Database\\Penyetor\\dataSemuaPenyetor.txt";

    public static void addPenyetor(Penyetor penyetorBaru){ // untuk nambah penyetor yang dipakai di SignIn
        daftarSemuaPenyetor.add(penyetorBaru);
        writeData();
    }

    //filepath = src/Database/Penyetor/data.txt
    // Load/baca data sebelum Sign In dan Login
    static String delim = "\\|";

    public static String generatePenyetorId() {
        int max = 0;

        for (Penyetor a : daftarSemuaPenyetor) {
            String id = a.getIdPenyetor().substring(2); // ambil bagian angkanya
            int num = Integer.parseInt(id);
            if (num > max) max = num;
        }

        int next = max + 1;
        return String.format("UP%03d", next); // UP001, UP002, dst
    }

    public static ArrayList<Penyetor> loadData(){
        return loadData(DATA_PENYETOR);
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
