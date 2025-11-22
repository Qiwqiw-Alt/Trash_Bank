package Model;

public class ItemTransaksi {

    private String idTransaksi;
    private String idSampah;
    private double hargaPerKg;
    private double beratKg;

    public ItemTransaksi(String idTransaksi, String idSampah, double hargaPerKg, double beratKg) {
        this.idTransaksi = idTransaksi;
        this.idSampah = idSampah;
        this.hargaPerKg = hargaPerKg;
        this.beratKg = beratKg;
    }

    public double getSubtotal() {
        return hargaPerKg * beratKg;
    }

    public String getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(String idTransaksi){this.idTransaksi = idTransaksi;}
    public String getIdSampah() { return idSampah; }
    public void setIdSampah(String idSampah){this.idSampah = idSampah; }
    public double getHargaPerKg() { return hargaPerKg; }
    public double getBeratKg() { return beratKg; }
}
