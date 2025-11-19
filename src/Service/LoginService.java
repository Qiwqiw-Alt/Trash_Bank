package Service;

import Model.Admin;
import Model.Penyetor;
import Model.User;
import Model.BankSampah;

public class LoginService {
    private BankSampah bankSampah;

    public LoginService(BankSampah bankSampah) {
        this.bankSampah = bankSampah;
    }

    public User login(String username, String password) {
        Admin admin = bankSampah.getAdmin();
        if(admin.getUsername().equals(username) && admin.getPassword().equals(password)){
            return  admin;
        }

        for(Penyetor penyetor : bankSampah.getDaftarPenyetor()){
            if(penyetor.getUsername().equals(username) && penyetor.getPassword().equals(password)){
                return  penyetor;
            }
        }

        return  null;
    }
}
