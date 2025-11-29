package View.PenyetorPanels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Database.DatabaseSampah;
import Database.DatabaseTransaksi;

import Model.BankSampah;
import Model.Penyetor;
import Model.Sampah;
import Model.Transaksi;
import Model.ItemTransaksi;

import java.awt.*;
import java.util.ArrayList;

public class SetorSampahPanel extends JPanel {

    private JComboBox<Sampah> comboSampah;
    private JTextField tfBerat;
    private DefaultTableModel tableModel;

    private JLabel lbTotalBerat;
    private JLabel lbTotalHarga;

    private double totalBerat = 0;
    private double totalHarga = 0;

    private ArrayList<ItemTransaksi> listItems = new ArrayList<>();

    public SetorSampahPanel(Penyetor user, BankSampah bank) {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Setor Sampah", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);

        // -------------------------------
        // FORM INPUT
        // -------------------------------
        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.setBorder(new EmptyBorder(10, 40, 10, 40));
        form.setOpaque(false);

        comboSampah = new JComboBox<>();
        loadSampahToCombo(bank);

        tfBerat = new JTextField();

        form.add(new JLabel("Jenis Sampah:"));
        form.add(comboSampah);

        form.add(new JLabel("Berat (kg):"));
        form.add(tfBerat);

        // -------------------------------
        // TOMBOL TAMBAH
        // -------------------------------
        JButton btnTambah = new JButton("Tambah Item");
        btnTambah.addActionListener(e -> tambahItem());

        JPanel wrapTambah = new JPanel();
        wrapTambah.setOpaque(false);
        wrapTambah.add(btnTambah);

        // -------------------------------
        // TABLE ITEM
        // -------------------------------
        String[] cols = { "Jenis", "Harga/Kg", "Berat", "Subtotal" };
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(10, 40, 10, 40));

        // -------------------------------
        // TOTAL
        // -------------------------------
        lbTotalBerat = new JLabel("Total Berat: 0 kg");
        lbTotalHarga = new JLabel("Total Harga: Rp 0");

        JPanel panelTotal = new JPanel();
        panelTotal.setOpaque(false);
        panelTotal.setLayout(new BoxLayout(panelTotal, BoxLayout.Y_AXIS));
        lbTotalBerat.setFont(new Font("Arial", Font.BOLD, 14));
        lbTotalHarga.setFont(new Font("Arial", Font.BOLD, 14));
        panelTotal.add(lbTotalBerat);
        panelTotal.add(lbTotalHarga);
        panelTotal.setBorder(new EmptyBorder(10, 40, 10, 40));

        // -------------------------------
        // BUTTON KIRIM TRANSAKSI
        // -------------------------------
        JButton btnKirim = new JButton("Kirim Transaksi");
        btnKirim.addActionListener(e -> simpanTransaksi(user, bank));

        JPanel wrapKirim = new JPanel();
        wrapKirim.setOpaque(false);
        wrapKirim.add(btnKirim);

        // -------------------------------
        // ADD ALL COMPONENT
        // -------------------------------
        main.add(form);
        main.add(wrapTambah);
        main.add(scroll);
        main.add(panelTotal);
        main.add(wrapKirim);

        add(main, BorderLayout.CENTER);
    }

    private void loadSampahToCombo(BankSampah bank) {
        ArrayList<Sampah> list = DatabaseSampah.loadData(bank.getFileDaftarSampah());
        for (Sampah s : list)
            comboSampah.addItem(s);
    }

    private void tambahItem() {

        Sampah s = (Sampah) comboSampah.getSelectedItem();
        if (s == null)
            return;

        double berat;
        try {
            berat = Double.parseDouble(tfBerat.getText());
            if (berat <= 0)
                throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Masukkan berat valid");
            return;
        }

        double hargaPerKg = s.getHargaPerKg();
        double subtotal = berat * hargaPerKg;

        // Tambah ke tabel
        tableModel.addRow(new Object[] {
                s.getJenis(), hargaPerKg, berat, subtotal
        });

        // Update total
        totalBerat += berat;
        totalHarga += subtotal;

        lbTotalBerat.setText("Total Berat: " + totalBerat + " kg");
        lbTotalHarga.setText("Total Harga: Rp " + totalHarga);

        // Tambah ke list item transaksi
        listItems.add(new ItemTransaksi("TEMP", s.getIdSampah(), hargaPerKg, berat));

        tfBerat.setText("");
    }

    private void simpanTransaksi(Penyetor user, BankSampah bank) {

        if (listItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Belum ada item!");
            return;
        }

        String newId = DatabaseTransaksi.generateTransaksiId();

        Transaksi trx = new Transaksi(newId, user.getIdPenyetor(), bank.getIdBank());

        trx.setTotalBerat(totalBerat);
        trx.setTotalHarga(totalHarga);
        trx.setStatus(Transaksi.Status.SEDANG_DITINJAU);

        for (ItemTransaksi i : listItems)
            i.setIdTransaksi(newId);


        DatabaseTransaksi.addTransaksi(trx, bank.getFileTransaksi());
        

        JOptionPane.showMessageDialog(this, "Transaksi berhasil dikirim!");

        // Reset panel
        tableModel.setRowCount(0);
        listItems.clear();
        totalBerat = 0;
        totalHarga = 0;
        lbTotalBerat.setText("Total Berat: 0 kg");
        lbTotalHarga.setText("Total Harga: Rp 0");
    }
}
