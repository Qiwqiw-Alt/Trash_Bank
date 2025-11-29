package Service;

import Database.DatabaseItemTransaksi;
import Database.DatabasePenyetor;
import Database.DatabaseTransaksi;
import Model.BankSampah;
import Model.ItemTransaksi;
import Model.Penyetor;
import Model.Transaksi;

import java.util.ArrayList;

public class TransaksiService {

    // Method ini dipanggil saat Admin tekan tombol "Simpan" di GUI
    public void prosesTransaksiBaru(Transaksi trx, BankSampah bankSampah) {
        
        // 1. SIMPAN HEADER TRANSAKSI (ID, Tanggal, Total Poin)
        String fileTrx = bankSampah.getFileTransaksi();
        DatabaseTransaksi.addTransaksi(trx, fileTrx);

        // 2. SIMPAN ITEM-ITEM SAMPAH (Detailnya)
        String fileItem = bankSampah.getFileItemTransaksi();
        for (ItemTransaksi item : trx.getItems()) {
            DatabaseItemTransaksi.addItem(item, fileItem);
        }

        // 3. UPDATE POIN PENYETOR (Otomatis nambah poin)
        updatePoinPenyetor(trx.getIdPenyetor(), trx.getTotalPoin(), bankSampah);
    }

    // Logic Private untuk update poin user
    private void updatePoinPenyetor(String idPenyetor, int tambahanPoin, BankSampah bankSampah) {
        String filePenyetor = bankSampah.getFilePenyetor();
        
        // A. Load semua penyetor
        ArrayList<Penyetor> listPenyetor = DatabasePenyetor.loadData(filePenyetor);
        boolean found = false;

        // B. Cari penyetor yang sesuai, lalu update poinnya
        for (Penyetor p : listPenyetor) {
            if (p.getIdPenyetor().equals(idPenyetor)) {
                // Ambil poin lama + Poin transaksi baru
                int poinBaru = p.getTotalPoin() + tambahanPoin;
                p.setTotalPoin(poinBaru); 
                
                found = true;
                break;
            }
        }

        // C. Simpan balik ke file jika ketemu
        if (found) {
            DatabasePenyetor.writeData(listPenyetor, filePenyetor);
            System.out.println("Sukses! Poin " + idPenyetor + " bertambah " + tambahanPoin);
        }
    }
}