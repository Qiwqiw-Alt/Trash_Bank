package Database;

import Model.Admin;
import Model.Transaksi;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DatabaseTransaksi {

    private static ArrayList<Transaksi> daftarTransaksi;
    private static final String DATA_TRANSAKSI = "src\\database\\Transaksi\\logs.txt";
    private static final String delim = "\\|";


    public void addTransaksi(Transaksi t, String filePath) {
        daftarTransaksi.add(t);
        writeData(filePath);
    }

    public String generateTransaksiId() {
        int max = 0;

        for (Transaksi t : daftarTransaksi) {
            String angka = t.getIdTransaksi().substring(3); // TRX###
            int num = Integer.parseInt(angka);
            if (num > max) max = num;
        }

        return String.format("TRX%03d", max + 1);
    }

    public static ArrayList<Transaksi> loadData(){
        return loadData(DATA_TRANSAKSI);
    }

    public static ArrayList<Transaksi> loadData(String filePath) {
        daftarTransaksi.clear();
        File file = new File(filePath);

        try {
            if (!file.exists()) {
                return daftarTransaksi;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(delim);
                if (p.length >= 6) {

                    String idTrx = p[0];
                    String idPenyetor = p[1];
                    String idBank = p[2];
                    LocalDate tanggal = LocalDate.parse(p[3]);
                    double totalHarga = Double.parseDouble(p[4]);
                    int totalPoin = Integer.parseInt(p[5]);

                    // Buat transaksi tanpa item dulu
                    Transaksi trx = new Transaksi(idTrx, idPenyetor, idBank);

                    // Set nilai yang dihitung ulang
                    trx.setTanggal(tanggal);
                    trx.setTotalHarga(totalHarga);
                    trx.setTotalPoin(totalPoin);

                    daftarTransaksi.add(trx);
                }
            }

            br.close();

        } catch (IOException e) {
            System.out.println("Gagal load transaksi: " + e.getMessage());
        }

        return daftarTransaksi;
    }

    public static void writeData(){
        writeData(DATA_TRANSAKSI);
    }

    public static void writeData(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {

            for (Transaksi t : daftarTransaksi) {
                bw.write(
                        t.getIdTransaksi() + "|" +
                                t.getIdPenyetor() + "|" +
                                t.getIdBank() + "|" +
                                t.getTanggal() + "|" +
                                t.getTotalHarga() + "|" +
                                t.getTotalPoin()
                );
                bw.newLine();
            }

        } catch (Exception e) {
            System.out.println("Gagal simpan transaksi: " + e.getMessage());
        }
    }

    public ArrayList<Transaksi> getDaftarTransaksi() {
        return daftarTransaksi;
    }

}
