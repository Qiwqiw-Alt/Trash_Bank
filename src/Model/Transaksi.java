package Model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Transaksi {
    public enum Status {
        SEDANG_DITINJAU,
        DITERIMA,
        DITOLAK, PENDING;
    }

    private String idTransaksi;
    private String idPenyetor;
    private String idBank;
    private Status status;

    private ArrayList<ItemTransaksi> items;
    private LocalDate tanggal;

    private double totalHarga;
    private double totalBerat;

    public Transaksi() {}

    public Transaksi(String idTransaksi, String idPenyetor, String idBank) {
        this.idTransaksi = idTransaksi;
        this.idPenyetor = idPenyetor;
        this.idBank = idBank;
        this.status = Status.SEDANG_DITINJAU;
        
        this.tanggal = LocalDate.now();
        this.items = new ArrayList<>();
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public void setTotalBerat(double totalBerat) {
        this.totalBerat = totalBerat;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getIdPenyetor() {
        return this.idPenyetor;
    }

    public String getIdBank() {
        return this.idBank;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus(){
        return this.status;
    }

    public void setItemTransaksi(ArrayList<ItemTransaksi> listItem) {
        this.items = listItem;
    }

    public String getIdTransaksi() { return idTransaksi; }
    public LocalDate getTanggal() { return tanggal; }
    public double getTotalBerat() { return totalBerat; }
    public double getTotalHarga() { return totalHarga; }
    public ArrayList<ItemTransaksi> getItems() { return items; }
}
