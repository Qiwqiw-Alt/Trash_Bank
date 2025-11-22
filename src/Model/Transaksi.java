package Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Transaksi {
    private String idTransaksi;
    private String idPenyetor;
    private String idBank;

    private List<ItemTransaksi> items;
    private LocalDate tanggal;

    private int totalPoin;
    private double totalHarga;

    public Transaksi(String idTransaksi, String idPenyetor, String idBank) {
        this.idTransaksi = idTransaksi;
        this.idPenyetor = idPenyetor;
        this.idBank = idBank;

        this.items = new ArrayList<>();
        this.tanggal = LocalDate.now();
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public void setTotalPoin(int poin) {
        this.totalPoin = poin;
    }

    public void tambahItem(ItemTransaksi item) {
        items.add(item);
        hitungTotal();
    }

    private void hitungTotal() {
        totalHarga = 0;
        for (ItemTransaksi i : items) {
            totalHarga += i.getSubtotal();
        }
        totalPoin = Poin.konversiKePoin(totalHarga);
    }

    public String getIdPenyetor() {
        return this.idPenyetor;
    }

    public void setIdPenyetor(String idPenyetor){
        this.idPenyetor = idPenyetor;
    }

    public String getIdBank() {
        return this.idBank;
    }

    public void setIdBank(String idBank){
        this.idBank = idBank;
    }

    public String getIdTransaksi() { return idTransaksi; }
    public LocalDate getTanggal() { return tanggal; }
    public double getTotalHarga() { return totalHarga; }
    public int getTotalPoin() { return totalPoin; }
    public List<ItemTransaksi> getItems() { return items; }
}
