package View.PenyetorPanels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import Database.DatabaseComplain;
import Model.BankSampah;
import Model.Complain;
import Model.Penyetor;
import Service.BankSampahService;
import Service.ComplainService;

public class KeluhanPanel extends JPanel {
    private Penyetor user;
    private BankSampah bank;
    private ComplainService cs;
    private BankSampahService bss;
    private ArrayList<Complain> daftarComplain;

    private JTextField judulField;
    private JTextArea isiArea;
    private JButton submitButton;
    private JTable riwayatTable;
    private DefaultTableModel tableModel;

    private final Color PRIMARY_COLOR = new Color(0x1F6B6A); 
    private final Color ACCENT_COLOR = new Color(0x67AE6E); 
    private final Color SOFT_BG = new Color(245, 245, 245);
    private final Color HEADER_BG = new Color(0x356A69); 
    private final Color HOVER_COLOR = new Color(0, 102, 0); 
    
    private final String ICON_COMPLAIN_PATH = "Trash_Bank\\\\src\\\\Asset\\\\Image\\\\suggestion-box.png";
    private final String ICON_HISTORY_PATH = "Trash_Bank\\\\src\\\\Asset\\\\Image\\\\history.png";
    private final int ICON_SIZE = 24;

    public KeluhanPanel(Penyetor user) {
        this.user = user;
        this.bss = new BankSampahService();
        this.cs = new ComplainService();
        this.bank = bss.getObjBankSampah(user.getIdBankSampah());
        daftarComplain = cs.daftarComplain(bank);

        initUI();
        loadRiwayatKeluhan();
    }

    private ImageIcon getScaledIcon(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (Exception e) {
            return null; 
        }
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(SOFT_BG);
        setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel header = new JLabel(" Kirim Keluhan", SwingConstants.CENTER);
        header.setIcon(getScaledIcon(ICON_COMPLAIN_PATH, 30, 30));
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setForeground(PRIMARY_COLOR.darker());
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(header, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 30));
        mainPanel.setBackground(SOFT_BG);
        add(mainPanel, BorderLayout.CENTER);

        JPanel formWrapper = createFormKeluhan();
        mainPanel.add(formWrapper, BorderLayout.NORTH);
        
        JPanel riwayatPanel = createRiwayatTable();
        mainPanel.add(riwayatPanel, BorderLayout.CENTER);
    }

    private JPanel createFormKeluhan() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(25, 25, 25, 25)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel judulLabel = new JLabel("Judul Keluhan:");
        judulLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        formPanel.add(judulLabel, gbc);

        judulField = new JTextField();
        judulField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        judulField.setPreferredSize(new Dimension(300, 40));
        judulField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY),
            new EmptyBorder(8, 10, 8, 10)));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        formPanel.add(judulField, gbc);

        JLabel isiLabel = new JLabel("Isi Keluhan:");
        isiLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(isiLabel, gbc);

        isiArea = new JTextArea(6, 30);
        isiArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        isiArea.setLineWrap(true);
        isiArea.setWrapStyleWord(true);
        isiArea.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        
        JScrollPane scrollPane = new JScrollPane(isiArea);
        scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
        scrollPane.setPreferredSize(new Dimension(300, 120)); 
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(scrollPane, gbc);
        
        submitButton = new JButton("Submit Keluhan");
        submitButton.setBackground(PRIMARY_COLOR);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitButton.setPreferredSize(new Dimension(180, 45));
        submitButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        submitButton.addActionListener(e -> tambahComplain());
        
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                submitButton.setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                submitButton.setBackground(ACCENT_COLOR);
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 5, 0, 5);
        formPanel.add(submitButton, gbc);
        
        return formPanel;
    }

    private JPanel createRiwayatTable() {
        JPanel riwayatPanel = new JPanel(new BorderLayout(0, 10));
        riwayatPanel.setBackground(SOFT_BG);

        JLabel riwayatTitle = new JLabel(" Riwayat Keluhan Anda");
        riwayatTitle.setIcon(getScaledIcon(ICON_HISTORY_PATH, ICON_SIZE, ICON_SIZE));
        riwayatTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        riwayatTitle.setForeground(PRIMARY_COLOR);
        riwayatPanel.add(riwayatTitle, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[] { "ID Complain", "Judul", "Isi", "Status", "Tanggapan" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        riwayatTable = new JTable(tableModel);
        riwayatTable.setFillsViewportHeight(true);
        riwayatTable.setRowHeight(30);
        riwayatTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        riwayatTable.setGridColor(new Color(240, 240, 240));
        riwayatTable.setSelectionBackground(new Color(230, 240, 230));

        JTableHeader header = riwayatTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_BG); 
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        
        for (int i = 0; i < riwayatTable.getColumnCount(); i++) {
            riwayatTable.getColumnModel().getColumn(i).setCellRenderer(new TextAreaRenderer());
        }

        JScrollPane tableScroll = new JScrollPane(riwayatTable);
        tableScroll.setBorder(new LineBorder(Color.LIGHT_GRAY));
        riwayatPanel.add(tableScroll, BorderLayout.CENTER);
        
        return riwayatPanel;
    }

    private void tambahComplain() {
        String judul = judulField.getText().trim();
        String isi = isiArea.getText().trim();

        if (judul.isEmpty() || isi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Judul dan isi keluhan tidak boleh kosong!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idUser = user.getIdPenyetor();
        String idBank = user.getIdBankSampah();
        String idComplain = DatabaseComplain.generateComplainId();

        Complain complainBaru = new Complain(idComplain, idUser, idBank, judul, isi);
        daftarComplain.add(complainBaru);

        DatabaseComplain.writeData(daftarComplain, bank.getFileComplain());
        DatabaseComplain.writeData(daftarComplain);

        JOptionPane.showMessageDialog(this, "Keluhan berhasil dikirim!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        judulField.setText("");
        isiArea.setText("");

        loadRiwayatKeluhan();
    }

    private void loadRiwayatKeluhan() {
        tableModel.setRowCount(0); 
        daftarComplain = DatabaseComplain.loadData(bank.getFileComplain());
        for (Complain c : daftarComplain) {
            if (c.getIdPenyetor().equals(user.getIdPenyetor())) {
                tableModel.addRow(
                        new Object[] { c.getIdComplain(), c.getJudul(), c.getIsi(), c.getStatus(), c.getTanggapanAdmin() });
            }
        }
        
        revalidate();
        repaint();
    }
}

class TextAreaRenderer extends JTextArea implements javax.swing.table.TableCellRenderer {
    public TextAreaRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
        setBorder(new EmptyBorder(5, 5, 5, 5)); 
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        setText(value != null ? value.toString() : "");
        setFont(table.getFont());

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        int tableWidth = table.getColumnModel().getColumn(column).getWidth();
        setSize(new Dimension(tableWidth, Short.MAX_VALUE));
        int preferredHeight = getPreferredSize().height;
        
        preferredHeight = Math.max(preferredHeight, 30); 
        
        if (table.getRowHeight(row) != preferredHeight) {
            table.setRowHeight(row, preferredHeight);
        }

        return this;
    }
}