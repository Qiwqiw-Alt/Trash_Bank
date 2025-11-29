package Controller;

import Service.RewardService;

public class RewardController {
    private static RewardService service = new RewardService();

    public RewardController() {
    }

    public static RewardService getService() {
        return service;
    }
}
