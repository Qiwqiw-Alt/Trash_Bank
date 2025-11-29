package Service;

import java.util.ArrayList;

import Database.DatabaseBankSampah;
import Model.BankSampah;


public class BankSampahService {
    public BankSampah getObjBankSampah(String idBankSampah ) {
        ArrayList<BankSampah> bankSampah = DatabaseBankSampah.loadData();
        for (BankSampah bs : bankSampah) {
            if (bs.getIdBank().equals(idBankSampah)) {
                      return bs;
            }
        }
        return null;
    }

    public ArrayList<BankSampah> getAllBankSampah() {
        return DatabaseBankSampah.loadData();
    }

}