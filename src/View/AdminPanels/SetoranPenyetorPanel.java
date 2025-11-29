package View.AdminPanels;

import Controller.SetoranPenyetorController;
import Model.BankSampah;
import Model.ItemTransaksi;
import Model.Transaksi;
import Model.Transaksi.Status;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SetoranPenyetorPanel extends JPanel {

    private BankSampah currentBank;
    private String selectedIdTrx = null;

    // --- Komponen Tabel Master (Daftar Transaksi) ---
    private JTable tableMaster;
    private DefaultTableModel modelMaster;
    
    // --- Komponen Detail (Rincian Item) ---
    private JLabel lblIdTrx, lblPenyetor, lblTanggal, lblTotalBerat;
    private JTable tableDetail;
    private DefaultTableModel modelDetail;
    
    // --- Komponen Aksi (Ganti Status) ---
    private JComboBox<Status> cbStatus;
    private JButton btnUpdateStatus;

    // --- Styling ---
    private final Color GREEN_PRIMARY = new Color(40, 167, 69);
    private final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 12);

    public SetoranPenyetorPanel(BankSampah bankSampah) {
        this.currentBank = bankSampah;
        initLayout();
        refreshMasterTable();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Bagian Tabel Master (Daftar Transaksi)
        mainContent.add(createMasterSection());
        mainContent.add(Box.createVerticalStrut(20));
        
        mainContent.add(new JSeparator());
        mainContent.add(Box.createVerticalStrut(20));

        // 2. Bagian Detail (Rincian Item & Aksi)
        mainContent.add(createDetailSection());

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ========================================================================
    // 1. BAGIAN TABEL DAFTAR TRANSAKSI (MASTER)
    // ========================================================================
    private JPanel createMasterSection() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("📚 Daftar Semua Transaksi");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(new Color(33, 37, 41));
        
        JButton btnRefresh = new JButton("🔄 Refresh");
        btnRefresh.addActionListener(e -> refreshMasterTable());
        
        header.add(lblTitle, BorderLayout.WEST);
        header.add(btnRefresh, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        // Tabel Master (Sesuai output CLI kamu: ID, Penyetor, Status, Tanggal, Berat, Harga)
        String[] cols = {"ID Trx", "ID Penyetor", "Status", "Tanggal", "Total Berat", "Total Harga"};
        modelMaster = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tableMaster = new JTable(modelMaster);
        tableMaster.setRowHeight(30);
        tableMaster.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableMaster.setSelectionBackground(new Color(204, 229, 255));
        
        JTableHeader th = tableMaster.getTableHeader();
        th.setBackground(GREEN_PRIMARY);
        th.setForeground(Color.WHITE);
        th.setFont(FONT_BOLD);

        JScrollPane scroll = new JScrollPane(tableMaster);
        scroll.setPreferredSize(new Dimension(800, 200));
        panel.add(scroll, BorderLayout.CENTER);

        // Listener: Klik tabel master -> Tampilkan detail (Logic CLI kamu pindah ke sini)
        tableMaster.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableMaster.getSelectedRow();
                if (row != -1) {
                    loadDetailData(row);
                }
            }
        });

        return panel;
    }

    // ========================================================================
    // 2. BAGIAN DETAIL & UPDATE STATUS
    // ========================================================================
    private JPanel createDetailSection() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        
        TitledBorder border = BorderFactory.createTitledBorder(
            new LineBorder(Color.LIGHT_GRAY), " 🔍 Detail & Validasi Transaksi ");
        border.setTitleFont(FONT_BOLD);
        panel.setBorder(BorderFactory.createCompoundBorder(border, new EmptyBorder(15, 15, 15, 15)));

        // A. Info Header Transaksi
        JPanel pnlInfo = new JPanel(new GridLayout(2, 2, 10, 5));
        pnlInfo.setBackground(Color.WHITE);
        
        lblIdTrx = new JLabel("ID: -"); 
        lblPenyetor = new JLabel("Penyetor: -"); 
        lblTanggal = new JLabel("Tanggal: -"); 
        lblTotalBerat = new JLabel("Total Berat: 0 Kg"); 

        pnlInfo.add(lblIdTrx); pnlInfo.add(lblPenyetor);
        pnlInfo.add(lblTanggal); pnlInfo.add(lblTotalBerat);
        panel.add(pnlInfo, BorderLayout.NORTH);

        // B. Tabel Rincian Item (Sesuai CLI: IDTrx, IDSampah, Berat, Harga, Subtotal)
        String[] cols = {"ID Sampah", "Berat (Kg)", "Harga/Kg", "Subtotal"};
        modelDetail = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tableDetail = new JTable(modelDetail);
        tableDetail.setRowHeight(25);
        
        JScrollPane scroll = new JScrollPane(tableDetail);
        scroll.setPreferredSize(new Dimension(800, 150));
        panel.add(scroll, BorderLayout.CENTER);

        // C. Panel Aksi (Ganti Status)
        JPanel pnlAction = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlAction.setBackground(Color.WHITE);
        
        pnlAction.add(new JLabel("Ubah Status Validasi: "));
        
        // ComboBox untuk 4 Status (Sesuai Switch Case CLI)
        cbStatus = new JComboBox<>(Status.values());
        cbStatus.setPreferredSize(new Dimension(200, 35));
        pnlAction.add(cbStatus);
        
        btnUpdateStatus = new JButton("💾 Simpan Status");
        btnUpdateStatus.setBackground(GREEN_PRIMARY);
        btnUpdateStatus.setForeground(Color.WHITE);
        btnUpdateStatus.addActionListener(e -> handleUpdateStatus());
        pnlAction.add(btnUpdateStatus);

        panel.add(pnlAction, BorderLayout.SOUTH);

        toggleDetail(false); // Matikan dulu sebelum pilih

        return panel;
    }

    // ========================================================================
    // LOGIC UTAMA (PENGGANTI CLI)
    // ========================================================================

    // Pengganti method tampilkanSemuaTransaksi() di CLI
    private void refreshMasterTable() {
        modelMaster.setRowCount(0);
        toggleDetail(false);

        // Ambil data dari Controller
        ArrayList<Transaksi> listTrx = SetoranPenyetorController.getService().daftarTransaksis(currentBank);
        
        for (Transaksi t : listTrx) {
            modelMaster.addRow(new Object[]{
                t.getIdTransaksi(),
                t.getIdPenyetor(),
                t.getStatus(),
                t.getTanggal().toString(),
                t.getTotalBerat(),
                t.getTotalHarga()
            });
        }
    }

    // Pengganti method tampilkanItemTransaksi(id) di CLI
    private void loadDetailData(int row) {
        selectedIdTrx = (String) modelMaster.getValueAt(row, 0);
        
        // Ambil object transaksi lengkap dari Controller
        Transaksi trx = SetoranPenyetorController.getService().getTrxById(selectedIdTrx, currentBank);
        
        if (trx != null) {
            // Update Info Label
            lblIdTrx.setText("ID: " + trx.getIdTransaksi());
            lblPenyetor.setText("Penyetor: " + trx.getIdPenyetor());
            lblTanggal.setText("Tanggal: " + trx.getTanggal());
            lblTotalBerat.setText("Total Berat: " + trx.getTotalBerat() + " Kg");
            
            // Set ComboBox sesuai status saat ini
            cbStatus.setSelectedItem(trx.getStatus());
        }

        // Load Tabel Item
        modelDetail.setRowCount(0);
        ArrayList<ItemTransaksi> allItems = SetoranPenyetorController.getService().daftarItemTransaksi(currentBank);
        
        // Filter item yang sesuai ID Transaksi (Logika CLI kamu: if(itm.getId... == idTrx))
        for (ItemTransaksi itm : allItems) {
            if (itm.getIdTransaksi().equals(selectedIdTrx)) {
                modelDetail.addRow(new Object[]{
                    itm.getIdSampah(),
                    itm.getBeratKg(),
                    itm.getHargaPerKg(),
                    itm.getSubtotal()
                });
            }
        }

        toggleDetail(true);
    }

    // Pengganti Scanner input status di CLI
    private void handleUpdateStatus() {
        if (selectedIdTrx == null) return;

        Status newStatus = (Status) cbStatus.getSelectedItem();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Ubah status transaksi ini menjadi " + newStatus + "?", 
            "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Panggil Service untuk update (Kamu perlu tambahkan method updateStatus di Service)
            // Contoh logic (sesuaikan dengan Controller kamu):
            
            Transaksi trx = SetoranPenyetorController.getService().getTrxById(selectedIdTrx, currentBank);
            if (trx != null) {
                trx.setStatus(newStatus);
                // Simpan perubahan ke Database (PENTING: Tambahkan method save/update di Controller/Service)
                SetoranPenyetorController.getService().updateTransaksiStatus(trx, currentBank);
                
                JOptionPane.showMessageDialog(this, "Status berhasil diperbarui!");
                refreshMasterTable(); // Refresh tabel agar status berubah
            }
        }
    }

    private void toggleDetail(boolean enable) {
        cbStatus.setEnabled(enable);
        btnUpdateStatus.setEnabled(enable);
        tableDetail.setEnabled(enable);
        
        if (!enable) {
            lblIdTrx.setText("ID: -");
            modelDetail.setRowCount(0);
            selectedIdTrx = null;
        }
    }
}