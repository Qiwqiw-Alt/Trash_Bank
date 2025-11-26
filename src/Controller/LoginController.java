package Controller;

import Service.LoginService;

public class LoginController {

    private static LoginService loginService = new LoginService();

    public LoginController() {
    }

    public static Object login(String username, String password) {
        return loginService.loginUser(username, password);
    }

    public static boolean isUserAvilable(String userID){
        return loginService.isUserAvilable(userID);
    }
}
