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
    private static ArrayList<Reward> listReward;
    private static final String DATA_REWARD = "src\\Database\\Reward\\reward.txt";
    static String delim = "\\|";

    public DatabaseReward() {
        this.listReward = loadData();
    }

    public static void addReward(Reward rewardBaru, String filePath) {
        listReward.add(rewardBaru);
        writeData(filePath);
    }

    public static String generateRewardId() {
        int max = 0;

        for (Reward reward : listReward) {
            String id = reward.getIdReward().substring(1);
            int num = Integer.parseInt(id);
            if (num > max) max = num;
        }

        int next = max + 1;
        return String.format("R%03d", next);
    }

    public static ArrayList<Reward> loadData(){
        return loadData(DATA_REWARD);
    }

    public static ArrayList<Reward> loadData(String filePath) {
        listReward.clear();
        File file = new File(filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (!file.exists()) {
                return listReward;
            }
            
            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {continue;}

                String[] data = line.split(delim);
                if (data.length >= 5) {
                    Reward loadReward = new Reward(
                            data[0], // untuk id
                            data[1], // nama hadiah
                            data[2], // deskripsi
                            (int) Double.parseDouble(data[3]), // poin tukar
                            Integer.parseInt(data[4]) // stok
                    );

                    listReward.add(loadReward);
                }

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return listReward;

    }

    public static void writeData(){
        writeData(DATA_REWARD);
    }

    public static void writeData(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            
            for (Reward reward : listReward) {
                String data = reward.getIdReward() + "|" +
                        reward.getNamaHadiah() + "|" +
                        reward.getDeskripsi() + "|" +
                        reward.getPoinTukar() + "|" + 
                        reward.getStok() + "\n";

                bw.write(data);
            }
            System.out.println("--- Data Reward Berhasil disimpan ke file ---");

        } catch (Exception e) {
            System.out.println("Error: Gagal menyimpan data reward ke file. " + e.getMessage());
        }
    }

}