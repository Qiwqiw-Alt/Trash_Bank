package View.AdminPanels;

import Database.DatabaseReward;
import Model.BankSampah;
import Model.Reward;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ManajemenRewardPanel extends JPanel {

    private BankSampah currentBank;
    private String selectedIdReward = null; 

    private JTextField tfRname;
    private JTextArea taRdescription;
    private JTextField tfRpoin;
    private JTextField tfStock;
    
    private JButton btnAdd; 
    private JButton btnUpdate; 
    private JButton btnDelete; 
    private JButton btnClear; 

    private JTable tableReward;
    private DefaultTableModel tableModel;

    private final Color PRIMARY_TEAL = new Color(0x316C6C); 
    private final Color HEADER_BG = PRIMARY_TEAL.darker(); 
    private final Color SOFT_BG = new Color(245, 245, 245); 
    private final Color ACCENT_GREEN = new Color(76, 175, 80); 
    private final Color TEXT_LIGHT = Color.WHITE; 
    private final Color TEXT_DARK = new Color(50, 50, 50); 

    private final Font FONT_TITLE = new Font("Fredoka", Font.BOLD, 24);
    private final Font FONT_HEADER = new Font("Fredoka", Font.BOLD, 18);
    private final Font FONT_LABEL = new Font("Fredoka", Font.BOLD, 14);
    private final Font FONT_INPUT = new Font("Fredoka", Font.PLAIN, 14);
    private final Font FONT_TABLE = new Font("Fredoka", Font.PLAIN, 14);
    
    private final String REWARD_ICON = "🎁";


    public ManajemenRewardPanel(BankSampah bankSampah) {
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

        JLabel lblMainTitle = new JLabel(REWARD_ICON + " Manajemen Reward");
        lblMainTitle.setFont(FONT_TITLE);
        lblMainTitle.setForeground(PRIMARY_TEAL);
        lblMainTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(lblMainTitle);
        mainContent.add(Box.createVerticalStrut(20));

        mainContent.add(createTopSection());
        
        JSeparator sep = new JSeparator();
        sep.setForeground(Color.LIGHT_GRAY);
        mainContent.add(Box.createVerticalStrut(25));
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
        panel.setBackground(PRIMARY_TEAL); 
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x2A5D5D), 1),
            new EmptyBorder(20, 20, 20, 20) 
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("Daftar Reward");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(TEXT_LIGHT); 
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(15));

        JPanel alertPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        Color INFO_BG_MOD = new Color(59, 117, 117); 
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
        panel.add(Box.createVerticalStrut(15));

        String[] columns = {"ID", "Nama Reward", "Deskripsi", "Poin", "Stok"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tableReward = new JTable(tableModel);
        styleTable(tableReward);
        
        JScrollPane scrollTable = new JScrollPane(tableReward);
        scrollTable.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scrollTable.getViewport().setBackground(Color.WHITE); 
        scrollTable.setPreferredSize(new Dimension(800, 250));
        scrollTable.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(scrollTable);

        tableReward.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableReward.getSelectedRow();
                if (row != -1) {
                    selectedIdReward = tableModel.getValueAt(row, 0).toString();
                    String nama = tableModel.getValueAt(row, 1).toString();
                    String desc = tableModel.getValueAt(row, 2).toString();
                    String poin = tableModel.getValueAt(row, 3).toString();
                    String stok = tableModel.getValueAt(row, 4).toString();

                    tfRname.setText(nama);
                    taRdescription.setText(desc);
                    tfRpoin.setText(poin);
                    tfStock.setText(stok);

                    toggleButtons(true);
                }
            }
        });

        return panel;
    }

    private JPanel createBottomSection() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(PRIMARY_TEAL); 
        panel.setBorder(BorderFactory.createCompoundBorder( 
            BorderFactory.createLineBorder(new Color(0x2A5D5D), 1), 
            new EmptyBorder(25, 25, 25, 25) 
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel lblFormTitle = new JLabel("Form Kelola Reward");
        lblFormTitle.setFont(FONT_HEADER);
        lblFormTitle.setForeground(TEXT_LIGHT); 
        panel.add(lblFormTitle, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 15, 0); 
        panel.add(Box.createVerticalStrut(5), gbc);
        gbc.insets = new Insets(8, 0, 8, 0);

        gbc.gridy++; panel.add(createLabel("Nama Reward:", TEXT_LIGHT), gbc); 
        gbc.gridy++; tfRname = new JTextField(); styleTextField(tfRname); panel.add(tfRname, gbc);

        gbc.gridy++; panel.add(createLabel("Deskripsi:", TEXT_LIGHT), gbc); 
        gbc.gridy++; 
        taRdescription = new JTextArea(3, 20); 
        styleTextArea(taRdescription);
        panel.add(new JScrollPane(taRdescription), gbc);

        gbc.gridy++;
        JPanel rowNumbers = new JPanel(new GridLayout(1, 2, 30, 0)); 
        rowNumbers.setBackground(PRIMARY_TEAL); 
        
        JPanel p1 = new JPanel(new BorderLayout(0, 5)); p1.setBackground(PRIMARY_TEAL); 
        p1.add(createLabel("Poin:", TEXT_LIGHT), BorderLayout.NORTH); 
        tfRpoin = new JTextField(); styleTextField(tfRpoin); p1.add(tfRpoin, BorderLayout.CENTER);
        
        JPanel p2 = new JPanel(new BorderLayout(0, 5)); p2.setBackground(PRIMARY_TEAL); 
        p2.add(createLabel("Stok:", TEXT_LIGHT), BorderLayout.NORTH); 
        tfStock = new JTextField(); styleTextField(tfStock); p2.add(tfStock, BorderLayout.CENTER);

        rowNumbers.add(p1); rowNumbers.add(p2);
        panel.add(rowNumbers, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(30, 0, 0, 0); 
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0)); 
        btnPanel.setBackground(PRIMARY_TEAL); 
        
        btnAdd = new JButton("Tambah");
        styleButton(btnAdd, ACCENT_GREEN); 
        btnAdd.addActionListener(e -> handleAdd());

        btnUpdate = new JButton("Edit");
        styleButton(btnUpdate, new Color(255, 193, 7)); 
        btnUpdate.setForeground(Color.BLACK); 
        btnUpdate.addActionListener(e -> handleUpdate());

        btnDelete = new JButton("Hapus");
        styleButton(btnDelete, new Color(220, 53, 69)); 
        btnDelete.addActionListener(e -> handleDelete());

        btnClear = new JButton("Bersihkan Form");
        styleButton(btnClear, Color.GRAY);
        btnClear.setPreferredSize(new Dimension(150, 35)); 
        btnClear.addActionListener(e -> resetForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        panel.add(btnPanel, gbc);

        toggleButtons(false);

        return panel;
    }

    private void handleAdd() {
        if (!validateInput()) return;
        String newId = DatabaseReward.generateRewardId(); 
        Reward r = new Reward(
            newId, tfRname.getText().trim(), taRdescription.getText().trim(),
            Integer.parseInt(tfRpoin.getText().trim()), Integer.parseInt(tfStock.getText().trim())
        );
        String path = currentBank.getFileReward();
        DatabaseReward.addReward(r, path);
        JOptionPane.showMessageDialog(this, "Berhasil menambah data!");
        resetForm();
        refreshTable();
    }

    private void handleUpdate() {
        if (selectedIdReward == null) return;
        if (!validateInput()) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ubah data ini?", "Edit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Reward r = new Reward(
                selectedIdReward, tfRname.getText().trim(), taRdescription.getText().trim(),
                Integer.parseInt(tfRpoin.getText().trim()), Integer.parseInt(tfStock.getText().trim())
            );
            String path = currentBank.getFileReward();
            DatabaseReward.updateReward(r, path);
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
            resetForm();
            refreshTable();
        }
    }

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

    private void refreshTable() {
        tableModel.setRowCount(0);
        String path = currentBank.getFileReward();
        ArrayList<Reward> list = DatabaseReward.loadData(path);
        for (Reward r : list) {
            tableModel.addRow(new Object[]{
                r.getIdReward(), r.getNamaHadiah(), r.getDeskripsi(), r.getHargaTukar(), r.getStok()
            });
        }
    }

    private boolean validateInput() {
        if (tfRname.getText().isEmpty() || tfRpoin.getText().isEmpty() || tfStock.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data tidak boleh kosong!");
            return false;
        }
        try {
            int poin = Integer.parseInt(tfRpoin.getText());
            int stok = Integer.parseInt(tfStock.getText());
            if (poin < 0 || stok < 0) {
                 JOptionPane.showMessageDialog(this, "Poin dan Stok tidak boleh negatif!");
                 return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Poin dan Stok harus angka!");
            return false;
        }
        return true;
    }

    private void resetForm() {
        tfRname.setText(""); taRdescription.setText(""); tfRpoin.setText(""); tfStock.setText("");
        selectedIdReward = null;
        tableReward.clearSelection();
        toggleButtons(false); 
    }

    private void toggleButtons(boolean isEditMode) {
        btnAdd.setEnabled(!isEditMode); 
        btnUpdate.setEnabled(isEditMode); 
        btnDelete.setEnabled(isEditMode); 
    }

    private JLabel createLabel(String text, Color textColor) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(textColor);
        return lbl;
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
    
    private void styleTextArea(JTextArea ta) {
        ta.setFont(FONT_INPUT);
        ta.setBorder(BorderFactory.createCompoundBorder(
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
    
    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(FONT_TABLE);
        table.setSelectionBackground(new Color(220, 240, 255));
        table.setGridColor(new Color(230, 230, 230));
        table.setShowVerticalLines(false); 
        table.setShowHorizontalLines(true); 
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(HEADER_BG); 
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Fredoka", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
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
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(defaultRenderer);
        }
        
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); 
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); 
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); 

        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    }
}