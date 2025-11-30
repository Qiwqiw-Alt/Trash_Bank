package View.PenyetorPanels;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import Database.DatabasePenukaranReward;
import Database.DatabaseTransaksi;

import java.awt.*;
import java.util.ArrayList;

import Model.Penyetor;
import Model.Transaksi;
import Service.BankSampahService;
import Service.SetoranPenyetorService;

public class PenyetorHomePanel extends JPanel {

    private BankSampahService bss = new BankSampahService();
    private SetoranPenyetorService sps = new SetoranPenyetorService();
    private ArrayList<Transaksi> listTransaksi;
    private Penyetor penyetor;

    private final Color GREEN_PRIMARY = new Color(0x356A69); 
    private final Color ACCENT_COLOR = new Color(0x67AE6E); 
    private final Color SOFT_GREY = new Color(245, 245, 245);
    
    private final String SETORAN_ICON_PATH = "Trash_Bank\\\\src\\\\Asset\\\\Image\\\\transaction.png"; 
    private final String POIN_ICON_PATH = "Trash_Bank\\\\src\\\\Asset\\\\Image\\\\money.png";

    public PenyetorHomePanel(Penyetor user) {
        this.penyetor = user;

        setLayout(new BorderLayout());
        setBackground(SOFT_GREY); 

        this.listTransaksi = sps.getDaftarTransaksi(penyetor);
        user.setTotalSetoran(this.listTransaksi.size());
        double totalPoinPendapatanTransaksi = DatabaseTransaksi.hitungPoinPenyetor(user);
        double totalPoinPengeluranTukarReward = DatabasePenukaranReward.hitungPoinPenyetor(user);
        double totalPoinUser = totalPoinPendapatanTransaksi - totalPoinPengeluranTukarReward;

        if (totalPoinUser < 0) {
            user.setTotalPoin(0);
        } else {
            user.setTotalPoin(totalPoinUser);
        }

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        container.setBackground(SOFT_GREY);

        container.add(createInfoCards(penyetor));
        container.add(Box.createVerticalStrut(30));
        container.add(createTablePanel(penyetor));

        add(container, BorderLayout.CENTER);
    }

    private JPanel createInfoCards(Penyetor user) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 30, 0));
        panel.setOpaque(false);

        panel.add(createSingleCard(
            "Total Setoran", 
            String.valueOf(listTransaksi.size()) + " Transaksi", 
            SETORAN_ICON_PATH, 
            ACCENT_COLOR)); 

        panel.add(createSingleCard(
            "Total Poin", 
            String.format("%.1f", user.getTotalPoin()) + " Poin", 
            POIN_ICON_PATH, 
            GREEN_PRIMARY));

        return panel;
    }

    private JPanel createSingleCard(String title, String value, String iconPath, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(Color.WHITE);

        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(25, 25, 25, 25)));

        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(60, 60));
        
        JLabel iconLabel = createImageLabel(iconPath, 40, 40, title.substring(0, 1)); 
        iconLabel.setOpaque(true);
        iconLabel.setBackground(accentColor.brighter());
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        iconPanel.add(iconLabel);
        card.add(iconPanel, BorderLayout.WEST);


        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(new EmptyBorder(5, 0, 0, 0));

        JLabel lblTitle = new JLabel(title.toUpperCase());
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitle.setForeground(new Color(150, 150, 150));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 28));
        lblValue.setForeground(accentColor.darker()); 
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(lblTitle);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(lblValue);
        
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }
    
    private JLabel createImageLabel(String path, int width, int height, String placeholderText) {
        JLabel imageLabel;
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH); 
            imageLabel = new JLabel(new ImageIcon(scaledImg));
        } catch (Exception e) {
            imageLabel = new JLabel(placeholderText, SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(width, height));
            imageLabel.setBorder(BorderFactory.createLineBorder(GREEN_PRIMARY, 1));
            imageLabel.setFont(new Font("Arial", Font.BOLD, 12));
            imageLabel.setOpaque(true);
            imageLabel.setBackground(new Color(230, 230, 230));
        }
        return imageLabel;
    }

    private JPanel createTablePanel(Penyetor user) {

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel title = new JLabel("Riwayat Transaksi Terakhir");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(GREEN_PRIMARY.darker());
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        wrapper.add(title, BorderLayout.NORTH);

        String[] columns = { "ID Transaksi", "Tanggal", "Berat (kg)", "Harga (Rp)", "Status" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        int limit = Math.min(listTransaksi.size(), 5);
        for (int i = 0; i < limit; i++) {
            Transaksi trx = listTransaksi.get(listTransaksi.size() - 1 - i); 
            
            if (trx.getIdPenyetor().equals(user.getIdPenyetor())) {
                Object[] row = {
                        trx.getIdTransaksi(),
                        trx.getTanggal(),
                        String.format("%.2f", trx.getTotalBerat()),
                        String.format("Rp %,.0f", trx.getTotalHarga()),
                        trx.getStatus()
                };
                model.addRow(row);
            }
        }

        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(new Color(240, 240, 240)); 
        table.setShowVerticalLines(false); 
        table.setSelectionBackground(new Color(230, 245, 230)); 
        table.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(GREEN_PRIMARY); 
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        class CenteredHeaderRenderer extends DefaultTableCellRenderer {
            public CenteredHeaderRenderer() {
                setHorizontalAlignment(JLabel.CENTER);
                setForeground(Color.WHITE);
                setBackground(GREEN_PRIMARY);
                setFont(new Font("Arial", Font.BOLD, 14));
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(100, 100, 100)));
            }
        }
        
        header.setDefaultRenderer(new CenteredHeaderRenderer());
        
        TableColumnModel columnModel = table.getColumnModel();
        
        columnModel.getColumn(0).setPreferredWidth(100); 
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(80);
        
        columnModel.getColumn(2).setCellRenderer(centerRenderer); 
        columnModel.getColumn(3).setCellRenderer(centerRenderer); 
        columnModel.getColumn(4).setCellRenderer(centerRenderer); 


        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        wrapper.add(scroll, BorderLayout.CENTER);

        return wrapper;
    }
}