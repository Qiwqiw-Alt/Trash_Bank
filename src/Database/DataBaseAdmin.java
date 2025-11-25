package Database;

import java.io.*;
import java.util.ArrayList;

import Model.Admin;

public class DataBaseAdmin {
    private static ArrayList<Admin> daftarSemuaAdmin = new ArrayList<Admin>();
    private static final String DATA_ADMIN = "src\\Database\\Admin\\data.txt";
    // private static Scanner input = new Scanner(System.in);

    public static void addAdmin(Admin adminBaru){ // untuk nambah admin yang dipakai di SignIn
        daftarSemuaAdmin.add(adminBaru);
        writeData();
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

    public static ArrayList<Admin> loadData() {
        daftarSemuaAdmin.clear();
        File file = new File(DATA_ADMIN);

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
                if (parts.length >= 6) {
                    Admin adminBaru = new Admin(parts[0], parts[1], parts[2], parts[3], parts[4]);

                    if(!parts[5].equalsIgnoreCase("null")){
                        adminBaru.setIdBankSampah(parts[5]);
                    }
                    daftarSemuaAdmin.add(adminBaru);
                    // 0 = ID
                    // 1 = username biasa
                    // 2 = password
                    // 3 = nama admin
                    // 4 = no hp
                }
            }
            return daftarSemuaAdmin;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return daftarSemuaAdmin;
        }
    }

    //Tulis data
    public static void writeData(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_ADMIN))) {

            for (Admin admin : daftarSemuaAdmin) {
                String data = admin.getIdAdmin() + "|" +
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
