package Controller;

import Service.SignInService;

public class SignInController {

    private SignInService service;

    public static SignInController() {
        this.service = new SignInService();
    }

    public static SignInService getService() {
        return service;
    }
}
