package Model;

import java.util.ArrayList;
import java.util.List;

public class BankSampah {
    private String namaBankSampah;
    private Admin admin;
    private ArrayList<Penyetor> daftarPenyetor;
    private ArrayList<Sampah> daftarKategoriSampah;
    private ArrayList<Transaksi> daftarTransaksi;

    public BankSampah(String namaBank, Admin admin) {
        this.namaBankSampah = namaBank;
        this.admin = admin;
        this.daftarPenyetor = new ArrayList<>();
        this.daftarKategoriSampah = new ArrayList<>();
        this.daftarTransaksi = new ArrayList<>();
    }

    public void tambahPenyetor(Penyetor p){
        daftarPenyetor.add(p);
    }

    public Penyetor cariPenyetor(String username) {
        for (Penyetor p : daftarPenyetor) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }
        return null;
    }

    public List<Penyetor> getDaftarPenyetor(){
        return daftarPenyetor;
    }

    public void tambahKategoriSampah(Sampah s) {
        daftarKategoriSampah.add(s);
    }

    public List<Sampah> getDaftarSampah() {
        return daftarKategoriSampah;
    }

    public void tambahTransaksi(Transaksi t) {
        daftarTransaksi.add(t);
    }

    public List<Transaksi> getDaftarTransaksi() {
        return daftarTransaksi;
    }

    public String getNamaBank() {
        return namaBankSampah;
    }

    public Admin getAdmin() {
        return admin;
    }
}
