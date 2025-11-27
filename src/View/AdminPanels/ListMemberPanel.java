package View.AdminPanels;

import Model.BankSampah;
import Model.Penyetor;
import Database.DatabasePenyetor;
import Controller.LoginController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ListMemberPanel extends JPanel {

    private BankSampah currentBank;
    
    // Komponen Form (Kiri)
    private JTextField tfUserId;
    private JButton btnAdd;
    
    // Komponen Tabel (Kanan)
    private JTable tableMember;
    private DefaultTableModel tableModel;

    // Styling
    private final Color GREEN_PRIMARY = new Color(0, 128, 0);
    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FONT_TEXT = new Font("Segoe UI", Font.PLAIN, 14);

    public ListMemberPanel(BankSampah bankSampah) { // Hapus parameter Admin jika tidak dipakai
        this.currentBank = bankSampah;
        
        initLayout();
        refreshTable(); // Load data saat panel dibuka
    }

    private void initLayout() {
        // Layout Utama: Grid 1 Baris, 2 Kolom (Kiri & Kanan), Jarak antar panel 20px
        setLayout(new GridLayout(1, 2, 20, 20));
        setBackground(new Color(245, 245, 245)); // Background abu muda
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Margin luar

        // 1. TAMBAHKAN PANEL KIRI (Form Tambah)
        add(createLeftPanel());

        // 2. TAMBAHKAN PANEL KANAN (Tabel List)
        add(createRightPanel());
    }

    // --- BAGIAN KIRI: FORM TAMBAH ---
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; 

        // Judul
        JLabel lblTitle = new JLabel("Tambah Anggota Baru");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(GREEN_PRIMARY);
        gbc.gridy = 0;
        panel.add(lblTitle, gbc);

        // Instruksi
        JLabel lblDesc = new JLabel("<html>Masukkan ID Penyetor (User ID)<br>yang ingin didaftarkan ke bank ini.</html>");
        lblDesc.setFont(FONT_TEXT);
        lblDesc.setForeground(Color.GRAY);
        gbc.gridy = 1;
        panel.add(lblDesc, gbc);

        // Input Field
        JLabel lblInput = new JLabel("ID Penyetor (cth: UP001):");
        lblInput.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 2;
        panel.add(lblInput, gbc);

        tfUserId = new JTextField();
        tfUserId.setFont(FONT_TEXT);
        tfUserId.setPreferredSize(new Dimension(200, 35));
        gbc.gridy = 3;
        panel.add(tfUserId, gbc);

        // Tombol Add
        btnAdd = new JButton("Tambahkan Member");
        btnAdd.setBackground(GREEN_PRIMARY);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setPreferredSize(new Dimension(200, 40));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setFocusPainted(false);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 0, 0, 0); // Jarak agak jauh dari input
        panel.add(btnAdd, gbc);
        
        // Spacer ke bawah (agar form tidak melayang di tengah vertikal)
        gbc.gridy = 5;
        gbc.weighty = 1.0; // Dorong konten ke atas
        panel.add(Box.createVerticalGlue(), gbc);

        // Logic Tombol
        btnAdd.addActionListener(e -> handleAddMember());

        return panel;
    }

    // --- BAGIAN KANAN: TABEL LIST ---
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Header
        JLabel lblTitle = new JLabel("Daftar Anggota Bank Sampah");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(GREEN_PRIMARY);
        panel.add(lblTitle, BorderLayout.NORTH);

        // Tabel
        String[] columns = {"ID", "Nama Lengkap", "No. HP"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel read-only
            }
        };

        tableMember = new JTable(tableModel);
        tableMember.setRowHeight(30);
        tableMember.getTableHeader().setBackground(new Color(240, 240, 240));
        tableMember.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableMember.setSelectionBackground(new Color(200, 255, 200));

        JScrollPane scrollPane = new JScrollPane(tableMember);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Hilangkan border default scrollpane
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // --- LOGIC ---

    private void handleAddMember() {
        String targetId = tfUserId.getText().trim();
        
        if (targetId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Penyetor tidak boleh kosong!");
            return;
        }

        // 1. Cek Logic Ketersediaan
        if (LoginController.isUserAvilable(targetId)) {
            
            // 2. Konfirmasi
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Tambahkan User ID: " + targetId + " ?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // 3. Update Database Global
                boolean success = DatabasePenyetor.assignUserToBank(targetId, currentBank.getIdBank());
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Berhasil menambahkan anggota!");
                    tfUserId.setText(""); // Reset form
                    refreshTable();       // Update tabel kanan otomatis
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal update database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            // --- LOGIKA JIKA USER TIDAK VALID (Else dari isUserAvilable) ---
            
            // Kita cek alasannya kenapa tidak available supaya pesan error-nya jelas
            Object user = LoginController.getUser(targetId);
            
            if (user == null) {
                JOptionPane.showMessageDialog(this, 
                    "ID Penyetor '" + targetId + "' tidak ditemukan di sistem.", 
                    "Data Tidak Ditemukan", JOptionPane.WARNING_MESSAGE);
            } else if (user instanceof Penyetor) {
                // Kalau user ada, berarti dia sudah punya bank sampah lain (idBank != null)
                JOptionPane.showMessageDialog(this, 
                    "Penyetor ini sudah tergabung di Bank Sampah lain.", 
                    "Gagal Menambahkan", JOptionPane.WARNING_MESSAGE);
            } else {
                // Kalau user ada tapi bukan Penyetor (misal ID Admin)
                JOptionPane.showMessageDialog(this, 
                    "ID tersebut terdaftar sebagai Admin, bukan Penyetor.", 
                    "Tipe Akun Salah", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Method untuk mengambil data terbaru dari database global
     * dan menampilkannya di tabel sebelah kanan.
     */
    private void refreshTable() {
        // 1. Bersihkan data lama di tabel
        tableModel.setRowCount(0);

        // 2. Load SEMUA data penyetor dari file global (data.txt)
        List<Penyetor> allPenyetor = DatabasePenyetor.loadData();

        // 3. FILTER: Hanya ambil penyetor yang ID Bank-nya sama dengan Bank ini
        if (currentBank != null && currentBank.getIdBank() != null) {
            List<Penyetor> myMembers = allPenyetor.stream()
                .filter(p -> p.getIdBankSampah() != null && 
                             p.getIdBankSampah().equals(currentBank.getIdBank()))
                .collect(Collectors.toList());

            // 4. Masukkan hasil filter ke dalam Tabel Model
            for (Penyetor p : myMembers) {
                tableModel.addRow(new Object[]{
                    p.getIdPenyetor(),
                    p.getNamaLengkap(),
                    p.getNoHp()
                });
            }
        }
    }
}                    