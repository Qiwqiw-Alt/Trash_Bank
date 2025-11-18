package Model;

public class Sampah {
    private String jenis;
    private double hargaPerKg;

    public Sampah(String jenis, double hargaPerKg) {
        this.jenis = jenis;
        this.hargaPerKg = hargaPerKg;
    }

    public String getJenis() {
        return jenis;
    }

    public double getHargaPerKg() {
        return hargaPerKg;
    }
}
