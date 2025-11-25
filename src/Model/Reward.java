package Model;

public class Reward {
    private String idReward;
    private String namaHadiah;
    private String deskripsi;
    private double poinTukar;
    private int stok;

    public Reward(String idReward, String namaHadiah, String deskripsi, double poinTukar, int stok) {
        this.idReward = idReward;
        this.namaHadiah = namaHadiah;
        this.deskripsi = deskripsi;
        this.poinTukar = poinTukar;
        this.stok = stok;
    }

    public String getIdReward() { return idReward; }
    public String getNamaHadiah() { return namaHadiah; }
    public String getDeskripsi() { return deskripsi; }
    public double getPoinTukar() { return poinTukar; }
    public int getStok() { return stok; }

    public void kurangiStok(int jumlah) {
        this.stok -= jumlah;
    }

    public void tambahStok(int jumlah) {
        this.stok += jumlah;
    }
}
