package View.AdminPanels;

import Model.Admin;
import Model.BankSampah;
import Model.Complain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Controller.ComplainController;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

// import Database.DatabaseComplain; // Import DB nanti

public class EvaluasiKomplainPanel extends JPanel {

    private Admin currentAdmin;
    private BankSampah currentBank;
    private String selectedIdComplain = null;
    private ArrayList<Complain> listComplainCache;

    // --- Komponen Tabel ---
    private JTable tableComplain;
    private DefaultTableModel tableModel;

    // --- Komponen Form Detail ---
    private JLabel lblPengirim;
    private JLabel lblTanggal;
    private JTextField tfJudul;
    private JTextArea taIsiKomplain;
    private JTextArea taTanggapanAdmin;
    
    // --- Komponen Ganti Status (COMBOBOX) ---
    private JComboBox<Complain.Status> cbStatus; 
    private JButton btnSimpan;

    // --- Styling ---
    private final Color GREEN_HEADER = new Color(40, 167, 69);
    private final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 12);

    public EvaluasiKomplainPanel(Admin admin, BankSampah bankSampah) {
        this.currentAdmin = admin;
        this.currentBank = bankSampah;
        initLayout();
        refreshTable();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Bagian Atas: Tabel
        mainContent.add(createTableSection());
        mainContent.add(Box.createVerticalStrut(20));
        mainContent.add(new JSeparator());
        mainContent.add(Box.createVerticalStrut(20));
        
        // Bagian Bawah: Form Ubah Status
        mainContent.add(createFormSection());

        JScrollPane mainScroll = new JScrollPane(mainContent);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScroll, BorderLayout.CENTER);
    }

    // --- 1. TABEL DAFTAR KOMPLAIN ---
    private JPanel createTableSection() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("📢 Daftar Komplain Masuk");
        lblTitle.setFont(FONT_HEADER);
        JButton btnRefresh = new JButton("🔄 Refresh");
        btnRefresh.addActionListener(e -> refreshTable());
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Tanggal", "ID Penyetor", "Judul Masalah", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tableComplain = new JTable(tableModel);
        tableComplain.setRowHeight(30);
        tableComplain.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableComplain.setSelectionBackground(new Color(204, 229, 255));

        JTableHeader header = tableComplain.getTableHeader();
        header.setBackground(GREEN_HEADER);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(tableComplain);
        scrollPane.setPreferredSize(new Dimension(800, 200));
        panel.add(scrollPane, BorderLayout.CENTER);

        tableComplain.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableComplain.getSelectedRow();
                if (row != -1) loadDetailToForm(row);
            }
        });

        return panel;
    }

    // --- 2. FORM DETAIL & UBAH STATUS ---
    private JPanel createFormSection() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(Color.LIGHT_GRAY), " 📝 Evaluasi & Update Status "));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;

        // Info Pengirim
        gbc.weightx = 0.5;
        lblPengirim = new JLabel("Pengirim: -");
        lblPengirim.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lblPengirim, gbc);

        gbc.gridx = 1;
        lblTanggal = new JLabel("Tanggal: -");
        lblTanggal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lblTanggal, gbc);

        // Judul (Read Only)
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        panel.add(createLabel("Judul Masalah:"), gbc);
        gbc.gridy++;
        tfJudul = new JTextField(); tfJudul.setEditable(false);
        tfJudul.setBackground(new Color(245, 245, 245));
        panel.add(tfJudul, gbc);

        // Isi Komplain (Read Only)
        gbc.gridy++;
        panel.add(createLabel("Isi Laporan:"), gbc);
        gbc.gridy++;
        taIsiKomplain = new JTextArea(3, 20); taIsiKomplain.setEditable(false);
        taIsiKomplain.setLineWrap(true); taIsiKomplain.setWrapStyleWord(true);
        taIsiKomplain.setBackground(new Color(245, 245, 245));
        panel.add(new JScrollPane(taIsiKomplain), gbc);

        // Tanggapan Admin (Editable)
        gbc.gridy++;
        panel.add(createLabel("Tanggapan Admin:"), gbc);
        gbc.gridy++;
        taTanggapanAdmin = new JTextArea(3, 20);
        taTanggapanAdmin.setLineWrap(true); taTanggapanAdmin.setWrapStyleWord(true);
        panel.add(new JScrollPane(taTanggapanAdmin), gbc);

        // --- PILIH STATUS (COMBOBOX) ---
        gbc.gridy++;
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actionPanel.setBackground(Color.WHITE);

        actionPanel.add(createLabel("Update Status: "));
        
        // ComboBox berisi 4 Status dari Enum
        cbStatus = new JComboBox<>(Complain.Status.values());
        cbStatus.setPreferredSize(new Dimension(200, 35));
        cbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        actionPanel.add(cbStatus);

        actionPanel.add(Box.createHorizontalStrut(15));

        // Tombol Simpan
        btnSimpan = new JButton("💾 Simpan Perubahan");
        styleButton(btnSimpan, GREEN_HEADER);
        btnSimpan.addActionListener(e -> handleUpdateStatus());
        actionPanel.add(btnSimpan);

        panel.add(actionPanel, gbc);

        toggleForm(false);
        return panel;
    }

    // ================= LOGIC =================

    private void handleUpdateStatus() {
        if (selectedIdComplain == null) return;

        // 1. Ambil Status yang dipilih di ComboBox
        Complain.Status newStatus = (Complain.Status) cbStatus.getSelectedItem();
        String tanggapan = taTanggapanAdmin.getText();

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Ubah status menjadi " + newStatus + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            
            // --- TODO: PANGGIL DATABASE KAMU ---
            // DatabaseComplain.updateComplainStatus(selectedIdComplain, newStatus, tanggapan, currentBank.getFileComplain());
            ComplainController.getService().updateComplain(selectedIdComplain, newStatus, tanggapan, currentBank);

            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
            
            // Refresh tabel agar status baru terlihat
            refreshTable();
            
            // Reset form
            toggleForm(false);
            taTanggapanAdmin.setText("");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        // DUMMY DATA (Ganti dengan loadData dari DB kamu nanti)
        this.listComplainCache = ComplainController.getService().daftarComplain(currentBank);
        for (Complain c : listComplainCache) {
            tableModel.addRow(new Object[]{
                c.getIdComplain(), 
                c.getFormattedTime(), 
                c.getIdPenyetor(), 
                c.getJudul(), 
                c.getStatus() // Akan tampil sebagai Teks Enum (PENDING, DLL)
            });
        }
    }

    private void loadDetailToForm(int row) {
        selectedIdComplain = tableModel.getValueAt(row, 0).toString();
        String tgl = tableModel.getValueAt(row, 1).toString();
        String sender = tableModel.getValueAt(row, 2).toString();
        String judul = tableModel.getValueAt(row, 3).toString();
        
        // Ambil status saat ini dari tabel
        Complain.Status currentStat = (Complain.Status) tableModel.getValueAt(row, 4);

        lblPengirim.setText("Pengirim: " + sender);
        lblTanggal.setText("Tanggal: " + tgl);
        tfJudul.setText(judul);
        
        // Set ComboBox sesuai status yang sedang aktif
        cbStatus.setSelectedItem(currentStat);
        
        // Logic ambil detail dari DB (Dummy)
        taIsiKomplain.setText("Detail isi komplain diambil dari database..."); 
        taTanggapanAdmin.setText("Tanggapan lama...");

        toggleForm(true);
    }

    private void toggleForm(boolean enable) {
        cbStatus.setEnabled(enable);
        taTanggapanAdmin.setEnabled(enable);
        btnSimpan.setEnabled(enable);

        if (!enable && selectedIdComplain == null) {
            lblPengirim.setText("Pengirim: -");
            lblTanggal.setText("Tanggal: -");
            tfJudul.setText("");
            taIsiKomplain.setText("");
            taTanggapanAdmin.setText("");
        }
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(180, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(Color.DARK_GRAY);
        return lbl;
    }
}