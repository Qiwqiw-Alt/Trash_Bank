package View.AdminPanels;

import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Database.DatabaseSampah;
import Model.BankSampah;
import Model.Sampah;

public class ManajemenSampahPanel extends JPanel {
    private BankSampah currentBank;
    private String selectedIdSampah= null; 

    // Komponen Form
    private JTextField tfRKategori;
    private JTextField tfRharga;
    
    // Tombol CRUD
    private JButton btnAdd;    // Create
    private JButton btnUpdate; // Update
    private JButton btnDelete; // Delete
    private JButton btnClear;  // Reset Form

    // Komponen Tabel
    private JTable tableSampah;
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

    public ManajemenSampahPanel(BankSampah bankSampah){
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

        JLabel lblMainTitle = new JLabel("📝 Daftar Kategori Sampah");
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
        String[] columns = {"ID", "Kategori Sampah", "Harga per Kg"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableSampah = new JTable(tableModel);
        tableSampah.setRowHeight(35);
        tableSampah.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableSampah.setSelectionBackground(new Color(204, 229, 255));
        
        JTableHeader header = tableSampah.getTableHeader();
        header.setBackground(GREEN_HEADER);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollTable = new JScrollPane(tableSampah);
        scrollTable.setPreferredSize(new Dimension(800, 250));
        scrollTable.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(scrollTable);

        // --- EVENT LISTENER TABEL (PENTING BUAT UPDATE/DELETE) ---
        tableSampah.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableSampah.getSelectedRow();
                if (row != -1) {
                    // Ambil data dari tabel
                    selectedIdSampah = tableModel.getValueAt(row, 0).toString();
                    String kategori = tableModel.getValueAt(row, 1).toString();
                    String harga = tableModel.getValueAt(row, 2).toString();

                    // Isi ke form
                    tfRKategori.setText(kategori);
                    tfRharga.setText(harga);

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

        JLabel lblFormTitle = new JLabel("🛠️ Form Kelola Sampah");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(lblFormTitle, gbc);

        // Input Fields
        gbc.gridy++; panel.add(new JLabel("Kategori Sampah:"), gbc);
        gbc.gridy++; tfRKategori = new JTextField(); styleTextField(tfRKategori); panel.add(tfRKategori, gbc);

        gbc.gridy++; panel.add(new JLabel("Harga per Kg (Rp):"), gbc);
        gbc.gridy++; 
        tfRharga = new JTextField(); // GANTI JADI TEXTFIELD
        styleTextField(tfRharga);
        panel.add(tfRharga, gbc);

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
        btnUpdate.setForeground(Color.BLACK);
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

        toggleButtons(false);

        return panel;
    }

    // ================= LOGIC CRUD =================

    // 1. CREATE
    private void handleAdd() {
        if (!validateInput()) return;
        
        // Generate ID baru (Bisa pakai method static di DatabaseSampah)
        String newId = DatabaseSampah.generateSampahId(); 
        
        Sampah s = new Sampah(
            newId,
            tfRKategori.getText().trim(),
           Double.parseDouble(tfRharga.getText().trim())
        );

        String path = currentBank.getFileDaftarSampah(); // Ambil path dari Model Bank
        DatabaseSampah.addSampah(s, path);
        
        JOptionPane.showMessageDialog(this, "Berhasil menambah data!");
        resetForm();
        refreshTable();
    }

    // 2. UPDATE
    private void handleUpdate() {
        if (selectedIdSampah == null) return;
        if (!validateInput()) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ubah data ini?", "Edit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            
            Sampah r = new Sampah(
                selectedIdSampah, // ID Tetap sama (jangan diubah)
                tfRKategori.getText().trim(),
                Double.parseDouble(tfRharga.getText().trim())
            );

            String path = currentBank.getFileDaftarSampah();
            DatabaseSampah.updateSampah(r, path);

            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
            resetForm();
            refreshTable();
        }
    }

    // 3. DELETE
    private void handleDelete() {
        if (selectedIdSampah == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus Sampah ini?", "Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            
            String path = currentBank.getFileDaftarSampah();
            DatabaseSampah.deleteSampah(selectedIdSampah, path);

            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
            resetForm();
            refreshTable();
        }
    }

    // 4. READ (Refresh Table)
    private void refreshTable() {
        tableModel.setRowCount(0);
        String path = currentBank.getFileDaftarSampah();
        ArrayList<Sampah> list = DatabaseSampah.loadData(path);

        for (Sampah s : list) {
            tableModel.addRow(new Object[]{
                s.getIdSampah(),
                s.getJenis(),
                s.getHargaPerKg()
            });
        }
    }

    // --- Helpers ---
    private boolean validateInput() {
        if (tfRKategori.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data tidak boleh kosong!");
            return false;
        }
        try {
            Double.parseDouble(tfRharga.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga dan Stok harus angka!");
            return false;
        }
        return true;
    }

    private void resetForm() {
        tfRKategori.setText("");
        tfRharga.setText("");
        selectedIdSampah = null;
        tableSampah.clearSelection();
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
