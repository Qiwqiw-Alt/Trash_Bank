package Model;

public class Reward {
    private String idReward;
    private String namaHadiah;
    private String deskripsi;
    private double hargaTukar;
    private int stok;

    public Reward(String idReward, String namaHadiah, String deskripsi, double hargaTukar, int stok) {
        this.idReward = idReward;
        this.namaHadiah = namaHadiah;
        this.deskripsi = deskripsi;
        this.hargaTukar = hargaTukar;
        this.stok = stok;
    }

    public String getIdReward() { return idReward; }
    public String getNamaHadiah() { return namaHadiah; }
    public String getDeskripsi() { return deskripsi; }
    public double getHargaTukar() { return hargaTukar; }
    public int getStok() { return stok; }

    public void kurangiStok(int jumlah) {
        this.stok -= jumlah;
    }

    public void tambahStok(int jumlah) {
        this.stok += jumlah;
    }
}
