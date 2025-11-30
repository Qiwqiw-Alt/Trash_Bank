package Model;

import java.util.ArrayList;

public class Penyetor extends User {

    private String idPenyetor;
    private String role;
    private String namaLengkap;
    private String noHp;
    private String idBankSampah;

    private Double totalPoin = 0.0; // -> total poin belum tahu
    private int totalSetoran = 0; // -> dapat diambil dari size transaksi
    private ArrayList<Transaksi> riwayatTransaksi;

    public Penyetor(String idPenyetor, String role, String username, String password, String namaLengkap, String noHp) {
        super(username, password);
        this.idPenyetor = idPenyetor;
        this.role = role;
        this.namaLengkap = namaLengkap;
        this.noHp = noHp;
        this.idBankSampah = null;
        this.riwayatTransaksi = new ArrayList<>();   // WAJIB
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public double getTotalPoin() {
        return totalPoin;
    }

    public void tambahPoin(int poin) {
        this.totalPoin += poin;
    }

    public void setTotalPoin(double totalPoin) {
        this.totalPoin = totalPoin;
    }

    public int getTotalSetoran() {
        return totalSetoran;
    }

    public void tambahSetoran(int banyakSetoran) {
        this.totalSetoran += banyakSetoran;
    }

    public void setTotalSetoran(int totalSetoran) {
        this.totalSetoran = totalSetoran;
    }

    public ArrayList<Transaksi> getRiwayatTransaksi() {
        return riwayatTransaksi;
    }

    public void setRiwayatTransaksi(ArrayList<Transaksi> listTransaksi) {
        this.riwayatTransaksi = listTransaksi;
    }

    public void tambahTransaksi(Transaksi id_trx) {
        riwayatTransaksi.add(id_trx);
    }

    public String getIdPenyetor() {
        return idPenyetor;
    }

    public String getIdBankSampah() {
        return idBankSampah;
    }

    public void setIdBankSampah(String idBankSampah) {
        this.idBankSampah = idBankSampah;
    }

     public String getRole(){
        return this.role;
    }
}
