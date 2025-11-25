package Controller;

import Service.SignInService;

public class SignInController {

    private static SignInService service;

    public SignInController() {
        this.service = new SignInService();
    }

    public static SignInService getService() {
        return service;
    }
}
