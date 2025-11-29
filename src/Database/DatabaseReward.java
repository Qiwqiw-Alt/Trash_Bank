package Database;

import Model.Reward;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

public class DatabaseReward {
    private static final String DATA_REWARD_GLOBAL = "src\\Database\\Reward\\reward.txt";
    private static String DELIM = "\\|";

    public static String generateRewardId() {
        ArrayList<Reward> list = loadData();
        
        int max = 0;
        for (Reward reward : list) {
            try {
                String id = reward.getIdReward().substring(1);
                int num = Integer.parseInt(id);
                if (num > max) max = num;
            } catch (Exception e) {
                continue;
            }
        }
        return String.format("R%03d", max + 1);
    }

    public static ArrayList<Reward> loadData() {
        return loadData(DATA_REWARD_GLOBAL);
    }
   
    public static ArrayList<Reward> loadData(String filePath) {
        ArrayList<Reward> listHasil = new ArrayList<>();
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
                if (data.length >= 5) {
                    Reward loadReward = new Reward(
                            data[0], // id
                            data[1], // nama hadiah
                            data[2], // deskripsi
                            Double.parseDouble(data[3]), // poin tukar (handle double/int safe)
                            Integer.parseInt(data[4]) // stok
                    );
                    listHasil.add(loadReward);
                }
            }
        } catch (IOException e) {
            System.out.println("Error Load Reward: " + e.getMessage());
        }
        return listHasil;
    }

   public static void writeData(ArrayList<Reward> listReward) {
        writeData(listReward, DATA_REWARD_GLOBAL);
    }

    public static void writeData(ArrayList<Reward> listReward, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            
            for (Reward reward : listReward) {
                String data = reward.getIdReward() + "|" +
                              reward.getNamaHadiah() + "|" +
                              reward.getDeskripsi() + "|" +
                              reward.getHargaTukar() + "|" + 
                              reward.getStok(); 
                              // \n dihapus di sini, diganti bw.newLine() agar support semua OS

                bw.write(data);
                bw.newLine();
            }
            System.out.println("--- Data Reward Berhasil disimpan ke file: " + filePath + " ---");

        } catch (IOException e) {
            System.out.println("Error Write Reward: " + e.getMessage());
        }
    }

    public static void addReward(Reward rewardBaru, String filePath) {
        ArrayList<Reward> list = loadData(filePath);
        list.add(rewardBaru);
        writeData(list, filePath);
    }

    public static void updateReward(Reward rewardBaru, String filePath) {
        ArrayList<Reward> list = loadData(filePath);
        boolean found = false;

        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getIdReward().equals(rewardBaru.getIdReward())){
                list.set(i, rewardBaru);
                found = true;
                break;
            }
        }

        if(found) {
            writeData(list, filePath);
        }
    }

    public static void deleteReward(String idReward, String filePath) {
        ArrayList<Reward> list = loadData(filePath);
        ArrayList<Reward> newList = new ArrayList<>();

        for (Reward r : list) {
            // Masukkan ke list baru KECUALI yang id-nya mau dihapus
            if (!r.getIdReward().equals(idReward)) {
                newList.add(r);
            }
        }

        writeData(newList, filePath);
    }
}