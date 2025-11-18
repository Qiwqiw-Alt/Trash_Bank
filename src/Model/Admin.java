package Model;

public class Admin extends User {
    private String namaAdmin;

    public Admin(String Username, String Password, String namaAdmin) {
        super(Username, Password);
        this.namaAdmin = namaAdmin;
    }

    public String getAdmin() {return namaAdmin;}
    public void setAdmin(String nama) {this.namaAdmin = nama;}
}
