package Model;

public class TransaksiJoin {
    private String idTransaksiJoin;
    private String penyetorId;
    private String bankId;
    private boolean status;
    
    public TransaksiJoin(String idTransaksiJoin, String penyetorId, String bankId, boolean status){
        this.idTransaksiJoin = idTransaksiJoin;
        this.penyetorId = penyetorId;
        this.bankId = bankId;
        this.status = status;
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

    public boolean getStatusRequest(){
        return this.status;
    }

    public void setStatus(boolean status){
        this.status = status;
    }
}
