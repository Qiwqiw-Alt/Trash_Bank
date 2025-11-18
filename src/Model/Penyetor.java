package Model;

public class Penyetor extends User {
    private String namaLengkap;
    private int totalPoin = 0;

    public Penyetor(String username, String password, String namaLengkap) {
        super(username, password);
        this.namaLengkap = namaLengkap;
    }

    public String getNamaLengkap() {return namaLengkap;}
    public int getTotalPoin() {return totalPoin;}
    public void tambahPoin(int poin) {this.totalPoin += poin;}
}
