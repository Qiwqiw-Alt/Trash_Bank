package Controller;

import Service.SetoranPenyetorService;

public class SetoranPenyetorController {
    private static SetoranPenyetorService service = new SetoranPenyetorService(); 

    public SetoranPenyetorController() {
    }

    public static SetoranPenyetorService getService() {
        return service;
    }
}
