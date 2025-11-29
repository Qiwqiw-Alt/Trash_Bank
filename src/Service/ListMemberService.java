package Service;

import Model.Admin;
import Model.Penyetor;

import java.util.ArrayList;

import Database.DatabasePenyetor;
import Database.DataBaseAdmin;

public class ListMemberService {
     public boolean isUserAvilable(String userID){
        ArrayList<Penyetor> penyetor = DatabasePenyetor.loadData();
        for (Penyetor u : penyetor) {
            if (u.getIdPenyetor().equals(userID) && u.getIdBankSampah() == null) return true;
        }

        ArrayList<Admin> admin = DataBaseAdmin.loadData();
        for (Admin u : admin) {
            if (u.getIdAdmin().equals(userID) && u.getIdBankSampah() == null) return true;
        }
        return false;
    }

    public Object getUserById(String userId){
        ArrayList<Admin> admins = DataBaseAdmin.loadData();
        for (Admin a : admins) {
            if (a.getIdAdmin().equals(userId)) {
                return a;
            }
        }

        // cek penyetor
        ArrayList<Penyetor> penyetors = DatabasePenyetor.loadData();
        for (Penyetor p : penyetors) {
            if (p.getIdPenyetor().equals(userId)) {
                return p;
            }
        }

        return null;
    }
}
