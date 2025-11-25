package Database;

import Model.Reward;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DatabaseReward {
    private ArrayList<Reward> listReward;
    private static final String DATA_REWARD = "src\\Database\\Reward\\reward.txt";
    String delim = "\\|";

    public DatabaseReward() {
        this.listReward = loadData();
    }

    public void addReward(Reward rewardBaru) {
        listReward.add(rewardBaru);
        saveData();
    }

    public String generateRewardId() {
        int max = 0;

        for (Reward reward : listReward) {
            String id = reward.getIdReward().substring(1);
            int num = Integer.parseInt(id);
            if (num > max) max = num;
        }

        int next = max + 1;
        return String.format("R%03d", next);

    }


    ArrayList<Reward> loadData() {
        ArrayList<Reward> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_REWARD))) {
            String line;
            while ((line = br.readLine()) != null) {

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
            return list;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return list;
        }
    }

    public void saveData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_REWARD))) {
            
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
