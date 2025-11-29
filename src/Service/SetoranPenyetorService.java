package Service;

import java.util.ArrayList;

import Database.DatabaseItemTransaksi;
import Database.DatabaseTransaksi;
import Model.BankSampah;
import Model.ItemTransaksi;
import Model.Transaksi;

public class SetoranPenyetorService {
//     public ArrayList<Sampah> daftarKategoriSampah(BankSampah bankSampah){
//         ArrayList<Sampah> list = DatabaseSampah.loadData(bankSampah.getFileDaftarSampah());
//         return list;
//     }

//    public Sampah getSampahByJenis(String jenis, BankSampah bankSampah) {
//         ArrayList<Sampah> list = DatabaseSampah.loadData(bankSampah.getFileDaftarSampah());
//         for(Sampah s : list){
//             if(s.getJenis().equalsIgnoreCase(jenis)){
//                 return s; 
//             }
//         }
//         return null;
//     }

//     public Object getUserById(String userId, BankSampah bankSampah){
//         ArrayList<Admin> admins = DataBaseAdmin.loadData(bankSampah.getFileAdmin());
//         for (Admin a : admins) {
//             if (a.getIdAdmin().equals(userId)) {
//                 return a;
//             }
//         }

//         ArrayList<Penyetor> penyetors = DatabasePenyetor.loadData(bankSampah.getFilePenyetor());
//         for (Penyetor p : penyetors) {
//             if (p.getIdPenyetor().equals(userId)) {
//                 return p;
//             }
//         }

//         return null;
//     }
    
//     public void prosesTransaksiBaru(Transaksi trx, BankSampah bankSampah) {
        
//         String fileTrx = bankSampah.getFileTransaksi();
//         DatabaseTransaksi.addTransaksi(trx, fileTrx);

//         String fileItem = bankSampah.getFileItemTransaksi();
//         for (ItemTransaksi item : trx.getItems()) {
//             DatabaseItemTransaksi.addItem(item, fileItem);
//         }

//         updatePoinPenyetor(trx.getIdPenyetor(), trx.getTotalPoin(), bankSampah);
//     }

//     private void updatePoinPenyetor(String idPenyetor, int tambahanPoin, BankSampah bankSampah) {
//         String filePenyetor = bankSampah.getFilePenyetor();
        
//         ArrayList<Penyetor> listPenyetor = DatabasePenyetor.loadData(filePenyetor);
//         boolean found = false;

//         for (Penyetor p : listPenyetor) {
//             if (p.getIdPenyetor().equals(idPenyetor)) {
//                 // Ambil poin lama + Poin transaksi baru
//                 int poinBaru = p.getTotalPoin() + tambahanPoin;
//                 p.setTotalPoin(poinBaru); 
                
//                 found = true;
//                 break;
//             }
//         }

//         if (found) {
//             DatabasePenyetor.writeData(listPenyetor, filePenyetor);
//             System.out.println("Sukses! Poin " + idPenyetor + " bertambah " + tambahanPoin);
//         }
//     }

    public ArrayList<Transaksi> dafTransaksis(BankSampah bankSampah){
        return DatabaseTransaksi.loadData(bankSampah.getFileTransaksi());
    }

    public ArrayList<ItemTransaksi> daftarItemTransaksi(BankSampah bankSampah){
        return DatabaseItemTransaksi.loadData(bankSampah.getFileItemTransaksi());
    }
}
