package Controller;

import Service.SignInService;

public class SignInController {
    private static SignInService service = new SignInService(); 

    public SignInController() {

    }

    public static SignInService getService() {
        return service;
    }
}