package Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Database.DatabaseBankSampah;
import Database.DatabaseItemTransaksi;
import Database.DatabasePenyetor;
import Database.DatabaseSampah;
import Database.DatabaseTransaksi;
import Model.Admin;
import Model.BankSampah;
import Model.ItemTransaksi;
import Model.Penyetor;
import Model.Sampah;
import Model.Transaksi;

public class AdminHomePanelService {
    public String getBankName(Admin admin){
        ArrayList<BankSampah> daftarBankSampah = DatabaseBankSampah.loadData();
       
        for(BankSampah bs : daftarBankSampah){
            if(bs.getIdBank().equals(admin.getIdBankSampah())){
                return bs.getNamaBank();
            }
        }

        return "Bank Smapah tidak ditemukan";
    }

    public int getTotalMember(BankSampah bankSampah){ 
        if(bankSampah == null) return 0;
        ArrayList<Penyetor> daftarPenyetors = DatabasePenyetor.loadData(bankSampah.getFilePenyetor());
        return daftarPenyetors.size();
    }

    public int getTotalTransaksi(BankSampah bankSampah){ 
        if(bankSampah == null) return 0;
        ArrayList<Transaksi> daftarTransaksi = DatabaseTransaksi.loadData(bankSampah.getFileTransaksi());
        return daftarTransaksi.size();
    }

    public double getTotalBeratSampah(BankSampah bankSampah){
        if (bankSampah == null) return 0.0;
        ArrayList<ItemTransaksi> daftarItemTransaksi = DatabaseItemTransaksi.loadData(bankSampah.getFileItemTransaksi());
        double beratTotal = 0.0;
        for(ItemTransaksi itm : daftarItemTransaksi){
            beratTotal += itm.getBeratKg();
        }
        return beratTotal;
    }

    public Map<String, Double> beratPerKategori(BankSampah bankSampah){
        Map<String, Double> beratSampahPerKategori = new HashMap<String, Double>();
        
        if (bankSampah == null) return beratSampahPerKategori;
        ArrayList<Sampah> daftarSampah = DatabaseSampah.loadData(bankSampah.getFileDaftarSampah());
        ArrayList<ItemTransaksi> daftarItemTransaksi = DatabaseItemTransaksi.loadData(bankSampah.getFileItemTransaksi());

        for(Sampah jenisSampah : daftarSampah){
            double beratPerKategori = 0;
            for(ItemTransaksi itm : daftarItemTransaksi){
                if(jenisSampah.getIdSampah().equals(itm.getIdSampah())){
                    beratPerKategori += itm.getBeratKg();
                }
            }

            beratSampahPerKategori.put(jenisSampah.getJenis(), beratPerKategori);
        }

        return beratSampahPerKategori;
    }
}
