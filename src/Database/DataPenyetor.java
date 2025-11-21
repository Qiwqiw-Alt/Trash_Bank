package Database;

import Model.Admin;
import Model.Penyetor;

import java.io.*;
import java.util.ArrayList;

public class DataPenyetor {
    private ArrayList<Penyetor> daftarSemuaPenyetor = new ArrayList<Penyetor>();

    public void addPenyetor(Penyetor penyetorBaru){ // untuk nambah penyetor yang dipakai di SignIn
        daftarSemuaPenyetor.add(penyetorBaru);
    }

    //filepath = src/Database/Penyetor/data.txt
    // Load/baca data sebelum Sign In dan Login
    String delim = "\\|";

    public String generatePenyetorId() {
        int max = 0;

        for (Penyetor a : daftarSemuaPenyetor) {
            String id = a.getIdPenyetor().substring(2); // ambil bagian angkanya
            int num = Integer.parseInt(id);
            if (num > max) max = num;
        }

        int next = max + 1;
        return String.format("UP%03d", next); // UP001, UP002, dst
    }

    public ArrayList<Penyetor> loadData(String filepath) {
        daftarSemuaPenyetor.clear();
        File file = new File(filepath);

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
                if (parts.length >= 6) {
                    Penyetor penyetorBaru = new Penyetor(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    // 0 = ID
                    // 1 = username biasa
                    // 2 = password
                    // 3 = nama lengkap
                    // 4 = no hp
                    if(!parts[5].equalsIgnoreCase("null")){
                        penyetorBaru.setIdBankSampah(parts[5]);
                    }
                    daftarSemuaPenyetor.add(penyetorBaru);
                }
            }
            return daftarSemuaPenyetor;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return daftarSemuaPenyetor;
        }
    }

    //Tulis data
    public void writeData(String filepath){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for (Penyetor penyetor : daftarSemuaPenyetor) {
                String data = penyetor.getIdPenyetor() + "|" +
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
}
