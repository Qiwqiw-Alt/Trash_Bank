package Model;

public class Sampah {
    private String idSampah;
    private String jenis;
    private double hargaPerKg;

    public Sampah(String idSampah, String jenis, double hargaPerKg) {
        this.idSampah = idSampah;
        this.jenis = jenis;
        this.hargaPerKg = hargaPerKg;
    }

    public String getIdSampah() { return idSampah;}

    public String getJenis() {
        return jenis;
    }

    public void setHargaPerKg(double hargaBaru) {this.hargaPerKg = hargaBaru;}

    public double getHargaPerKg() {
        return hargaPerKg;
    }

    @Override
    public String toString() {
        return jenis + " (Rp " + hargaPerKg + "/kg)";
    }
}
