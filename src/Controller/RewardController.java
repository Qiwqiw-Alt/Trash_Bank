package Controller;

import Service.RewardService;

public class RewardController {
    private static RewardService rewardService = new RewardService();

    public RewardController() {
    }

    public static RewardService getService() {
        return rewardService;
    }

    public static void tambahReward(String idBank, String nama, String deskripsi, double poin, int stok) {
        rewardService.registerReward(idBank, nama, deskripsi, poin, stok);
    }
}
