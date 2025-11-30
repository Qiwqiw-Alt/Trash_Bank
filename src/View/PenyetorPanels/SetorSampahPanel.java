package View.PenyetorPanels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Database.DatabaseItemTransaksi;
import Database.DatabaseSampah;
import Database.DatabaseTransaksi;

import Model.BankSampah;
import Model.Penyetor;
import Model.Sampah;
import Model.Transaksi;
import Model.ItemTransaksi;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
    private ArrayList<Transaksi> listTransaksi = new ArrayList<>();

    private final Color GREEN_PRIMARY = new Color(0x356A69); 
    private final Color SOFT_GREY = new Color(245, 245, 245);
    private final NumberFormat currencyFormatter = new DecimalFormat("#,##0");

    private final String RECYCLE_ICON_PATH = "Trash_Bank\\\\src\\\\Asset\\\\Image\\\\recycle-symbol.png"; 
    private final String CHECK_ICON_PATH = "Trash_Bank\\\\src\\\\Asset\\\\Image\\\\check.png";  
    private final int ICON_SIZE_SMALL = 18;
    private final int ICON_SIZE_BUTTON = 20;

    public SetorSampahPanel(Penyetor user, BankSampah bank) {

        setLayout(new BorderLayout());
        setBackground(SOFT_GREY);

        JLabel title = new JLabel(" Setor Sampah", SwingConstants.CENTER);
        title.setIcon(getScaledIcon(RECYCLE_ICON_PATH, ICON_SIZE_BUTTON, ICON_SIZE_BUTTON)); 
        title.setFont(new Font("Arial", Font.BOLD, 22)); 
        title.setForeground(GREEN_PRIMARY.darker()); 
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(0, 30, 30, 30)); 


        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.setBorder(new EmptyBorder(10, 40, 10, 40));
        form.setOpaque(false);
        
        comboSampah = new JComboBox<>();
        comboSampah.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 
        comboSampah.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        loadSampahToCombo(bank);

        tfBerat = new JTextField();
        tfBerat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfBerat.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));


        JLabel lbJenis = new JLabel("Jenis Sampah:");
        lbJenis.setIcon(null); 
        lbJenis.setFont(new Font("Segoe UI", Font.BOLD, 14));
        form.add(lbJenis);
        form.add(comboSampah);

        JLabel lbBerat = new JLabel("Berat (kg):");
        lbBerat.setFont(new Font("Segoe UI", Font.BOLD, 14));
        form.add(lbBerat);
        form.add(tfBerat);

        JButton btnTambah = new JButton(" Tambah Item");
        btnTambah.setBackground(GREEN_PRIMARY);
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setFont(new Font("Arial", Font.BOLD, 14));
        btnTambah.setFocusPainted(false);
        btnTambah.setBorder(new EmptyBorder(8, 15, 8, 15)); 
        btnTambah.addActionListener(e -> tambahItem());

        JPanel wrapTambah = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        wrapTambah.setOpaque(false);
        wrapTambah.add(btnTambah);

        String[] cols = { "Jenis", "Harga/Kg (Rp)", "Berat (kg)", "Subtotal (Rp)" };
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);

        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(new Color(240, 240, 240));
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(230, 245, 230));
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(GREEN_PRIMARY);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scroll = new JScrollPane(table); 
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200))); 
        scroll.setBorder(new EmptyBorder(10, 40, 10, 40));

        lbTotalBerat = new JLabel("Total Berat: 0 kg");
        lbTotalHarga = new JLabel("Total Harga: Rp 0");

        JPanel panelTotal = new JPanel(new BorderLayout()); 
        panelTotal.setOpaque(false);
        
        JPanel totalTextWrapper = new JPanel();
        totalTextWrapper.setLayout(new BoxLayout(totalTextWrapper, BoxLayout.Y_AXIS));
        totalTextWrapper.setOpaque(false);
        
        lbTotalBerat.setFont(new Font("Arial", Font.BOLD, 16)); 
        lbTotalHarga.setFont(new Font("Arial", Font.BOLD, 16)); 
        lbTotalHarga.setForeground(GREEN_PRIMARY.darker()); 
        
        lbTotalBerat.setAlignmentX(Component.RIGHT_ALIGNMENT);
        lbTotalHarga.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        totalTextWrapper.add(lbTotalBerat);
        totalTextWrapper.add(lbTotalHarga);
        
        panelTotal.add(totalTextWrapper, BorderLayout.EAST); 
        panelTotal.setBorder(new EmptyBorder(10, 40, 10, 40));

        JButton btnKirim = new JButton("Kirim ");
        btnKirim.setIcon(getScaledIcon(CHECK_ICON_PATH, ICON_SIZE_BUTTON, ICON_SIZE_BUTTON)); 
        btnKirim.setBackground(GREEN_PRIMARY);
        btnKirim.setForeground(Color.WHITE);
        btnKirim.setFont(new Font("Arial", Font.BOLD, 16));
        btnKirim.setFocusPainted(false);
        btnKirim.setBorder(new EmptyBorder(10, 25, 10, 25));
        btnKirim.addActionListener(e -> simpanTransaksi(user, bank));

        JPanel wrapKirim = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapKirim.setOpaque(false);
        wrapKirim.add(btnKirim);

        main.add(form);
        main.add(wrapTambah);
        main.add(Box.createVerticalStrut(10));
        main.add(scroll);
        main.add(Box.createVerticalStrut(10));
        main.add(panelTotal);
        main.add(wrapKirim);

        add(main, BorderLayout.CENTER);
    }

    private ImageIcon getScaledIcon(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (Exception e) {
            return null; 
        }
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

        tableModel.addRow(new Object[] {
                s.getJenis(), 
                currencyFormatter.format(hargaPerKg), 
                String.format("%.2f", berat), 
                currencyFormatter.format(subtotal)
        });

        totalBerat += berat;
        totalHarga += subtotal;

        lbTotalBerat.setText("Total Berat: " + String.format("%.2f", totalBerat) + " kg");
        lbTotalHarga.setText("Total Harga: Rp " + currencyFormatter.format(totalHarga));

        listItems.add(new ItemTransaksi("Belum ada", s.getIdSampah(), hargaPerKg, berat));

        tfBerat.setText("");
    }

    private void simpanTransaksi(Penyetor user, BankSampah bank) {

        
        listTransaksi = DatabaseTransaksi.loadData(bank.getFileTransaksi());

        if (listItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Belum ada item!");
            return;
        }

        String newId = DatabaseTransaksi.generateTransaksiId();

        Transaksi trx = new Transaksi(newId, user.getIdPenyetor(), bank.getIdBank());

        trx.setTotalBerat(totalBerat);
        trx.setTotalHarga(totalHarga);
        trx.setStatus(Transaksi.Status.SEDANG_DITINJAU);
        for (ItemTransaksi i : listItems) {
            i.setIdTransaksi(newId);
        }
        trx.setItemTransaksi(listItems);

        listTransaksi.add(trx); 
        
        DatabaseTransaksi.addTransaksi(trx, bank.getFileTransaksi()); 
        DatabaseTransaksi.writeData(listTransaksi); 
        DatabaseItemTransaksi.writeData(listItems, bank.getFileItemTransaksi()); 
        DatabaseItemTransaksi.writeData(listItems); 

        JOptionPane.showMessageDialog(this, "Transaksi berhasil dikirim!");
        user.tambahSetoran(1);

        tableModel.setRowCount(0);
        listItems.clear();
        totalBerat = 0;
        totalHarga = 0;
        lbTotalBerat.setText("Total Berat: 0 kg");
        lbTotalHarga.setText("Total Harga: Rp 0");
    }
}