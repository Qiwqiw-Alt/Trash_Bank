package Model;

import java.time.LocalDate;

public class Complain {

    public enum Status {
        SEDANG_DITINJAU,
        DITERIMA,
        DITOLAK
    }

    private String idComplain;
    private String idPenyetor;
    private String idBank;

    private String judul;
    private String isi;

    private LocalDate tanggal;
    private Status status;
    private String tanggapanAdmin;

    public Complain(String idComplain, String idPenyetor, String idBank, String judul, String isi) {
        this.idComplain = idComplain;
        this.idPenyetor = idPenyetor;
        this.idBank = idBank;
        this.judul = judul;
        this.isi = isi;
        this.tanggal = LocalDate.now();
        this.status = Status.SEDANG_DITINJAU;
        this.tanggapanAdmin = "-";
    }

    // GETTER SETTER
    public String getIdComplain() { return idComplain; }
    public String getIdPenyetor() { return idPenyetor; }
    public String getIdBank() { return idBank; }
    public String getJudul() { return judul; }
    public String getIsi() { return isi; }
    public LocalDate getTanggal() { return tanggal; }
    public Status getStatus() { return status; }
    public String getTanggapanAdmin() { return tanggapanAdmin; }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setTanggapanAdmin(String tanggapanAdmin) {
        this.tanggapanAdmin = tanggapanAdmin;
    }
}
