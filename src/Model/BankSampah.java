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
    private ArrayList<Reward> daftarReward;


    private String fileAdmin;
    private String filePenyetor;
    private String fileTransaksi;
    private String fileDaftarSampah;
    private String fileDaftarReward;

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
        this.fileDaftarReward = "src\\Database\\Reward\\dfreward_" + idBank + ".txt";
    }

    public String getIdBank() { return idBank; }
    public String getNamaBank() { return namaBankSampah; }
    public void setNamaBank(String namaBank){
        this.namaBankSampah = namaBank;
    }

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
    public String getFileReward() { return fileDaftarReward; }

    public ArrayList<Penyetor> getDaftarPenyetor() { return daftarPenyetor; }
    public void setDaftarPenyetor(ArrayList<Penyetor> daftarPenyetor) { this.daftarPenyetor = daftarPenyetor; }
}
