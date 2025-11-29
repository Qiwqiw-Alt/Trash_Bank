package View.AdminPanels;

import Model.Admin;
import Model.BankSampah;
import Model.Penyetor;
import Model.Sampah;
import Model.Transaksi;
import Database.DatabaseSampah; 

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Controller.SetoranPenyetorController;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SetoranPenyetorPanel extends JPanel {
    private BankSampah curreBankSampah;


    public SetoranPenyetorPanel(BankSampah bankSampah){
        this.curreBankSampah = bankSampah;
    }

    public void tampilkanSemuaTransaksi(){
        ArrayList<Transaksi> dafTransaksi = SetoranPenyetorController.getService().dafTransaksis(curreBankSampah);
        System.out.println("ID, ID Bank, ");
        for(Transaksi trx : dafTransaksi){

        }
    }
}