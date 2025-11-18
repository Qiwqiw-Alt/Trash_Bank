package Model;

import java.time.LocalDate;

public class Transaksi {
    private Penyetor penyetor;
    private Sampah sampah;
    private double beratKg;
    private double totalHarga;
    private int poinDidapat;
    private LocalDate tanggal;

    public Transaksi(Penyetor penyetor, Sampah sampah, double beratKg) {
        this.penyetor = penyetor;
        this.sampah = sampah;
        this.beratKg = beratKg;
        this.totalHarga = beratKg * sampah.getHargaPerKg();
        this.poinDidapat = Poin.konversiKePoin(totalHarga);
        this.tanggal = LocalDate.now();

        this.penyetor.tambahPoin(poinDidapat); // langsung tambah poin
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public int getPoinDidapat() {
        return poinDidapat;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }


}
