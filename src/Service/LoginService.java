package Service;

import Database.DataBaseAdmin;
import Database.DatabasePenyetor;
import Model.Admin;
import Model.Penyetor;

import java.util.ArrayList;

public class LoginService {
    private final String ADMIN_FILE = "src/Database/Admin/data.txt";
    private final String PENYETOR_FILE = "src/Database/Penyetor/data.txt";

    public boolean isUsernameTaken(String username) {
        ArrayList<Penyetor> penyetor = DatabasePenyetor.loadData(PENYETOR_FILE);
        for (Penyetor u : penyetor) {
            if (u.getUsername().equals(username)) return true;
        }

        ArrayList<Admin> admin = DataBaseAdmin.loadData(ADMIN_FILE);
        for (Admin u : admin) {
            if (u.getUsername().equals(username)) return true;
        }
        return false;
    }

    public Object loginUser(String username, String password) {
        // cek admin
        ArrayList<Admin> admins = DataBaseAdmin.loadData(ADMIN_FILE);
        for (Admin a : admins) {
            if (a.getUsername().equals(username) && a.getPassword().equals(password)) {
                return a;
            }
        }

        // cek penyetor
        ArrayList<Penyetor> penyetors = DatabasePenyetor.loadData(PENYETOR_FILE);
        for (Penyetor p : penyetors) {
            if (p.getUsername().equals(username) && p.getPassword().equals(password)) {
                return p;
            }
        }

        return null;
    }

    public ArrayList<Admin> getAllAdmins() {
        return DataBaseAdmin.loadData(ADMIN_FILE);
    }

    public ArrayList<Penyetor> getAllPenyetors() {
        return DatabasePenyetor.loadData(PENYETOR_FILE);
    }
}
