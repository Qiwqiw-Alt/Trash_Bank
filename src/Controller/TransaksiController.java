package Controller;

import Service.TransaksiService;

public class TransaksiController {
    private static TransaksiService service = new TransaksiService(); 

    public TransaksiController() {
        // Kosongkan atau hapus constructor ini jika tidak dipakai
    }

    public static TransaksiService getService() {
        return service;
    }
}
