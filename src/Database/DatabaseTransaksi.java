package Database;

import Model.Transaksi.Status;
import Model.Transaksi;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

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
                if (p.length >= 7) {
                    String idTrx = p[0];
                    String idPenyetor = p[1];
                    String idBank = p[2];

                    Transaksi trx = new Transaksi(idTrx, idPenyetor, idBank);

                    try {
                        trx.setStatus(Status.valueOf(p[3])); 
                    } catch (Exception e) {
                        trx.setStatus(Status.PENDING); // Default jika error
                    }
                    
                    try {
                        trx.setTanggal(LocalDate.parse(p[4]));
                    } catch (Exception e) {
                        trx.setTanggal(LocalDate.now()); 
                    }
                    
                    double totalHarga = Double.parseDouble(p[5]);
                    double totalBerat = Double.parseDouble(p[6]);

                    // Buat objek
                    
                    trx.setTotalHarga(totalHarga);
                    trx.setTotalBerat(totalBerat);

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
                        t.getStatus().name() + "|" +
                        t.getTanggal().toString() + "|" +
                        t.getTotalHarga() + "|" +
                        t.getTotalBerat()
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
