package Controller;

import Service.AdminHomePanelService;

public class AdminHomePanelController {
    private static AdminHomePanelService service = new AdminHomePanelService(); 

    public AdminHomePanelController() {
        // Kosongkan atau hapus constructor ini jika tidak dipakai
    }

    public static AdminHomePanelService getService() {
        return service;
    }
}
