package Controller;

import Service.LoginService;

public class LoginController {

    private static LoginService loginService;

    public LoginController() {
        this.loginService = new LoginService();
    }

    public static Object login(String username, String password) {
        return loginService.loginUser(username, password);
    }
}
