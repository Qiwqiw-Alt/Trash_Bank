package Controller;

import Service.TransaksiService;

public class TransaksiController {
    private static TransaksiService service = new TransaksiService(); 

    public TransaksiController() {
    }

    public static TransaksiService getService() {
        return service;
    }
}
