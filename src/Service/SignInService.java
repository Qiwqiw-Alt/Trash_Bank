package Service;

import Model.Admin;
import Model.BankSampah;
import Model.Penyetor;
import Model.User;

public class SignInService {



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
