package View.AdminPanels;

import Database.DatabaseReward;
import Model.BankSampah;
import Model.Reward;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
// import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ManajemenRewardPanel extends JPanel {

    private BankSampah currentBank;
    private String selectedIdReward = null; // Untuk menyimpan ID yang sedang diedit

    // Komponen Form
    private JTextField tfRname;
    private JTextArea taRdescription;
    private JTextField tfRpoin;
    private JTextField tfStock;
    
    // Tombol CRUD
    private JButton btnAdd;    // Create
    private JButton btnUpdate; // Update
    private JButton btnDelete; // Delete
    private JButton btnClear;  // Reset Form

    // Komponen Tabel
    private JTable tableReward;
    private DefaultTableModel tableModel;

    // Styling Colors
    private final Color GREEN_HEADER = new Color(40, 167, 69);
    private final Color GREEN_BG_LIGHT = new Color(209, 231, 221);
    private final Color TEXT_GREEN = new Color(15, 81, 50);
    private final Color COLOR_EDIT = new Color(255, 193, 7); // Kuning
    private final Color COLOR_DELETE = new Color(220, 53, 69); // Merah

    // Fonts
    private final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 22);
    private final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);

    public ManajemenRewardPanel(BankSampah bankSampah) {
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
        mainContent.setBorder(new EmptyBorder(20, 30, 20, 30));

        mainContent.add(createTopSection());
        
        JSeparator sep = new JSeparator();
        sep.setForeground(Color.LIGHT_GRAY);
        mainContent.add(Box.createVerticalStrut(20));
        mainContent.add(sep);
        mainContent.add(Box.createVerticalStrut(20));

        mainContent.add(createBottomSection());

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    // --- BAGIAN TABEL (READ) ---
    private JPanel createTopSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel lblMainTitle = new JLabel("📝 Daftar Reward (CRUD System)");
        lblMainTitle.setFont(FONT_HEADER);
        lblMainTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblMainTitle);
        panel.add(Box.createVerticalStrut(15));

        // Alert Info
        JPanel alertPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        alertPanel.setBackground(GREEN_BG_LIGHT);
        alertPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(186, 203, 190), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        alertPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
        JLabel lblAlert = new JLabel("💡 Info: Klik baris pada tabel untuk Mengedit atau Menghapus data.");
        lblAlert.setForeground(TEXT_GREEN);
        lblAlert.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        alertPanel.add(lblAlert);
        alertPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(alertPanel);
        panel.add(Box.createVerticalStrut(15));

        // Tabel
        String[] columns = {"ID", "Nama Reward", "Deskripsi", "Poin", "Stok"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableReward = new JTable(tableModel);
        tableReward.setRowHeight(35);
        tableReward.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableReward.setSelectionBackground(new Color(204, 229, 255));
        
        JTableHeader header = tableReward.getTableHeader();
        header.setBackground(GREEN_HEADER);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollTable = new JScrollPane(tableReward);
        scrollTable.setPreferredSize(new Dimension(800, 250));
        scrollTable.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(scrollTable);

        // --- EVENT LISTENER TABEL (PENTING BUAT UPDATE/DELETE) ---
        tableReward.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableReward.getSelectedRow();
                if (row != -1) {
                    // Ambil data dari tabel
                    selectedIdReward = tableModel.getValueAt(row, 0).toString();
                    String nama = tableModel.getValueAt(row, 1).toString();
                    String desc = tableModel.getValueAt(row, 2).toString();
                    String poin = tableModel.getValueAt(row, 3).toString();
                    String stok = tableModel.getValueAt(row, 4).toString();

                    // Isi ke form
                    tfRname.setText(nama);
                    taRdescription.setText(desc);
                    tfRpoin.setText(poin);
                    tfStock.setText(stok);

                    // Atur tombol: Matikan Add, Nyalakan Edit & Delete
                    toggleButtons(true);
                }
            }
        });

        return panel;
    }

    // --- BAGIAN FORM (CREATE, UPDATE, DELETE) ---
    private JPanel createBottomSection() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel lblFormTitle = new JLabel("🛠️ Form Kelola Reward");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(lblFormTitle, gbc);

        // Input Fields
        gbc.gridy++; panel.add(new JLabel("Nama Reward:"), gbc);
        gbc.gridy++; tfRname = new JTextField(); styleTextField(tfRname); panel.add(tfRname, gbc);

        gbc.gridy++; panel.add(new JLabel("Deskripsi:"), gbc);
        gbc.gridy++; 
        taRdescription = new JTextArea(3, 20); 
        taRdescription.setFont(FONT_INPUT);
        taRdescription.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.add(new JScrollPane(taRdescription), gbc);

        // Row Poin & Stok
        gbc.gridy++;
        JPanel rowNumbers = new JPanel(new GridLayout(1, 2, 20, 0));
        rowNumbers.setBackground(Color.WHITE);
        
        JPanel p1 = new JPanel(new BorderLayout()); p1.setBackground(Color.WHITE);
        p1.add(new JLabel("Poin:"), BorderLayout.NORTH); 
        tfRpoin = new JTextField(); styleTextField(tfRpoin); p1.add(tfRpoin, BorderLayout.CENTER);
        
        JPanel p2 = new JPanel(new BorderLayout()); p2.setBackground(Color.WHITE);
        p2.add(new JLabel("Stok:"), BorderLayout.NORTH); 
        tfStock = new JTextField(); styleTextField(tfStock); p2.add(tfStock, BorderLayout.CENTER);

        rowNumbers.add(p1); rowNumbers.add(p2);
        panel.add(rowNumbers, gbc);

        // --- TOMBOL AKSI ---
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setBackground(Color.WHITE);

        btnAdd = new JButton("💾 Simpan");
        styleButton(btnAdd, GREEN_HEADER);
        btnAdd.addActionListener(e -> handleAdd());

        btnUpdate = new JButton("✏️ Edit");
        styleButton(btnUpdate, COLOR_EDIT);
        btnUpdate.setForeground(Color.BLACK); // Text hitam biar jelas di kuning
        btnUpdate.addActionListener(e -> handleUpdate());

        btnDelete = new JButton("🗑️ Hapus");
        styleButton(btnDelete, COLOR_DELETE);
        btnDelete.addActionListener(e -> handleDelete());

        btnClear = new JButton("🔄 Bersihkan");
        styleButton(btnClear, Color.GRAY);
        btnClear.addActionListener(e -> resetForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        panel.add(btnPanel, gbc);

        // Default: Tombol Edit/Hapus mati dulu
        toggleButtons(false);

        return panel;
    }

    // ================= LOGIC CRUD =================

    // 1. CREATE
    private void handleAdd() {
        if (!validateInput()) return;
        
        // Generate ID baru (Bisa pakai method static di DatabaseReward)
        String newId = DatabaseReward.generateRewardId(); 
        
        Reward r = new Reward(
            newId,
            tfRname.getText().trim(),
            taRdescription.getText().trim(),
            Integer.parseInt(tfRpoin.getText().trim()),
            Integer.parseInt(tfStock.getText().trim())
        );

        String path = currentBank.getFileReward(); // Ambil path dari Model Bank
        DatabaseReward.addReward(r, path);
        
        JOptionPane.showMessageDialog(this, "Berhasil menambah data!");
        resetForm();
        refreshTable();
    }

    // 2. UPDATE
    private void handleUpdate() {
        if (selectedIdReward == null) return;
        if (!validateInput()) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ubah data ini?", "Edit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            
            Reward r = new Reward(
                selectedIdReward, // ID Tetap sama (jangan diubah)
                tfRname.getText().trim(),
                taRdescription.getText().trim(),
                Integer.parseInt(tfRpoin.getText().trim()),
                Integer.parseInt(tfStock.getText().trim())
            );

            String path = currentBank.getFileReward();
            DatabaseReward.updateReward(r, path);

            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
            resetForm();
            refreshTable();
        }
    }

    // 3. DELETE
    private void handleDelete() {
        if (selectedIdReward == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus reward ini?", "Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            
            String path = currentBank.getFileReward();
            DatabaseReward.deleteReward(selectedIdReward, path);

            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
            resetForm();
            refreshTable();
        }
    }

    // 4. READ (Refresh Table)
    private void refreshTable() {
        tableModel.setRowCount(0);
        String path = currentBank.getFileReward();
        ArrayList<Reward> list = DatabaseReward.loadData(path);

        for (Reward r : list) {
            tableModel.addRow(new Object[]{
                r.getIdReward(),
                r.getNamaHadiah(),
                r.getDeskripsi(),
                r.getPoinTukar(),
                r.getStok()
            });
        }
    }

    // --- Helpers ---
    private boolean validateInput() {
        if (tfRname.getText().isEmpty() || tfRpoin.getText().isEmpty() || tfStock.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data tidak boleh kosong!");
            return false;
        }
        try {
            Integer.parseInt(tfRpoin.getText());
            Integer.parseInt(tfStock.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Poin dan Stok harus angka!");
            return false;
        }
        return true;
    }

    private void resetForm() {
        tfRname.setText("");
        taRdescription.setText("");
        tfRpoin.setText("");
        tfStock.setText("");
        selectedIdReward = null;
        tableReward.clearSelection();
        toggleButtons(false); // Balik ke mode Add
    }

    private void toggleButtons(boolean isEditMode) {
        btnAdd.setEnabled(!isEditMode);   // Jika mode edit, tombol Add mati
        btnUpdate.setEnabled(isEditMode); // Jika mode edit, tombol Update nyala
        btnDelete.setEnabled(isEditMode); // Jika mode edit, tombol Delete nyala
    }

    private void styleTextField(JTextField tf) {
        tf.setFont(FONT_INPUT);
        tf.setPreferredSize(new Dimension(200, 35));
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(100, 35));
    }
}