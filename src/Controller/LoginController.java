package Controller;

import Model.Admin;
import Model.Penyetor;
import Model.User;
import Service.LoginService;
import View.LoginView;

public class LoginController {
    private LoginView view;
    private LoginService service;

    public LoginController(LoginView view, LoginService service){
        this.view = view;
        this.service = service;
    }

    public void login() {
        // String username = view.getUsernameInput();
        // String password = view.getPasswordInput();

        // User user = service.login(username, password);

//        if (user == null) {
//            view.showMessage("Login gagal! Username atau password salah.");
//        } else if (user instanceof Admin) {
//            view.showMessage("Login sebagai Admin berhasil!");
//            view.moveToAdminDashboard();
//        } else if (user instanceof Penyetor) {
//            view.showMessage("Login sebagai Penyetor berhasil!");
//            view.moveToUserDashboard();
//        }
    }
}
