package Service;

import Database.DataBaseAdmin;
import Database.DatabasePenyetor;
import Model.Admin;
import Model.Penyetor;
import Model.User;

import java.util.ArrayList;
import java.util.List;

public class SignInService {

    public boolean isUsernameTaken(String username) {
        ArrayList<Penyetor> penyetor = DatabasePenyetor.loadData("src/Database/Penyetor/data.txt");
        for (Penyetor u : penyetor) {
            if (u.getUsername().equals(username)) return true;
        }

        ArrayList<Admin> admin = DataBaseAdmin.loadData("src/Database/Admin/data.txt");
        for (Admin u : admin) {
            if (u.getUsername().equals(username)) return true;
        }

        return false;
    }

    public void registerAdmin(String role, String nama, String user, String pass, String noHp) {
        String id = DataBaseAdmin.generateAdminId();
        Admin admin = new Admin(id, role, user, pass, nama, noHp);
        DataBaseAdmin.addAdmin(admin);
    }

    public void registerPenyetor(String role, String nama, String user, String pass, String noHp) {
        String id = DatabasePenyetor.generatePenyetorId();
        Penyetor penyetor = new Penyetor(id, role, user, pass, nama, noHp);
        DatabasePenyetor.addPenyetor(penyetor);
    }
}
