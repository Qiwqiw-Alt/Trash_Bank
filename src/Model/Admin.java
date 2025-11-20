package Model;

public class Admin extends User {
    private String namaAdmin;
    private String nohp;


    public Admin(String Username, String Password, String namaAdmin, String nohp) {
        super(Username, Password);
        this.nohp = nohp;
        this.namaAdmin = namaAdmin;
    }

    // Setter and Getter namaAdmin
    public String getAdmin() {return namaAdmin;}
    public void setAdmin(String nama) {this.namaAdmin = nama;}

    
    // Setter dan Getter nohp
    public String getNohp() {return this.nohp;}
    public void setNohp(String noHp) {this.nohp = noHp;}



}
