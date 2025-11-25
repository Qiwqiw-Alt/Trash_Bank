package Model;

import Database.DataBaseAdmin;

public class Admin extends User {
    private String idAdmin;
    private String namaAdmin;
    private String noHp;
    private String idBankSampah;



    public Admin(String idAdmin, String username, String password, String namaAdmin, String noHp) {
        super(username, password);
        this.idAdmin = idAdmin;
        this.noHp = noHp;
        this.namaAdmin = namaAdmin;
        this.idBankSampah = null;
    }

    // Setter and Getter namaAdmin
    public String getNamaAdmin() {return namaAdmin;}
    public void setNamaAdmin(String nama) {this.namaAdmin = nama;}

    // Setter dan Getter nohp
    public String getNohp() {return this.noHp;}
    public void setNohp(String noHp) {this.noHp = noHp;}

    public String getIdAdmin() { return idAdmin; }

    public String getIdBankSampah(){
        return this.idBankSampah;
    }

    public void setIdBankSampah(String idBankSampah){
        this.idBankSampah = idBankSampah;
    }


}
