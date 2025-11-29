package Service;

import java.util.ArrayList;

import Database.DatabaseComplain;
import Model.BankSampah;
import Model.Complain;

public class ComplainService {
    public ArrayList<Complain> daftarComplain(BankSampah bankSampah){
        ArrayList<Complain> hasil = DatabaseComplain.loadData(bankSampah.getFileComplain());
        return hasil;
    }

    public void updateComplain(String selectedIdComplain, Complain.Status newStatus, String tanggapan, BankSampah bankSampah){
        ArrayList<Complain> list = DatabaseComplain.loadData(bankSampah.getFileComplain());
        boolean found = false;

        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getIdComplain().equals(selectedIdComplain)){
                list.get(i).setStatus(newStatus);
                list.get(i).setTanggapanAdmin(tanggapan);
                found = true;
                break;
            }
        }

        if(found) {
            DatabaseComplain.writeData(list, bankSampah.getFileComplain());
        }
    }
}
