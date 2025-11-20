package Database;

import java.io.*;
import java.util.ArrayList;
import Model.Admin;
import Model.Penyetor;

public class DataAdmin {
    private ArrayList<Admin> daftarSemuaAdmin = new ArrayList<Admin>();

    public void addAdmin(Admin adminBaru){ // untuk nambah admin yang dipakai di SignIn
        daftarSemuaAdmin.add(adminBaru);
    }

    //filepath = src/Database/Admin/data.txt
    // Load/baca data sebelum Sign In dan Login
    String delim = "\\|";

    public ArrayList<Admin> loadData(String filepath) {
        daftarSemuaAdmin.clear();
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
                    daftarSemuaAdmin.add(new Admin(parts[0], parts[1], parts[2], parts[3]));
                    // 0 = username biasa
                    // 1 = nama admin -> buat login/sigin
                    // 2 = nomor hp
                    // 3 = password -> buat login/signin
                }
            }
            return daftarSemuaAdmin;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return daftarSemuaAdmin;
        }
    }

    //Tulis data
    public void writeData(String filepath){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {

            for (Admin admin : daftarSemuaAdmin) {
                String data = admin.getUsername() + "|" +
                        admin.getNamaAdmin() + "|" +
                        admin.getNohp() + "|" +
                        admin.getPassword();

                writer.write(data);
                writer.newLine();
            }
            System.out.println("--- Data berhasil disimpan ke file ---");

        } catch (IOException e) {
            System.err.println("Error: Gagal menyimpan data ke file. " + e.getMessage());
        }
    }

}
