package Service;

import Database.DataBaseAdmin;
import Database.DatabasePenyetor;
import Model.Admin;
import Model.Penyetor;

import java.util.ArrayList;

public class SignInService {

    public boolean isUsernameTaken(String username, String role) {
        ArrayList<Penyetor> penyetor = DatabasePenyetor.loadData("src\\Database\\Penyetor\\dataSemuaPenyetor.txt");
        for (Penyetor u : penyetor) {
            if (u.getUsername().equals(username)) return true;
        }
       
        ArrayList<Admin> admin = DataBaseAdmin.loadData();
        for (Admin u : admin) {
            if (u.getUsername().equals(username)) return true;
        }
        

        return false;
    }

    public void registerAdmin(String role, String nama, String user, String pass, String noHp) {
        DataBaseAdmin.loadData();
        String id = DataBaseAdmin.generateAdminId();
        Admin admin = new Admin(id, role, user, pass, nama, noHp);
        DataBaseAdmin.addAdmin(admin, DataBaseAdmin.getFinalPath());
    }

    public void registerPenyetor(String role, String nama, String user, String pass, String noHp) {
        DatabasePenyetor.loadData();
        String id = DatabasePenyetor.generatePenyetorId();
        Penyetor penyetor = new Penyetor(id, role, user, pass, nama, noHp);
        String filePathGlobal = "src\\Database\\Penyetor\\dataSemuaPenyetor.txt";
        DatabasePenyetor.addPenyetor(penyetor, filePathGlobal);
    }
}
