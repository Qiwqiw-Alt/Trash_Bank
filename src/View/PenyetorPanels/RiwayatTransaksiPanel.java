package View.PenyetorPanels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Database.DatabaseTransaksi;
import Model.BankSampah;
import Model.Penyetor;
import Model.Transaksi;
import Service.BankSampahService;

import java.awt.*;
import java.util.ArrayList;

public class RiwayatTransaksiPanel extends JPanel {

    BankSampah bank;
    BankSampahService bss = new BankSampahService();

    private final String[] COLUMN_NAMES = {"ID Transaksi", "Tanggal", "Total Berat", "Total Harga", "Status"};

    public RiwayatTransaksiPanel(Penyetor user) {

        bank = bss.getObjBankSampah(user.getIdBankSampah());

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Riwayat Transaksi", SwingConstants.CENTER); 
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        String iconPath = "src//Asset//Image//history.png"; 
        Icon headerIcon = null;
        
        try {
            headerIcon = new ImageIcon(getClass().getResource(iconPath));
            
            if (headerIcon != null && headerIcon.getIconWidth() > 0) {
                title.setIcon(headerIcon);
                title.setIconTextGap(10); 
            }

        } catch (Exception e) {
        }

        add(title, BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(245, 245, 245));
        mainContent.setBorder(new EmptyBorder(10, 30, 30, 30)); 

        JPanel tableTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tableTitlePanel.setBackground(new Color(245, 245, 245));
        
        JLabel tableTitle = new JLabel("Riwayat Transaksi Anda");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 16));
        
        if (headerIcon != null && headerIcon.getIconWidth() > 0) {
             tableTitle.setIcon(headerIcon);
             tableTitle.setIconTextGap(5);
        } else {
             tableTitle.setText("Riwayat Transaksi Anda"); 
        }

        tableTitlePanel.add(tableTitle);
        mainContent.add(tableTitlePanel, BorderLayout.NORTH);

        ArrayList<Transaksi> allTransaksi = DatabaseTransaksi.loadData(bank.getFileTransaksi());
        ArrayList<Transaksi> userTransaksi = new ArrayList<>();

        for (Transaksi trx : allTransaksi) {
            if (trx.getIdPenyetor().equals(user.getIdPenyetor())) {
                userTransaksi.add(trx);
            }
        }
        
        Object[][] tableData = new Object[userTransaksi.size()][COLUMN_NAMES.length];
        for (int i = 0; i < userTransaksi.size(); i++) {
            Transaksi trx = userTransaksi.get(i);
            tableData[i][0] = trx.getIdTransaksi();
            tableData[i][1] = trx.getTanggal();
            tableData[i][2] = trx.getTotalBerat() + " kg";
            tableData[i][3] = "Rp " + trx.getTotalHarga();
            tableData[i][4] = trx.getStatus().toString(); 
        }

        DefaultTableModel model = new DefaultTableModel(tableData, COLUMN_NAMES) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(220, 240, 255));
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(60, 140, 135)); 
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        header.setBorder(null);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        class StatusRenderer extends DefaultTableCellRenderer {
            public StatusRenderer() {
                setHorizontalAlignment(JLabel.CENTER);
            }
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    try {
                        Transaksi.Status status = Transaksi.Status.valueOf(value.toString());
                        c.setForeground(setStatusColor(status));
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } catch (IllegalArgumentException e) {
                        c.setForeground(Color.BLACK); 
                    }
                }
                return c;
            }
        }
        
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); 
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); 
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); 
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer()); 
        
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1)); 
        scrollPane.getViewport().setBackground(Color.WHITE); 

        mainContent.add(scrollPane, BorderLayout.CENTER);
        
        if (userTransaksi.isEmpty()) {
            JLabel emptyLabel = new JLabel("Belum ada riwayat transaksi.", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
            mainContent.add(emptyLabel, BorderLayout.SOUTH);
        }

        add(mainContent, BorderLayout.CENTER);
    }

    private Color setStatusColor(Transaksi.Status status) {
        switch (status) {
            case DITERIMA:
                return new Color(34, 139, 34); 
            case DITOLAK:
                return new Color(178, 34, 34); 
            case SEDANG_DITINJAU:
                return new Color(255, 165, 0); 
            default:
                return Color.GRAY;
        }
    }
}