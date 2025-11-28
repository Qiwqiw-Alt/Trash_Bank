package Controller;

import Service.ComplainService;

public class ComplainController {
    private static ComplainService service = new ComplainService(); 

    public ComplainController() {
        // Kosongkan atau hapus constructor ini jika tidak dipakai
    }

    public static ComplainService getService() {
        return service;
    }
}
