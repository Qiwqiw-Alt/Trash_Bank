
package View.AdminPanels;

import Model.Admin;
import Model.BankSampah;
import Model.Complain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.SwingConstants;

import Controller.ComplainController;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class EvaluasiKomplainPanel extends JPanel {

    private Admin currentAdmin;
    private BankSampah currentBank;
    private String selectedIdComplain = null;
    private ArrayList<Complain> listComplainCache;


    private JTable tableComplain;
    private DefaultTableModel tableModel;

    private JLabel lblPengirim;
    private JLabel lblTanggal;
    private JTextField tfJudul;
    private JTextArea taIsiKomplain;
    private JTextArea taTanggapanAdmin;

    private JComboBox<Complain.Status> cbStatus; 
    private JButton btnSimpan;

    private final Color PRIMARY_COLOR = new Color(49, 108, 108); 
    private final Color HEADER_BG = PRIMARY_COLOR.darker(); 
    private final Color SOFT_BG = new Color(245, 245, 245); 
    private final Color TEXT_LIGHT = Color.WHITE; 
    private final Color TEXT_DARK = new Color(50, 50, 50);
    private final Color READONLY_BG = new Color(220, 230, 230); 
    
    private final Font FONT_TITLE = new Font("Fredoka", Font.BOLD, 22);
    private final Font FONT_HEADER = new Font("Fredoka", Font.BOLD, 18);
    private final Font FONT_LABEL = new Font("Fredoka", Font.BOLD, 14);
    private final Font FONT_TABLE = new Font("Fredoka", Font.PLAIN, 13);
    private final Font FONT_INPUT = new Font("Fredoka", Font.PLAIN, 14);


    public EvaluasiKomplainPanel(Admin admin, BankSampah bankSampah) {
        this.currentAdmin = admin;
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

        mainContent.add(createTableSection());
        mainContent.add(Box.createVerticalStrut(25));
        JSeparator sep = new JSeparator();
        sep.setForeground(Color.LIGHT_GRAY);
        mainContent.add(sep);
        mainContent.add(Box.createVerticalStrut(25));
        
        mainContent.add(createFormSection());

        JScrollPane mainScroll = new JScrollPane(mainContent);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScroll, BorderLayout.CENTER);
    }

    private JPanel createTableSection() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(PRIMARY_COLOR); 
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HEADER_BG, 1),
            new EmptyBorder(20, 20, 20, 20) 
        ));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        
        JLabel lblTitle = new JLabel("Daftar Komplain Masuk");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(TEXT_LIGHT);
        
        JButton btnRefresh = new JButton("Refresh");
        styleButton(btnRefresh, HEADER_BG); 
        btnRefresh.setPreferredSize(new Dimension(120, 35));
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
        tableComplain.setRowHeight(35);
        tableComplain.setFont(FONT_TABLE);
        tableComplain.setSelectionBackground(new Color(220, 240, 255));
        tableComplain.setGridColor(new Color(230, 230, 230));
        tableComplain.setShowVerticalLines(false);
        tableComplain.setShowHorizontalLines(true);

        JTableHeader header = tableComplain.getTableHeader();
        header.setBackground(HEADER_BG);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Fredoka", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (column == 4) { 
                    setHorizontalAlignment(SwingConstants.CENTER);
                    if (value != null) {
                         setText(value.toString().replace("_", " "));
                    }
                } else if (column == 0 || column == 2) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(TEXT_DARK);
                }
                return c;
            }
        };

        for (int i = 0; i < tableComplain.getColumnCount(); i++) {
            tableComplain.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(tableComplain);
        scrollPane.setPreferredSize(new Dimension(800, 250));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
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

    private JPanel createFormSection() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(PRIMARY_COLOR); 
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR.brighter(), 1), 
            BorderFactory.createTitledBorder(
                new LineBorder(PRIMARY_COLOR.brighter()), 
                "Evaluasi & Update Status ", 
                0, 2, FONT_HEADER, TEXT_LIGHT))); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;

        gbc.weightx = 0.5;
        lblPengirim = new JLabel("Pengirim: -");
        lblPengirim.setFont(new Font("Fredoka", Font.BOLD, 14));
        lblPengirim.setForeground(TEXT_LIGHT); 
        panel.add(lblPengirim, gbc);

        gbc.gridx = 1;
        lblTanggal = new JLabel("Tanggal: -");
        lblTanggal.setFont(new Font("Fredoka", Font.BOLD, 14));
        lblTanggal.setForeground(TEXT_LIGHT); 
        panel.add(lblTanggal, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.weightx = 1.0;
        panel.add(createLabel("Judul Masalah:"), gbc); 
        gbc.gridy++;
        tfJudul = new JTextField(); tfJudul.setEditable(false);
        styleTextField(tfJudul, true); 
        panel.add(tfJudul, gbc);

        gbc.gridy++;
        panel.add(createLabel("Isi Laporan:"), gbc); 
        gbc.gridy++;
        taIsiKomplain = new JTextArea(3, 20); taIsiKomplain.setEditable(false);
        taIsiKomplain.setLineWrap(true); taIsiKomplain.setWrapStyleWord(true);
        styleTextArea(taIsiKomplain, true); 
        panel.add(new JScrollPane(taIsiKomplain), gbc);

        gbc.gridy++;
        panel.add(createLabel("Tanggapan Admin:"), gbc); 
        gbc.gridy++;
        taTanggapanAdmin = new JTextArea(3, 20);
        taTanggapanAdmin.setLineWrap(true); taTanggapanAdmin.setWrapStyleWord(true);
        styleTextArea(taTanggapanAdmin, false); 
        panel.add(new JScrollPane(taTanggapanAdmin), gbc);


        gbc.gridy++;
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        actionPanel.setBackground(PRIMARY_COLOR); 

        actionPanel.add(createLabel("Update Status: ")); 
        
        cbStatus = new JComboBox<>(Complain.Status.values());
        cbStatus.setPreferredSize(new Dimension(200, 35));
        cbStatus.setFont(FONT_INPUT);
        cbStatus.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        actionPanel.add(cbStatus);

        actionPanel.add(Box.createHorizontalStrut(20));

        btnSimpan = new JButton("Simpan Perubahan");
        styleButton(btnSimpan, HEADER_BG); 
        btnSimpan.addActionListener(e -> handleUpdateStatus());
        actionPanel.add(btnSimpan);

        panel.add(actionPanel, gbc);

        toggleForm(false);
        return panel;
    }

    private void handleUpdateStatus() {
        if (selectedIdComplain == null) return;

        Complain.Status newStatus = (Complain.Status) cbStatus.getSelectedItem();
        String tanggapan = taTanggapanAdmin.getText();

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Ubah status menjadi " + newStatus + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            
            ComplainController.getService().updateComplain(selectedIdComplain, newStatus, tanggapan, currentBank);

            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
            
            refreshTable();
            
            toggleForm(false);
            taTanggapanAdmin.setText("");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        this.listComplainCache = ComplainController.getService().daftarComplain(currentBank);
        for (Complain c : listComplainCache) {
            tableModel.addRow(new Object[]{
                c.getIdComplain(), 
                c.getFormattedTime(), 
                c.getIdPenyetor(), 
                c.getJudul(), 
                c.getStatus().name()
            });
        }
    }

    private void loadDetailToForm(int row) {
        selectedIdComplain = tableModel.getValueAt(row, 0).toString();
        String tgl = tableModel.getValueAt(row, 1).toString();
        String sender = tableModel.getValueAt(row, 2).toString();
        String judul = tableModel.getValueAt(row, 3).toString();
        
        Complain.Status currentStat = (Complain.Status) tableModel.getValueAt(row, 4);

        lblPengirim.setText("Pengirim: " + sender);
        lblTanggal.setText("Tanggal: " + tgl);
        tfJudul.setText(judul);
        
        cbStatus.setSelectedItem(currentStat);
        
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
        btn.setFont(new Font("Fredoka", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(180, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_LIGHT); 
        return lbl;
    }
    
    private void styleTextField(JTextField tf, boolean readOnly) {
        tf.setFont(FONT_INPUT);
        tf.setPreferredSize(new Dimension(tf.getPreferredSize().width, 35));
        tf.setEditable(!readOnly);
        if (readOnly) {
             tf.setBackground(READONLY_BG);
             tf.setForeground(TEXT_DARK);
        } else {
             tf.setBackground(Color.WHITE);
             tf.setForeground(TEXT_DARK); 
        }
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private void styleTextArea(JTextArea ta, boolean readOnly) {
        ta.setFont(FONT_INPUT);
        ta.setEditable(!readOnly);
        ta.setLineWrap(true); 
        ta.setWrapStyleWord(true);
        if (readOnly) {
             ta.setBackground(READONLY_BG);
             ta.setForeground(TEXT_DARK);
        } else {
             ta.setBackground(Color.WHITE);
             ta.setForeground(TEXT_DARK); 
        }
        ta.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
    }
}