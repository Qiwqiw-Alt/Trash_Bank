package Database;

import Model.ItemTransaksi;

import java.io.*;
import java.util.ArrayList;

public class DatabaseItemTransaksi {

    private ArrayList<ItemTransaksi> daftarItem = new ArrayList<>();
    private static final String DATA_TRANSAKSI = "src\\Database\\ItemTransaksi\\itemtransaksi.txt";
    private final String delim = "\\|";

    public void addItem(ItemTransaksi it) {
        daftarItem.add(it);
    }

    public ArrayList<ItemTransaksi> loadData() {
        daftarItem.clear();
        File file = new File(DATA_TRANSAKSI);

        try {
            if (!file.exists()) {
                return daftarItem;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(delim);
                if (p.length >= 4) {

                    String idTrx = p[0];
                    String idSampah = p[1];
                    double hargaPerKg = Double.parseDouble(p[2]);
                    double berat = Double.parseDouble(p[3]);

                    daftarItem.add(new ItemTransaksi(idTrx, idSampah, hargaPerKg, berat));
                }
            }

            br.close();

        } catch (IOException e) {
            System.out.println("Gagal load item transaksi: " + e.getMessage());
        }

        return daftarItem;
    }

    public void writeData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_TRANSAKSI))) {

            for (ItemTransaksi it : daftarItem) {
                bw.write(
                        it.getIdTransaksi() + "|" +
                                it.getIdSampah() + "|" +
                                it.getHargaPerKg() + "|" +
                                it.getBeratKg()
                );
                bw.newLine();
            }

        } catch (Exception e) {
            System.out.println("Gagal simpan item transaksi: " + e.getMessage());
        }
    }

    public ArrayList<ItemTransaksi> getDaftarItem() {
        return daftarItem;
    }
}
