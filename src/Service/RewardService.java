package Service;

import java.util.ArrayList;
import Database.DatabaseReward;
import Model.Reward;

public class RewardService {
    private String getFilePath(String idBank) {
        return "src\\Database\\Reward\\dfreward_" + idBank + ".txt";
    }

    public boolean isRewardAvailable(String rewardName, String idBank) {
        ArrayList<Reward> reward = DatabaseReward.loadData("src\\Database\\Reward\\dfreward_" + idBank + ".txt");
        for (Reward r : reward) {
            if (r.getNamaHadiah().equals(rewardName)) return true;
        }
        return false;
    }

    public void registerReward(String idBank, String rewardName, String rewardDesc, double poin, int stok) {
        String path = getFilePath(idBank);
        DatabaseReward.loadData(path);
        String idReward = DatabaseReward.generateRewardId();
        Reward newReward = new Reward(idReward, rewardName, rewardDesc, poin, stok);
        DatabaseReward.addReward(newReward, path);
    }
}
