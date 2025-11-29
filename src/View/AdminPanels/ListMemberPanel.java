package View.AdminPanels;

import Model.BankSampah;
import Model.Penyetor;
import Database.DatabasePenyetor;
import Controller.ListMemberController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ListMemberPanel extends JPanel {

    private BankSampah currentBank;
    
    // --- Komponen Tab Kiri ---
    private JTabbedPane tabLeft;
    
    // 1. Tab Manual Add
    private JTextField tfUserId;
    private JButton btnAdd;

    // 2. Tab Request List (BARU)
    private JTable tableRequest;
    private DefaultTableModel modelRequest;
    private JButton btnAccept;
    private JButton btnReject;
    
    // --- Komponen Kanan (Daftar Member Sah) ---
    private JTable tableMember;
    private DefaultTableModel tableModel;

    // Styling
    private final Color GREEN_PRIMARY = new Color(0, 128, 0);
    private final Color RED_DANGER = new Color(220, 53, 69);
    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FONT_TEXT = new Font("Segoe UI", Font.PLAIN, 14);

    public ListMemberPanel(BankSampah bankSampah) {
        this.currentBank = bankSampah;
        
        initLayout();
        refreshTable(); // Load data member
        refreshRequestTable(); // Load data request
    }

    private void initLayout() {
        setLayout(new GridLayout(1, 2, 20, 20));
        setBackground(new Color(245, 245, 245)); 
        setBorder(new EmptyBorder(20, 20, 20, 20)); 

        // 1. PANEL KIRI (TABBED PANE)
        add(createLeftPanel());

        // 2. PANEL KANAN (LIST MEMBER)
        add(createRightPanel());
    }

    // ========================================================================
    // BAGIAN KIRI: TABBED PANE (MANUAL ADD & REQUEST)
    // ========================================================================
    private JPanel createLeftPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        tabLeft = new JTabbedPane();
        tabLeft.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Tab 1: Input Manual (Kode lama dipindah ke method ini)
        tabLeft.addTab("➕ Tambah Manual", createManualAddPanel());
        
        // Tab 2: Permintaan Masuk (Fitur Baru)
        tabLeft.addTab("📩 Permintaan Masuk", createRequestPanel());

        container.add(tabLeft, BorderLayout.CENTER);
        return container;
    }

    private JPanel createManualAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; 

        JLabel lblTitle = new JLabel("Input ID Penyetor");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(GREEN_PRIMARY);
        gbc.gridy = 0; panel.add(lblTitle, gbc);

        JLabel lblDesc = new JLabel("<html>Masukkan ID Penyetor yang ingin<br>didaftarkan secara manual.</html>");
        lblDesc.setFont(FONT_TEXT);
        lblDesc.setForeground(Color.GRAY);
        gbc.gridy = 1; panel.add(lblDesc, gbc);

        JLabel lblInput = new JLabel("ID Penyetor (cth: UP001):");
        lblInput.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 2; panel.add(lblInput, gbc);

        tfUserId = new JTextField();
        tfUserId.setFont(FONT_TEXT);
        tfUserId.setPreferredSize(new Dimension(200, 35));
        gbc.gridy = 3; panel.add(tfUserId, gbc);

        btnAdd = new JButton("Tambahkan Member");
        styleButton(btnAdd, GREEN_PRIMARY);
        btnAdd.addActionListener(e -> handleAddMember());
        
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(btnAdd, gbc);
        
        gbc.gridy = 5; gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }

    private JPanel createRequestPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("Daftar Request Bergabung");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(Color.DARK_GRAY);
        panel.add(lblTitle, BorderLayout.NORTH);

        // Tabel Request
        String[] cols = {"ID", "Nama Penyetor"};
        modelRequest = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableRequest = new JTable(modelRequest);
        tableRequest.setRowHeight(25);
        tableRequest.setSelectionBackground(new Color(255, 240, 200)); // Warna kuning muda
        panel.add(new JScrollPane(tableRequest), BorderLayout.CENTER);

        // Tombol Aksi
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(Color.WHITE);

        btnAccept = new JButton("✅ Terima");
        styleButton(btnAccept, GREEN_PRIMARY);
        btnAccept.addActionListener(e -> handleRequestAction(true));

        btnReject = new JButton("❌ Tolak");
        styleButton(btnReject, RED_DANGER);
        btnReject.addActionListener(e -> handleRequestAction(false));

        btnPanel.add(btnAccept);
        btnPanel.add(btnReject);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ========================================================================
    // BAGIAN KANAN: DAFTAR MEMBER SAH
    // ========================================================================
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel("Daftar Anggota Aktif");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(GREEN_PRIMARY);
        panel.add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"ID", "Nama Lengkap", "No. HP"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tableMember = new JTable(tableModel);
        tableMember.setRowHeight(30);
        tableMember.getTableHeader().setBackground(new Color(240, 240, 240));
        tableMember.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableMember.setSelectionBackground(new Color(200, 255, 200));

        JScrollPane scrollPane = new JScrollPane(tableMember);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ========================================================================
    // LOGIC & ACTIONS
    // ========================================================================

    private void handleAddMember() {
        String targetId = tfUserId.getText().trim();
        
        if (targetId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Penyetor tidak boleh kosong!");
            return;
        }

        // --- Logic Manual Add (Sama seperti sebelumnya) ---
        if (ListMemberController.getService().isUserAvilable(targetId)) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Tambahkan User ID: " + targetId + " ke database lokal bank ini?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Proses Add Member
                prosesTambahMember(targetId);
                tfUserId.setText(""); 
            }
        } else {
            JOptionPane.showMessageDialog(this, "ID tidak ditemukan atau sudah punya Bank lain.");
        }
    }

    private void handleRequestAction(boolean isAccepted) {
        int row = tableRequest.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih user dari tabel request dulu!");
            return;
        }

        String userId = (String) modelRequest.getValueAt(row, 0);
        String userName = (String) modelRequest.getValueAt(row, 1);

        if (isAccepted) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Terima " + userName + " (" + userId + ") menjadi anggota?",
                "Konfirmasi Terima", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                prosesTambahMember(userId); // Pakai logic yang sama dengan manual add
                JOptionPane.showMessageDialog(this, "Berhasil menerima anggota!");
            }
        } else {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Tolak permintaan " + userName + "?",
                "Konfirmasi Tolak", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Logic Tolak: Hapus request (Update DB Global set BankID = null atau status Rejected)
                // TODO: Panggil Service untuk reject (Set ID Bank di user jadi null lagi)
                DatabasePenyetor.assignUserToBank(userId, "null"); 
                JOptionPane.showMessageDialog(this, "Permintaan ditolak.");
            }
        }
        
        refreshRequestTable();
        refreshTable();
    }

    // Method Helper agar tidak duplikasi kodingan
    private void prosesTambahMember(String userId) {
        boolean globalSuccess = DatabasePenyetor.assignUserToBank(userId, currentBank.getIdBank());
        
        if (globalSuccess) {
            Object userObj = ListMemberController.getService().getUserById(userId);
            if (userObj instanceof Penyetor) {
                Penyetor p = (Penyetor) userObj;
                p.setIdBankSampah(currentBank.getIdBank());
                
                String filePath = "src\\Database\\Penyetor\\penyetor_" + currentBank.getIdBank() + ".txt";
                DatabasePenyetor.addPenyetor(p, filePath);
                
                refreshTable();
                refreshRequestTable();
            }
        }
    }

    // --- REFRESH TABLES ---

    private void refreshTable() {
        tableModel.setRowCount(0);
        if (currentBank != null) {
            String filePath = "src\\Database\\Penyetor\\penyetor_" + currentBank.getIdBank() + ".txt";
            List<Penyetor> myMembers = DatabasePenyetor.loadData(filePath);
            for (Penyetor p : myMembers) {
                tableModel.addRow(new Object[]{ p.getIdPenyetor(), p.getNamaLengkap(), p.getNoHp() });
            }
        }
    }

    private void refreshRequestTable() {
        modelRequest.setRowCount(0);
        
        // TODO: LOGIKA CARI REQUEST (Contoh Logic)
        // Biasanya request adalah User yang ID Bank-nya = ID Bank Ini, 
        // TAPI belum masuk ke file penyetor_BSxxx.txt (Database Lokal).
        // Atau kamu bisa buat status khusus di Penyetor misal "PENDING".
        
        // Disini saya pakai simulasi dummy atau logic sederhana:
        // Load Global, cari yang ID Banknya = Bank ini, tapi belum ada di Lokal.
        
        /* ArrayList<Penyetor> global = DatabasePenyetor.loadData(); // Load semua user
        String myPath = "src\\Database\\Penyetor\\penyetor_" + currentBank.getIdBank() + ".txt";
        ArrayList<Penyetor> local = DatabasePenyetor.loadData(myPath); // Load user yg sudah sah
        
        for (Penyetor p : global) {
            // Jika dia milih bank ini
            if (p.getIdBankSampah() != null && p.getIdBankSampah().equals(currentBank.getIdBank())) {
                
                // Cek apakah dia SUDAH ada di lokal?
                boolean alreadyMember = false;
                for (Penyetor loc : local) {
                    if (loc.getIdPenyetor().equals(p.getIdPenyetor())) {
                        alreadyMember = true;
                        break;
                    }
                }
                
                // Jika BELUM ada di lokal, berarti dia statusnya REQUEST / PENDING
                if (!alreadyMember) {
                    modelRequest.addRow(new Object[]{ p.getIdPenyetor(), p.getNamaLengkap() });
                }
            }
        }
        */
        
        // DUMMY UNTUK TEST TAMPILAN
        modelRequest.addRow(new Object[]{"UP099", "Calon Member A"});
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}