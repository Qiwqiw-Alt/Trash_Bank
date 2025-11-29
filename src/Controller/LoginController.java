package Controller;

import Service.LoginService;

public class LoginController {

    private static LoginService service = new LoginService();

    public LoginController() {
    }

    public static LoginService getService () {
        return service;
    }
}
