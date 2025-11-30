package Service;
import Database.DataBaseAdmin;
import Database.DatabasePenyetor;
import Model.Admin;
import Model.Penyetor;

import java.util.ArrayList;

public class LoginService {
    public boolean isUsernameTaken(String username) {
        ArrayList<Penyetor> penyetor = DatabasePenyetor.loadData();
        for (Penyetor u : penyetor) {
            if (u.getUsername().equals(username)) return true;
        }

        ArrayList<Admin> admin = DataBaseAdmin.loadData();
        for (Admin u : admin) {
            if (u.getUsername().equals(username)) return true;
        }
        return false;
    }

    public Object loginUser(String username, String password) {
        ArrayList<Admin> admins = DataBaseAdmin.loadData();
        for (Admin a : admins) {
            if (a.getUsername().equals(username) && a.getPassword().equals(password)) {
                return a;
            }
        }

        ArrayList<Penyetor> penyetors = DatabasePenyetor.loadData();
        for (Penyetor p : penyetors) {
            if (p.getUsername().equals(username) && p.getPassword().equals(password)) {
                return p;
            }
        }

        return null;
    }

    public ArrayList<Admin> getAllAdmins() {
        return DataBaseAdmin.loadData();
    }

    public ArrayList<Penyetor> getAllPenyetors() {
        return DatabasePenyetor.loadData();
    }
}
