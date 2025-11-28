package Database;

import Model.ItemTransaksi;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseItemTransaksi {
    private static final String DATA_ITEM_TRANSAKSI_GLOBAL = "src\\Database\\ItemTransaksi\\itemtransaksi.txt";
    private static final String DELIM = "\\|";

    public static String getFinalPath(){
        return DATA_ITEM_TRANSAKSI_GLOBAL;
    }

    public static void addItem(ItemTransaksi it) {
        addItem(it, DATA_ITEM_TRANSAKSI_GLOBAL);
    }
    
    public static ArrayList<ItemTransaksi> loadData() {
        return loadData(DATA_ITEM_TRANSAKSI_GLOBAL);
    }

    public static ArrayList<ItemTransaksi> loadData(String filePath) {
        ArrayList<ItemTransaksi> listHasil = new ArrayList<>();
        File file = new File(filePath);
        
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
                if (p.length >= 4) {
                    String idTrx = p[0];
                    String idSampah = p[1];
                    double hargaPerKg = Double.parseDouble(p[2]);
                    double berat = Double.parseDouble(p[3]);

                    listHasil.add(new ItemTransaksi(idTrx, idSampah, hargaPerKg, berat));
                }
            }

        } catch (IOException e) {
            System.out.println("Gagal load item transaksi: " + e.getMessage());
        }

        return listHasil;
    }

    public static void writeData(List<ItemTransaksi> listItem) {
        writeData(listItem, DATA_ITEM_TRANSAKSI_GLOBAL);
    }

    public static void writeData(List<ItemTransaksi> listItem, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {

            for (ItemTransaksi it : listItem) {
                bw.write(
                        it.getIdTransaksi() + "|" +
                        it.getIdSampah() + "|" +
                        it.getHargaPerKg() + "|" +
                        it.getBeratKg()
                );
                bw.newLine();
            }
            System.out.println("Item Transaksi tersimpan di: " + filePath);

        } catch (IOException e) {
            System.out.println("Gagal simpan item transaksi: " + e.getMessage());
        }
    }

    public static void addItem(ItemTransaksi it, String filePath) {
        ArrayList<ItemTransaksi> list = loadData(filePath);
        list.add(it);
        writeData(list, filePath);
    }
}