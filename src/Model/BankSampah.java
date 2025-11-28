package Model;

public class BankSampah {
    private String idBank;
    private String namaBankSampah;
    private String alamat;
    private Admin admin;

    private String fileAdmin;
    private String filePenyetor;
    private String fileTransaksi;
    private String fileDaftarSampah;
    private String fileDaftarReward;
    private String fileDaftarComplain;

    public BankSampah(String idBank, String namaBank, String alamat) {
        this.idBank = idBank;
        this.namaBankSampah = namaBank;
        this.alamat = alamat;

        this.fileAdmin = "src\\Database\\Admin\\admin_" + idBank + ".txt";
        this.filePenyetor = "src\\Database\\Penyetor\\penyetor_" + idBank + ".txt";
        this.fileTransaksi = "src\\Database\\Transaksi\\trx_" + idBank + ".txt";
        this.fileDaftarSampah = "src\\Database\\Sampah\\dfsampah_" + idBank + ".txt";
        this.fileDaftarReward = "src\\Database\\Reward\\dfreward_" + idBank + ".txt";
        this.fileDaftarComplain = "src\\Database\\Complain\\dfcompalain_" + idBank + ".txt";
    }

    public String getIdBank() { return idBank; }
    public String getNamaBank() { return namaBankSampah; }
    public void setNamaBank(String namaBank){ this.namaBankSampah = namaBank;}
    public Admin getAdmin() { return admin; }
    public void setAlamat(String alamat) {this.alamat = alamat;}
    public String getAlamat(){return this.alamat;}

    public String getFileAdmin() { return fileAdmin; }
    public String getFilePenyetor() { return filePenyetor; }
    public String getFileTransaksi() { return fileTransaksi; }
    public String getFileDaftarSampah() { return fileDaftarSampah; }
    public String getFileReward() { return fileDaftarReward; }
    public String getFileComplain() { return fileDaftarComplain; }
}
