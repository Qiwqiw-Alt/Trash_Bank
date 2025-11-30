package Controller;

import Service.AdminHomePanelService;

public class AdminHomePanelController {
    private static AdminHomePanelService service = new AdminHomePanelService(); 

    public AdminHomePanelController() {
       
    }

    public static AdminHomePanelService getService() {
        return service;
    }
}
