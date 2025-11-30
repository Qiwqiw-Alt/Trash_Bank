package Service;

import Database.DatabaseBankSampah;
import Model.BankSampah;

public class CreateBankSampahService {
    public void addBankSampah(String idBank, String nama, String alamat){
        BankSampah newBank = new BankSampah(idBank, nama, alamat);
        DatabaseBankSampah.addBankSampah(newBank);
    }
}
