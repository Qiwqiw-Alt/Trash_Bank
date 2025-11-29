package Service;

import java.util.ArrayList;

import Database.DatabaseItemTransaksi;
import Database.DatabaseTransaksi;
import Model.BankSampah;
import Model.ItemTransaksi;
import Model.Transaksi;

public class SetoranPenyetorService {

    public ArrayList<Transaksi> daftarTransaksis(BankSampah bankSampah){
        return DatabaseTransaksi.loadData(bankSampah.getFileTransaksi());
    }

    public ArrayList<ItemTransaksi> daftarItemTransaksi(BankSampah bankSampah){
        return DatabaseItemTransaksi.loadData(bankSampah.getFileItemTransaksi());
    }

    public Transaksi getTrxById(String idTrx, BankSampah bankSampah){
        ArrayList<Transaksi> daftarTransaksi = DatabaseTransaksi.loadData(bankSampah.getFileTransaksi());
        for(Transaksi trs : daftarTransaksi){
            if(trs.getIdTransaksi().equals(idTrx));
            return trs;
        }
        return null;
    }

    public void updateTransaksiStatus(Transaksi trxUpdate, BankSampah bank) {
        ArrayList<Transaksi> list = daftarTransaksis(bank);
        boolean found = false;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdTransaksi().equals(trxUpdate.getIdTransaksi())) {
                list.get(i).setStatus(trxUpdate.getStatus()); // Update status
                found = true;
                break;
            }
        }

        if (found) {
            Database.DatabaseTransaksi.writeData(list, bank.getFileTransaksi());
        }
    }
}
