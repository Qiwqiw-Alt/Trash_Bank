package Database;

import Model.Sampah;

import java.io.*;
import java.util.ArrayList;

public class DatabaseSampah {

    private ArrayList<Sampah> daftarSampah = new ArrayList<>();
    private static final String DATA_SAMPAH = "src/Database/Sampah/sampah.txt";
    private final String delim = "\\|";

    public void addSampah(Sampah s) {
        daftarSampah.add(s);
    }

    public String generateSampahId() {
        int max = 0;

        for (Sampah s : daftarSampah) {
            String angka = s.getIdSampah().substring(2);
            int num = Integer.parseInt(angka);
            if (num > max) max = num;
        }

        return String.format("SP%03d", max + 1);
    }

    public ArrayList<Sampah> loadData() {
        daftarSampah.clear();
        File file = new File(DATA_SAMPAH);

        try {
            if (!file.exists()) {
                return daftarSampah;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(delim);
                if (p.length >= 3) {
                    String id = p[0];
                    String jenis = p[1];
                    double harga = Double.parseDouble(p[2]);

                    daftarSampah.add(new Sampah(id, jenis, harga));
                }
            }

            br.close();

        } catch (IOException e) {
            System.out.println("Gagal load sampah: " + e.getMessage());
        }

        return daftarSampah;
    }

    public void writeData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_SAMPAH))) {

            for (Sampah s : daftarSampah) {
                bw.write(s.getIdSampah() + "|" + s.getJenis() + "|" + s.getHargaPerKg());
                bw.newLine();
            }

        } catch (Exception e) {
            System.out.println("Gagal simpan sampah: " + e.getMessage());
        }
    }

    public ArrayList<Sampah> getDaftarSampah() {
        return daftarSampah;
    }

}
