package Model;

import java.util.ArrayList;

public class Penyetor extends User {
    private String namaLengkap;
    private int totalPoin = 0;
    private int totalSetoran = 0;
    private ArrayList<Transaksi> riwayatTransaksi;

    public Penyetor(String username, String password, String namaLengkap) {
        super(username, password);
        this.namaLengkap = namaLengkap;
    }

    public String getNamaLengkap() {return namaLengkap;}
    public void setNamaLengkap(String namaLengkap) {this.namaLengkap = namaLengkap;}

    public int getTotalPoin() {return totalPoin;}
    public void tambahPoin(int poin) {this.totalPoin += poin;}
    public void setTotalPoint(int poin) {this.totalPoin = poin;}

    public int getTotalSetoran(){return totalSetoran;}
    public void tambahSetoran(int banyakSetoran) {this.totalSetoran += banyakSetoran;}
    public void setSetoran(int setoran) {this.totalSetoran = setoran;}

}
