package Model;

import java.util.ArrayList;
import java.util.List;

import Database.DataBaseAdmin;
import Database.DatabasePenyetor;
import Database.DatabaseSampah;
import Database.DatabaseTransaksi;

public class BankSampah {
    private String idBank;
    private String namaBankSampah;
    private String alamat;

    private Admin admin;
    private ArrayList<Penyetor> daftarPenyetor;
    private ArrayList<Sampah> daftarKategoriSampah;
    private ArrayList<Transaksi> daftarTransaksi;


    private String fileAdmin;
    private String filePenyetor;
    private String fileTransaksi;
    private String fileDaftarSampah;

    public BankSampah(String idBank, String namaBank, String alamat) {
        this.idBank = idBank;
        this.namaBankSampah = namaBank;
        this.alamat = alamat;

        this.daftarPenyetor = new ArrayList<>();
        this.daftarKategoriSampah = new ArrayList<>();
        this.daftarTransaksi = new ArrayList<>();

        this.fileAdmin = "src\\Database\\Admin\\admin_" + idBank + ".txt";
        this.filePenyetor = "src\\Database\\Penyetor\\penyetor_" + idBank + ".txt";
        this.fileTransaksi = "src\\Database\\Transaksi\\trx_" + idBank + ".txt";
        this.fileDaftarSampah = "src\\Database\\Sampah\\dfsampah_" + idBank + ".txt";
    }

    public void writeDataAdmin(){
        DataBaseAdmin.writeData(fileAdmin);
    }

    public void loadDataAdmin(){
        this.daftarPenyetor = DatabasePenyetor.loadData(fileAdmin);
    }

    public void writeDataPenyetor(){
        DataBaseAdmin.writeData(filePenyetor);
    }

    public void loadDataPenyetor(){
        this.daftarPenyetor = DatabasePenyetor.loadData(filePenyetor);
    }

    public void writeDataTransaksi(){
        DataBaseAdmin.writeData(fileTransaksi);
    }

    public void loadDataTransaksi(){
        this.daftarTransaksi = DatabaseTransaksi.loadData(fileTransaksi);
    }

    public void writeDataSampah(){
        DataBaseAdmin.writeData(fileDaftarSampah);
    }

    public void loadDataSampah(){
        this.daftarKategoriSampah = DatabaseSampah.loadData(fileDaftarSampah);
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

    public ArrayList<Penyetor> getDaftarPenyetor(){
        return daftarPenyetor;
    }

    public void tambahKategoriSampah(Sampah s) {
        daftarKategoriSampah.add(s);
    }

    public ArrayList<Sampah> getDaftarSampah() {
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
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
    public String getAlamat(){
        return this.alamat;
    }

    public String getFileAdmin() { return fileAdmin; }
    public String getFilePenyetor() { return filePenyetor; }
    public String getFileTransaksi() { return fileTransaksi; }
    public String getFilDaftarSampah() { return fileDaftarSampah; }
}
