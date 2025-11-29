package Service;

import java.util.ArrayList;

import Database.DatabaseBankSampah;
import Database.DatabaseTransaksi;
import Model.BankSampah;
import Model.Complain;
import Model.Penyetor;
import Model.Transaksi;

public class BankSampahService {
    public BankSampah getObjBankSampah(String idBankSampah) {
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

    public int getTotalSetoran(Penyetor user) {

        BankSampah bankUser = getObjBankSampah(user.getIdBankSampah());
        if (bankUser == null) {
            return 0;
        }
        ArrayList<Transaksi> allTransaksi = DatabaseTransaksi.loadData(bankUser.getFileTransaksi());
        int result = 0;
        for (Transaksi trx : allTransaksi) {
            if (trx.getIdPenyetor().equals(user.getIdPenyetor())) {
                result++;
            }
        }
        return result;
    }

}