package Controller;

import Service.SignInService;

public class SignInController {
    private static SignInService service = new SignInService(); 

    public SignInController() {
        // Kosongkan atau hapus constructor ini jika tidak dipakai
    }

    public static SignInService getService() {
        return service;
    }
}
