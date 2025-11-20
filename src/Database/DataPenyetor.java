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

    public ArrayList<Penyetor> loadData(String filepath) {
        daftarSemuaPenyetor.clear();
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
                    daftarSemuaPenyetor.add(new Penyetor(parts[0], parts[1], parts[2], parts[3]));
                    // 0 = username biasa
                    // 1 = nama admin -> buat login/sigin
                    // 2 = nomor hp
                    // 3 = password -> buat login/signin
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
