package Database;

import Model.Sampah;

import java.io.*;
import java.util.ArrayList;

public class DatabaseSampah {
    private static final String DATA_SAMPAH_GLOBAL = "src\\Database\\Sampah\\sampah.txt";
    private static final String DELIM = "\\|";

    public static String getFinalPath(){
        return DATA_SAMPAH_GLOBAL;
    }

    public String generateSampahId() {
        ArrayList<Sampah> listSampah = loadData();

        int max = 0;
        for (Sampah s : listSampah) {
            String angka = s.getIdSampah().substring(2);
            int num = Integer.parseInt(angka);
            if (num > max) max = num;
        }

        return String.format("SP%03d", max + 1);
    }

    public static ArrayList<Sampah> loadData() {
        return loadData(DATA_SAMPAH_GLOBAL);
    }

    public static ArrayList<Sampah> loadData(String filePath) {
        ArrayList<Sampah> listHasil = new ArrayList<>();
        File file = new File(filePath);

        // Buat folder jika belum ada
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (!file.exists()) {
                file.createNewFile();
                return listHasil;
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(DELIM);
                if (p.length >= 3) {
                    String id = p[0];
                    String jenis = p[1];
                    double harga = Double.parseDouble(p[2]);

                    listHasil.add(new Sampah(id, jenis, harga));
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal load sampah: " + e.getMessage());
        }

        return listHasil;
    }

    public static void writeData(ArrayList<Sampah> listSampah){
        writeData(listSampah, DATA_SAMPAH_GLOBAL);
    }

    public static void writeData(ArrayList<Sampah> listSampah, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {

            for (Sampah s : listSampah) {
                bw.write(s.getIdSampah() + "|" + s.getJenis() + "|" + s.getHargaPerKg());
                bw.newLine();
            }
            System.out.println("Data Sampah tersimpan di: " + filePath);

        } catch (IOException e) {
            System.out.println("Gagal simpan sampah: " + e.getMessage());
        }
    }

    public static void addSampah(Sampah s, String filePath) {
        ArrayList<Sampah> list = loadData(filePath);
        list.add(s);
        writeData(list, filePath);
    }

}
