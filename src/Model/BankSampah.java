package Model;

import java.util.ArrayList;
import java.util.List;

public class BankSampah {
    private String idBank;
    private String namaBankSampah;

    private Admin admin;
    private ArrayList<Penyetor> daftarPenyetor;
    private ArrayList<Sampah> daftarKategoriSampah;
    private ArrayList<Transaksi> daftarTransaksi;

    private String fileAdmin;
    private String filePenyetor;
    private String fileTransaksi;

    public BankSampah(String idBank, String namaBank) {
        this.idBank = idBank;
        this.namaBankSampah = namaBank;

        this.daftarPenyetor = new ArrayList<>();
        this.daftarKategoriSampah = new ArrayList<>();
        this.daftarTransaksi = new ArrayList<>();

        this.fileAdmin = "admin_" + idBank + ".txt";
        this.filePenyetor = "penyetor_" + idBank + ".txt";
        this.fileTransaksi = "trx_" + idBank + ".txt";
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

    public String getIdBank() { return idBank; }
    public String getNamaBank() { return namaBankSampah; }
    public Admin getAdmin() { return admin; }

    public String getFileAdmin() { return fileAdmin; }
    public String getFilePenyetor() { return filePenyetor; }
    public String getFileTransaksi() { return fileTransaksi; }
}
