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
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
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
    private String selectedIdSampah = null; 

    private JTextField tfRKategori;
    private JTextField tfRharga;
    
    private JButton btnAdd; 
    private JButton btnUpdate; 
    private JButton btnDelete; 
    private JButton btnClear; 

    private JTable tableSampah;
    private DefaultTableModel tableModel;

    private final Color PRIMARY_COLOR = new Color(0x316C6C); 
    private final Color HEADER_BG = PRIMARY_COLOR.darker(); 
    private final Color SOFT_BG = new Color(245, 245, 245); 
    private final Color GREEN_ACCENT = new Color(40, 167, 69); 
    private final Color COLOR_EDIT = new Color(255, 193, 7); 
    private final Color COLOR_DELETE = new Color(220, 53, 69); 
    private final Color TEXT_LIGHT = Color.WHITE; 
    private final Color TEXT_DARK = new Color(50, 50, 50); 
    private final Color INFO_BG_MOD = new Color(59, 117, 117); 

    private final Font FONT_TITLE = new Font("Fredoka", Font.BOLD, 22);
    private final Font FONT_HEADER = new Font("Fredoka", Font.BOLD, 18);
    private final Font FONT_LABEL = new Font("Fredoka", Font.BOLD, 14);
    private final Font FONT_INPUT = new Font("Fredoka", Font.PLAIN, 14);
    private final Font FONT_TABLE = new Font("Fredoka", Font.PLAIN, 13);


    public ManajemenSampahPanel(BankSampah bankSampah){
        this.currentBank = bankSampah;
        initLayout();
        refreshTable();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(SOFT_BG);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(SOFT_BG);
        mainContent.setBorder(new EmptyBorder(25, 30, 25, 30));

        mainContent.add(createTopSection());
        
        mainContent.add(Box.createVerticalStrut(25));
        JSeparator sep = new JSeparator();
        sep.setForeground(Color.LIGHT_GRAY);
        mainContent.add(sep);
        mainContent.add(Box.createVerticalStrut(25));

        mainContent.add(createBottomSection());

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createTopSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PRIMARY_COLOR); 
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x2A5D5D), 1),
            new EmptyBorder(25, 25, 25, 25) 
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);


        JLabel lblMainTitle = new JLabel("Daftar Kategori Sampah");
        lblMainTitle.setFont(FONT_TITLE);
        lblMainTitle.setForeground(TEXT_LIGHT); 
        lblMainTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblMainTitle);
        panel.add(Box.createVerticalStrut(15));

        JPanel alertPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        alertPanel.setBackground(INFO_BG_MOD); 
        alertPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(50, 100, 100), 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));
        alertPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        JLabel lblAlert = new JLabel("Info: Klik baris pada tabel untuk Mengedit atau Menghapus data.");
        lblAlert.setForeground(TEXT_LIGHT); 
        lblAlert.setFont(new Font("Fredoka", Font.ITALIC, 13));
        alertPanel.add(lblAlert);
        alertPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(alertPanel);
        panel.add(Box.createVerticalStrut(20));

        String[] columns = {"ID", "Kategori Sampah", "Harga per Kg"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableSampah = new JTable(tableModel);
        
        tableSampah.setRowHeight(30);
        tableSampah.setFont(FONT_TABLE);
        tableSampah.setSelectionBackground(new Color(220, 240, 255)); 
        tableSampah.setGridColor(new Color(230, 230, 230));
        tableSampah.setShowVerticalLines(false); 
        tableSampah.setShowHorizontalLines(true); 
        
        JTableHeader header = tableSampah.getTableHeader();
        header.setBackground(HEADER_BG); 
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Fredoka", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        
        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                c.setForeground(TEXT_DARK);
                
                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                } else {
                    c.setBackground(table.getSelectionBackground());
                }
                return c;
            }
        };
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        for (int i = 0; i < tableSampah.getColumnCount(); i++) {
            tableSampah.getColumnModel().getColumn(i).setCellRenderer(defaultRenderer);
        }
        
        tableSampah.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableSampah.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);


        JScrollPane scrollTable = new JScrollPane(tableSampah);
        scrollTable.setPreferredSize(new Dimension(800, 300));
        scrollTable.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scrollTable.getViewport().setBackground(Color.WHITE); 
        scrollTable.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(scrollTable);

        tableSampah.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableSampah.getSelectedRow();
                if (row != -1) {
                    selectedIdSampah = tableModel.getValueAt(row, 0).toString();
                    String kategori = tableModel.getValueAt(row, 1).toString();
                    String harga = tableModel.getValueAt(row, 2).toString();

                    tfRKategori.setText(kategori);
                    tfRharga.setText(harga);

                    toggleButtons(true);
                }
            }
        });

        return panel;
    }

    private JPanel createBottomSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR); 
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x2A5D5D), 1),
            new EmptyBorder(25, 25, 25, 25) 
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblFormTitle = new JLabel("Form Kelola Kategori Sampah");
        lblFormTitle.setFont(FONT_HEADER);
        lblFormTitle.setForeground(TEXT_LIGHT); 
        panel.add(lblFormTitle, BorderLayout.NORTH);

        JPanel formInputPanel = new JPanel(new GridBagLayout());
        formInputPanel.setBackground(PRIMARY_COLOR); 
        formInputPanel.setBorder(new EmptyBorder(15, 0, 15, 0)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 20); 
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;
        
        JLabel lblKategori = new JLabel("Kategori Sampah:");
        lblKategori.setFont(FONT_LABEL);
        lblKategori.setForeground(TEXT_LIGHT); 
        gbc.weightx = 0.0; 
        formInputPanel.add(lblKategori, gbc);
        
        gbc.gridx = 1; 
        gbc.weightx = 1.0; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tfRKategori = new JTextField(); 
        styleTextField(tfRKategori); 
        formInputPanel.add(tfRKategori, gbc);
        
        gbc.gridy++; gbc.gridx = 0; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        
        JLabel lblHarga = new JLabel("Harga per Kg (Rp):");
        lblHarga.setFont(FONT_LABEL);
        lblHarga.setForeground(TEXT_LIGHT); 
        formInputPanel.add(lblHarga, gbc);
        
        gbc.gridx = 1; 
        gbc.weightx = 1.0; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tfRharga = new JTextField(); 
        styleTextField(tfRharga);
        formInputPanel.add(tfRharga, gbc);

        panel.add(formInputPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setBackground(PRIMARY_COLOR); 

        btnAdd = new JButton("Tambah");
        styleButton(btnAdd, GREEN_ACCENT); 
        btnAdd.addActionListener(e -> handleAdd());

        btnUpdate = new JButton("Edit");
        styleButton(btnUpdate, COLOR_EDIT); 
        btnUpdate.setForeground(Color.BLACK); 
        btnUpdate.addActionListener(e -> handleUpdate());

        btnDelete = new JButton("Hapus");
        styleButton(btnDelete, COLOR_DELETE); 
        btnDelete.addActionListener(e -> handleDelete());

        btnClear = new JButton("Bersihkan Form");
        styleButton(btnClear, Color.GRAY);
        btnClear.setPreferredSize(new Dimension(150, 35));
        btnClear.addActionListener(e -> resetForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        panel.add(btnPanel, BorderLayout.SOUTH);

        toggleButtons(false);

        return panel;
    }

    private void handleAdd() {
        if (!validateInput()) return;
        
        String newId = DatabaseSampah.generateSampahId(); 
        
        Sampah s = new Sampah(
            newId,
            tfRKategori.getText().trim(),
            Double.parseDouble(tfRharga.getText().trim())
        );

        String path = currentBank.getFileDaftarSampah(); 
        Database.DatabaseSampah.addSampah(s, path);
        
        JOptionPane.showMessageDialog(this, "Berhasil menambah data!");
        resetForm();
        refreshTable();
    }

    private void handleUpdate() {
        if (selectedIdSampah == null) return;
        if (!validateInput()) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ubah data ini?", "Edit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            
            Sampah r = new Sampah(
                selectedIdSampah, 
                tfRKategori.getText().trim(),
                Double.parseDouble(tfRharga.getText().trim())
            );

            String path = currentBank.getFileDaftarSampah();
            Database.DatabaseSampah.updateSampah(r, path);

            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
            resetForm();
            refreshTable();
        }
    }

    private void handleDelete() {
        if (selectedIdSampah == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus Sampah ini?", "Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            
            String path = currentBank.getFileDaftarSampah();
            Database.DatabaseSampah.deleteSampah(selectedIdSampah, path);

            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
            resetForm();
            refreshTable();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        String path = currentBank.getFileDaftarSampah();
        ArrayList<Sampah> list = Database.DatabaseSampah.loadData(path);

        for (Sampah s : list) {
            tableModel.addRow(new Object[]{
                s.getIdSampah(),
                s.getJenis(),
                s.getHargaPerKg()
            });
        }
    }

    private boolean validateInput() {
        if (tfRKategori.getText().isEmpty() || tfRharga.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kategori dan Harga tidak boleh kosong!");
            return false;
        }
        try {
            double harga = Double.parseDouble(tfRharga.getText().trim());
            if (harga <= 0) {
                 JOptionPane.showMessageDialog(this, "Harga harus lebih dari nol!");
                 return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga per Kg harus berupa angka!");
            return false;
        }
        return true;
    }

    private void resetForm() {
        tfRKategori.setText("");
        tfRharga.setText("");
        selectedIdSampah = null;
        tableSampah.clearSelection();
        toggleButtons(false); 
    }

    private void toggleButtons(boolean isEditMode) {
        btnAdd.setEnabled(!isEditMode); 
        btnUpdate.setEnabled(isEditMode); 
        btnDelete.setEnabled(isEditMode); 
    }

    private void styleTextField(JTextField tf) {
        tf.setFont(FONT_INPUT);
        tf.setPreferredSize(new Dimension(200, 35));
        tf.setMaximumSize(new Dimension(Short.MAX_VALUE, 35)); 
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(150, 150, 150), 1), 
            new EmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Fredoka", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setPreferredSize(new Dimension(100, 35));
    }
}