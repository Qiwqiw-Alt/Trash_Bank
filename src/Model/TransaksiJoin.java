package Model;

public class TransaksiJoin {
    public enum Status {
        SEDANG_DITINJAU,
        DITERIMA,
        DITOLAK, PENDING
    }

    private String idTransaksiJoin;
    private String penyetorId;
    private String bankId;
    private Status status;
    
    public TransaksiJoin(String idTransaksiJoin, String penyetorId, String bankId){
        this.idTransaksiJoin = idTransaksiJoin;
        this.penyetorId = penyetorId;
        this.bankId = bankId;
        this.status = Status.SEDANG_DITINJAU;
    }

    public String getIdTransaksiJoin(){
        return this.idTransaksiJoin;
    }

    public void setIdTransksiJoin(String idTransaksiJoin){
        this.idTransaksiJoin = idTransaksiJoin;
    }

    public String getIdPenyetor(){
        return this.penyetorId;
    }

    public String getbankId(){
        return this.bankId;
    }

    public Status getStatusRequest(){
        return this.status;
    }

    public void setStatus(Status status){
        this.status = status;
    }
}
