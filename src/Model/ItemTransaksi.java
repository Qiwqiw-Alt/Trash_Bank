package Model;

public class ItemTransaksi {
    private String kategoriSampah;
    private double hargaPerKg;
    private double beratKg;

    public ItemTransaksi(String kategoriSampah, double hargaPerKg, double beratKg) {
        this.kategoriSampah = kategoriSampah;
        this.hargaPerKg = hargaPerKg;
        this.beratKg = beratKg;
    }

    public String getKategoriSampah() { return kategoriSampah; }
    public double getHargaPerKg() { return hargaPerKg; }
    public double getBeratKg() { return beratKg; }

    public double getSubtotal() {
        return beratKg * hargaPerKg;
    }
}
