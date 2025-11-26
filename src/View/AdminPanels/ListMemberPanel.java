package View.AdminPanels;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JPanel;

import Controller.LoginController;
import Database.DatabasePenyetor;
import Model.Admin;
import Model.BankSampah;
import Model.Penyetor;

public class ListMemberPanel extends JPanel {
    public ListMemberPanel(Admin admin, BankSampah bankSampah){
        //fitur 1
        AddMember(bankSampah);
        

    }

    public void AddMember(BankSampah bankSampah){
        // Fitur pertam buat tambah member berdasarkan userIDnya.Jadi nanti skenarione, member mendatang
        //lokasi bank sampah disekitar tempat mereka tinggal, kemudian meminta admin bank sampah tersebut menambhakan meraka
        // dengan syarat user sudah mendaftar ke aplikasi (UserId harus sudah ada) dan belum tergabung ke bank sampah lain (idbankSampah = null)
        //misal pakai scanner
        Scanner scanner = new Scanner(System.in);
        String userID = scanner.nextLine();

        if(LoginController.isUserAvilable(userID)){
            bankSampah.d = DatabasePenyetor.loadData("src\\Database\\Penyetor\\" + bankSampah.getIdBank() + ".txt");
            daftarPenyetor.add()

        }

    }
}
