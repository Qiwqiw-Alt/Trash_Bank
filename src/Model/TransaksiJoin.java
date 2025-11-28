package Model;

public class TransaksiJoin {
    private String penyetorId;
    private String bankId;
    private boolean status;
    
    public TransaksiJoin(Penyetor penyetor, String bankId, boolean status){
        this.penyetorId = penyetor.getIdPenyetor();
        this.bankId = bankId;
        this.status = status;
    }

    public String getIdPenyetor(){
        return this.penyetorId;
    }

    public String bankId(){
        return this.bankId;
    }

    public boolean getStatusRequest(){
        return this.status;
    }

    public void setStatus(boolean status){
        this.status = status;
    }
}
