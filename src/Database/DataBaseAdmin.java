package Database;

import java.io.*;
import java.util.ArrayList;

import Model.Admin;

public class DataBaseAdmin {
    private static ArrayList<Admin> daftarSemuaAdmin = new ArrayList<Admin>();
    // private static Scanner input = new Scanner(System.in);

    public static void addAdmin(Admin adminBaru){ // untuk nambah admin yang dipakai di SignIn
        daftarSemuaAdmin.add(adminBaru);
        writeData("src/Database/Admin/data.txt");
    }

    //filepath = src/Database/Admin/data.txt
    // Load/baca data sebelum Sign In dan Login
    static String delim = "\\|";

    public static String generateAdminId() {
        int max = 0;

        for (Admin a : daftarSemuaAdmin) {
            String id = a.getIdAdmin().substring(2); // ambil bagian angkanya
            int num = Integer.parseInt(id);
            if (num > max) max = num;
        }

        int next = max + 1;
        return String.format("UA%03d", next); // UA001, UA002, dst
    }

    public static ArrayList<Admin> loadData(String filepath) {
        daftarSemuaAdmin.clear();
        File file = new File(filepath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            if (!file.exists()) {
                System.out.println("File tidak ditemukan, membuat file baru.");
                file.createNewFile();
                return daftarSemuaAdmin;
            }
            
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if(line.isEmpty()) continue;

                String[] parts = line.split(delim);
                if (parts.length >= 7) {
                    Admin adminBaru = new Admin(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);

                    if(!parts[5].equalsIgnoreCase("null")){
                        adminBaru.setIdBankSampah(parts[5]);
                    }
                    daftarSemuaAdmin.add(adminBaru);
                    // 0 = ID
                    // 1 = role
                    // 2 = username biasa
                    // 3 = password
                    // 4 = nama admin
                    // 5 = no hp
                }
            }
            return daftarSemuaAdmin;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return daftarSemuaAdmin;
        }
    }

    //Tulis data
    public static void writeData(String filepath){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {

            for (Admin admin : daftarSemuaAdmin) {
                String data = admin.getIdAdmin() + "|" +
                        admin.getRole() + "|" +
                        admin.getUsername() + "|" +
                        admin.getPassword() + "|" +
                        admin.getNamaAdmin() + "|" +
                        admin.getNohp() + "|" +
                        (admin.getIdBankSampah() == null ? "null" : admin.getIdBankSampah());

                writer.write(data);
                writer.newLine();
            }
            System.out.println("--- Data berhasil disimpan ke file ---");

        } catch (IOException e) {
            System.err.println("Error: Gagal menyimpan data ke file. " + e.getMessage());
        }
    }
}
