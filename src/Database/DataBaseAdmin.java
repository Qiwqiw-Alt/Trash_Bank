package Database;

import java.io.*;
import java.util.ArrayList;

import Model.Admin;

public class DataBaseAdmin {
    private static final String DATA_ADMIN_GLOBAL = "src\\Database\\Admin\\dataSemuaAdmin.txt";
    private static String DELIM = "\\|";

    public static String generateAdminId() {
        ArrayList<Admin> allData = loadData(DATA_ADMIN_GLOBAL);
        int max = 0;

        for (Admin a : allData) {
            try {
                String idStr = a.getIdAdmin().substring(2); // Ambil angka setelah "UP"
                int num = Integer.parseInt(idStr);
                if (num > max) max = num;
            } catch (NumberFormatException e) {
                continue; // Skip jika format ID salah
            }
        }

        return String.format("UA%03d", max + 1); // UA001, UA002, dst
    }

    public static ArrayList<Admin> loadData(){
        return loadData(DATA_ADMIN_GLOBAL);
    }

    public static ArrayList<Admin> loadData(String filePath) {
        ArrayList<Admin> listHasil = new ArrayList<>();
        File file = new File(filePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try {
            if (!file.exists()) {
                file.createNewFile();
                return listHasil; 
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(DELIM);
                if (parts.length >= 6) {
                    Admin adminBaru = new Admin(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);

                    if (parts.length > 6 && !parts[6].equalsIgnoreCase("null")) {
                        adminBaru.setIdBankSampah(parts[6]);
                    }
        
                    listHasil.add(adminBaru);
                }
            }
            br.close();
        } catch (IOException e) {
            System.err.println("Error Load Admin: " + e.getMessage());
        }

        return listHasil; 
    }

    public static void writeData(ArrayList<Admin> listAdmin){
        writeData(listAdmin, DATA_ADMIN_GLOBAL);
    }
    
    public static void writeData(ArrayList<Admin> listAdmin, String filePath){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (Admin admin : listAdmin) {
                String idBank = (admin.getIdBankSampah() == null) ? "null" : admin.getIdBankSampah();

                String data = admin.getIdAdmin() + "|" +
                              admin.getRole() + "|" +
                              admin.getUsername() + "|" +
                              admin.getPassword() + "|" +
                              admin.getNamaAdmin() + "|" +
                              admin.getNohp() + "|" +
                              idBank;

                writer.write(data);
                writer.newLine();
            }
            System.out.println("Data Admin berhasil disimpan ke: " + filePath);
        } catch (IOException e) {
            System.err.println("Error: Gagal menyimpan data ke file. " + e.getMessage());
        }
    }

    public static void addMin(Admin newAdmin){
        addMin(newAdmin, DATA_ADMIN_GLOBAL);
    }

    public static void addMin(Admin newAdmin, String filePath){
        ArrayList<Admin> currentList = loadData(filePath);
        currentList.add(newAdmin);
        writeData(currentList, filePath);
    }

}
