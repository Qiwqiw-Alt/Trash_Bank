package View.AdminPanels;

import Controller.RewardController;
import Database.DatabaseReward; // Pastikan import ini ada
import Model.BankSampah;
import Model.Reward;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ManajemenRewardPanel extends JPanel {

    private BankSampah currentBank;

    // Komponen Form (Kiri)
    private JTextField tfRname;
    private JTextField tfRdeskription;
    private JTextField tfRpoin;
    private JTextField tfStock;
    private JButton btnAdd;
    
    // Komponen Tabel (Kanan)
    private JTable tableReward; // Ganti nama jadi tableReward biar jelas
    private DefaultTableModel tableModel;

    // Styling
    private final Color GREEN_PRIMARY = new Color(0, 128, 0);
    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FONT_TEXT = new Font("Segoe UI", Font.PLAIN, 14);

    public ManajemenRewardPanel(BankSampah bankSampah){
        this.currentBank = bankSampah;
        
        initLayout();
        refreshTable(); // Load data reward saat panel dibuka
    }

    private void initLayout() {
        setLayout(new GridLayout(1, 2, 20, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(createLeftPanel());
        add(createRightPanel());
    }

    // --- BAGIAN KIRI: FORM TAMBAH REWARD ---
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0); // Jarak antar elemen lebih rapat
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; 

        // Judul
        JLabel lblTitle = new JLabel("Tambah Reward Baru");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(GREEN_PRIMARY);
        gbc.gridy = 0;
        panel.add(lblTitle, gbc);

        // Input Nama
        gbc.gridy++;
        panel.add(new JLabel("Nama Hadiah:"), gbc);
        tfRname = new JTextField();
        tfRname.setPreferredSize(new Dimension(200, 30));
        panel.add(tfRname, gbc);

        // Input Deskripsi
        gbc.gridy++;
        panel.add(new JLabel("Deskripsi Singkat:"), gbc);
        tfRdeskription = new JTextField();
        tfRdeskription.setPreferredSize(new Dimension(200, 30));
        panel.add(tfRdeskription, gbc);

        // Input Poin
        gbc.gridy++;
        panel.add(new JLabel("Poin Tukar:"), gbc);
        tfRpoin = new JTextField();
        tfRpoin.setPreferredSize(new Dimension(200, 30));
        panel.add(tfRpoin, gbc);

        // Input Stok
        gbc.gridy++;
        panel.add(new JLabel("Stok Awal:"), gbc);
        tfStock = new JTextField();
        tfStock.setPreferredSize(new Dimension(200, 30));
        panel.add(tfStock, gbc);

        // Tombol Add
        btnAdd = new JButton("Simpan Reward");
        btnAdd.setBackground(GREEN_PRIMARY);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(btnAdd, gbc);
        
        gbc.gridy++;
        gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);

        btnAdd.addActionListener(e -> handleAddReward());

        return panel;
    }

    // --- BAGIAN KANAN: TABEL REWARD ---
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel("Daftar Reward Tersedia");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(GREEN_PRIMARY);
        panel.add(lblTitle, BorderLayout.NORTH);

        // Tabel Khusus Reward
        String[] columns = {"ID", "Nama Hadiah", "Poin", "Stok"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableReward = new JTable(tableModel);
        tableReward.setRowHeight(30);
        tableReward.getTableHeader().setBackground(new Color(240, 240, 240));
        tableReward.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        panel.add(new JScrollPane(tableReward), BorderLayout.CENTER);

        return panel;
    }

    // --- LOGIC BENAR (HANDLING REWARD) ---
    private void handleAddReward() {
        String name = tfRname.getText().trim();
        String desc = tfRdeskription.getText().trim();
        String poinStr = tfRpoin.getText().trim();
        String stokStr = tfStock.getText().trim();

        // 1. Validasi Kosong
        if (name.isEmpty() || desc.isEmpty() || poinStr.isEmpty() || stokStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!");
            return;
        }

        try {
            // 2. Parsing Angka
            double poin = Double.parseDouble(poinStr);
            int stok = Integer.parseInt(stokStr);

            if (poin < 0 || stok < 0) {
                JOptionPane.showMessageDialog(this, "Nilai tidak boleh negatif.");
                return;
            }

            // 3. Cek apakah nama reward sudah ada
            // Logic ini harus DIBALIK dari kodemu sebelumnya. 
            // Kalau isRewardAvailable == true, berarti ERROR (Duplikat).
            if (RewardController.getService().isRewardAvailable(name, currentBank.getIdBank())) {
                JOptionPane.showMessageDialog(this, 
                    "Reward dengan nama '" + name + "' sudah ada!", 
                    "Duplikat", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 4. Proses Simpan
            int confirm = JOptionPane.showConfirmDialog(this, "Simpan Reward ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Panggil Controller Reward
                RewardController.tambahReward(currentBank.getIdBank(), name, desc, poin, stok);
                
                JOptionPane.showMessageDialog(this, "Reward berhasil ditambahkan!");
                
                // Reset Form
                tfRname.setText("");
                tfRdeskription.setText("");
                tfRpoin.setText("");
                tfStock.setText("");
                
                // Refresh Tabel Reward
                refreshTable();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Poin dan Stok harus angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- LOGIC REFRESH TABEL REWARD ---
    private void refreshTable() {
        tableModel.setRowCount(0);

        // Load data reward spesifik bank ini
        // Kita butuh path file reward bank ini: "src/Database/Reward/dfreward_BSxxx.txt"
        String path = "src\\Database\\Reward\\dfreward_" + currentBank.getIdBank() + ".txt";
        
        ArrayList<Reward> list = DatabaseReward.loadData(path);

        for (Reward r : list) {
            tableModel.addRow(new Object[]{
                r.getIdReward(),
                r.getNamaHadiah(),
                r.getPoinTukar(), // Tampilkan Poin
                r.getStok()       // Tampilkan Stok
            });
        }
    }
}