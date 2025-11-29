package Database;

import Model.TransaksiJoin;
import Model.TransaksiJoin.Status;

import java.io.*;
import java.util.ArrayList;

public class DatabaseRequestJoin {
    private static final String DATA_TRANSAKSI_JOIN_GLOBAL = "src\\Database\\Reward\\reward.txt";
    private static String DELIM = "\\|";

    public static String generateRewardId() {
        ArrayList<TransaksiJoin> list = loadData();
        
        int max = 0;
        for (TransaksiJoin join : list) {
            try {
                String id = join.getIdTransaksiJoin().substring(3);
                int num = Integer.parseInt(id);
                if (num > max) max = num;
            } catch (Exception e) {
                continue;
            }
        }
        return String.format("TRJ%03d", max + 1);
    }

    public static ArrayList<TransaksiJoin> loadData() {
        return loadData(DATA_TRANSAKSI_JOIN_GLOBAL);
    }
   
    public static ArrayList<TransaksiJoin> loadData(String filePath) {
        ArrayList<TransaksiJoin> listHasil = new ArrayList<>();
        File file = new File(filePath);
        
        // Buat folder parent jika belum ada
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

                String[] data = line.split(DELIM);
                if (data.length >= 4) {
                    TransaksiJoin loadTransaksiJoin = new TransaksiJoin(
                            data[0], // id
                            data[1], // nama hadiah
                            data[2] // deskripsi
                    );

                    try {
                        loadTransaksiJoin.setStatus(Status.valueOf(data[3])); 
                    } catch (Exception e) {
                        loadTransaksiJoin.setStatus(Status.PENDING); // Default jika error
                    }

                    listHasil.add(loadTransaksiJoin);
                }
            }
        } catch (IOException e) {
            System.out.println("Error Load Reward: " + e.getMessage());
        }
        return listHasil;
    }

   public static void writeData(ArrayList<TransaksiJoin> listTransaksiJoin) {
        writeData(listTransaksiJoin, DATA_TRANSAKSI_JOIN_GLOBAL);
    }

    public static void writeData(ArrayList<TransaksiJoin> listTransaksiJoin, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            
            for (TransaksiJoin transaksiJoin : listTransaksiJoin) {
                String data = transaksiJoin.getIdTransaksiJoin() + "|" +
                              transaksiJoin.getIdPenyetor() + "|" +
                              transaksiJoin.getbankId() + "|" +
                              transaksiJoin.getStatusRequest().name(); 
                              
                bw.write(data);
                bw.newLine();
            }
            System.out.println("--- Data Reward Berhasil disimpan ke file: " + filePath + " ---");

        } catch (IOException e) {
            System.out.println("Error Write Reward: " + e.getMessage());
        }
    }

    public static void addReward(TransaksiJoin TransaksiJoinBaru, String filePath) {
        ArrayList<TransaksiJoin> list = loadData(filePath);
        list.add(TransaksiJoinBaru);
        writeData(list, filePath);
    }

    public static void updateTransaksiJoin(TransaksiJoin transaksiJoinBaru, String filePath) {
        ArrayList<TransaksiJoin> list = loadData(filePath);
        boolean found = false;

        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getIdTransaksiJoin().equals(transaksiJoinBaru.getIdTransaksiJoin())){
                list.set(i, transaksiJoinBaru);
                found = true;
                break;
            }
        }

        if(found) {
            writeData(list, filePath);
        }
    }

    public static void deleteTransaksiJoin(String idTransaksiJoin, String filePath) {
        ArrayList<TransaksiJoin> list = loadData(filePath);
        ArrayList<TransaksiJoin> newList = new ArrayList<>();

        for (TransaksiJoin r : list) {
            // Masukkan ke list baru KECUALI yang id-nya mau dihapus
            if (!r.getIdTransaksiJoin().equals(idTransaksiJoin)) {
                newList.add(r);
            }
        }

        writeData(newList, filePath);
    }

    public static void updateStatus(String idPenyetor, TransaksiJoin.Status statusBaru, String filePath) {
        // 1. Load semua
        ArrayList<TransaksiJoin> list = loadData(filePath);
        boolean found = false;

        // 2. Cari & Ubah
        for (TransaksiJoin tj : list) {
            if (tj.getIdPenyetor().equals(idPenyetor)) {
                tj.setStatus(statusBaru);
                found = true;
                // Jangan break jika satu user bisa request berkali-kali (history), 
                // tapi kalau 1 user 1 request, boleh break.
            }
        }

        // 3. Simpan
        if (found) {
            writeData(list, filePath);
        }
    }
}
