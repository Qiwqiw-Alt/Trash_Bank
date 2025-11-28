package Database;

import Model.Transaksi;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTransaksi {
    private static final String DATA_TRANSAKSI_GLOBAL = "src\\database\\Transaksi\\logs.txt";
    private static final String DELIM = "\\|";

    public static String getFinalPath(){
        return DATA_TRANSAKSI_GLOBAL;
    }

    public static String generateTransaksiId() {
        return generateTransaksiId(DATA_TRANSAKSI_GLOBAL);
    }

    public static String generateTransaksiId(String filePath) {
        ArrayList<Transaksi> list = loadData(filePath);

        int max = 0;
        for (Transaksi t : list) {
            try {
                String angka = t.getIdTransaksi().substring(3); 
                int num = Integer.parseInt(angka);
                if (num > max) max = num;
            } catch (Exception e) {
                continue;
            }
        }
        return String.format("TRX%03d", max + 1);
    }

    public static ArrayList<Transaksi> loadData() {
        return loadData(DATA_TRANSAKSI_GLOBAL);
    }

    public static ArrayList<Transaksi> loadData(String filePath) {
        ArrayList<Transaksi> listHasil = new ArrayList<>();
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
                if (p.length >= 6) {
                    String idTrx = p[0];
                    String idPenyetor = p[1];
                    String idBank = p[2];
                    

                    LocalDate tanggal;
                    try {
                        tanggal = LocalDate.parse(p[3]);
                    } catch (Exception e) {
                        tanggal = LocalDate.now(); 
                    }
                    
                    double totalHarga = Double.parseDouble(p[4]);
                    int totalPoin = Integer.parseInt(p[5]);

                    // Buat objek
                    Transaksi trx = new Transaksi(idTrx, idPenyetor, idBank);
                    trx.setTanggal(tanggal);
                    trx.setTotalHarga(totalHarga);
                    trx.setTotalPoin(totalPoin);

                    listHasil.add(trx);
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal load transaksi: " + e.getMessage());
        }

        return listHasil;
    }

    public static void writeData(ArrayList<Transaksi> listTransaksi) {
        writeData(listTransaksi, DATA_TRANSAKSI_GLOBAL);
    }

    public static void writeData(ArrayList<Transaksi> listTransaksi, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {

            for (Transaksi t : listTransaksi) {
                bw.write(
                        t.getIdTransaksi() + "|" +
                        t.getIdPenyetor() + "|" +
                        t.getIdBank() + "|" +
                        t.getTanggal().toString() + "|" +
                        t.getTotalHarga() + "|" +
                        t.getTotalPoin()
                );
                bw.newLine();
            }
            System.out.println("Transaksi tersimpan di: " + filePath);

        } catch (IOException e) {
            System.out.println("Gagal simpan transaksi: " + e.getMessage());
        }
    }

    public static void addTransaksi(Transaksi t, String filePath) {
        ArrayList<Transaksi> list = loadData(filePath);
        list.add(t);
        writeData(list, filePath);
    }

}
