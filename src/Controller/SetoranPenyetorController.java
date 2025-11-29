package Controller;

import Service.SetoranPenyetorService;

public class SetoranPenyetorController {
    private static SetoranPenyetorService service = new SetoranPenyetorService(); 

    public SetoranPenyetorController() {
        // Kosongkan atau hapus constructor ini jika tidak dipakai
    }

    public static SetoranPenyetorService getService() {
        return service;
    }
}
