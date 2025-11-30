package View.AdminPanels;

import Model.Admin;
import Model.BankSampah; 

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder; 

import Controller.AdminHomePanelController;

import java.awt.*;
import java.util.Map;

public class AdminHomePanel extends JPanel {

    private Admin currentUser;
    private BankSampah curreBankSampah;
    
    private JLabel lblBankName;
    private JLabel lblAdminName;
    
    private JLabel lblTotalMemberVal;
    private JLabel lblTotalTrxVal;
    private JLabel lblTotalSampahVal; 
    
    private JPanel panelCategoryList; 

    private final Color GREEN_PRIMARY = new Color(40, 167, 69); 
    private final Color BLUE_ACCENT = new Color(0, 123, 255); 
    private final Color YELLOW_ACCENT = new Color(255, 193, 7); 
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250); 
    private final Color CARD_BACKGROUND = Color.WHITE;
    private final Color TEXT_DARK = new Color(33, 37, 41);
    private final Color TEXT_MUTED = new Color(108, 117, 125);
    private final Color BORDER_LIGHT = new Color(222, 226, 230); 
    
    private final Font FONT_HEADER_TITLE = new Font("Fredoka", Font.BOLD, 30); 
    private final Font FONT_SUBTITLE = new Font("Fredoka", Font.PLAIN, 16);
    private final Font FONT_CARD_TITLE = new Font("Fredoka", Font.BOLD, 14);
    private final Font FONT_CARD_VALUE = new Font("Fredoka", Font.BOLD, 36); 
    private final Font FONT_SECTION_TITLE = new Font("Fredoka", Font.BOLD, 20); 

    public AdminHomePanel(Admin admin, BankSampah bankSampah) {
        this.currentUser = admin;
        this.curreBankSampah = bankSampah;
        
        initLayout();
        loadData(); 
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR); 
        
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(BACKGROUND_COLOR);
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40)); 

        mainContent.add(createHeaderSection());
        mainContent.add(Box.createVerticalStrut(35)); 

        mainContent.add(createSummaryCards());
        mainContent.add(Box.createVerticalStrut(35));

        mainContent.add(createCategorySection());
        mainContent.add(Box.createVerticalStrut(25));

        JButton btnRefresh = new JButton("Muat Ulang Data");
        btnRefresh.setFont(new Font("Fredoka", Font.BOLD, 14));
        btnRefresh.setBackground(GREEN_PRIMARY); 
        btnRefresh.setForeground(CARD_BACKGROUND);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnRefresh.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRefresh.addActionListener(e -> loadData()); 
        mainContent.add(btnRefresh);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }


    private JPanel createHeaderSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 100));

        lblAdminName = new JLabel("Halo, Admin " + currentUser.getNamaAdmin() + "!");
        lblAdminName.setFont(FONT_SUBTITLE);
        lblAdminName.setForeground(TEXT_MUTED);

        lblBankName = new JLabel("Dashboard Bank Sampah");
        lblBankName.setFont(FONT_HEADER_TITLE);
        lblBankName.setForeground(GREEN_PRIMARY); 
        
        panel.add(lblAdminName);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblBankName);

        return panel;
    }

    private JPanel createSummaryCards() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 30, 0)); 
        panel.setBackground(BACKGROUND_COLOR);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 150)); 

        String iconMember = "👥"; 
        String iconTrx = "💵"; 
        String iconSampah = "🗑️"; 

        lblTotalMemberVal = new JLabel("0");
        panel.add(createSingleCard("Total Nasabah", lblTotalMemberVal, BLUE_ACCENT, iconMember)); 

        lblTotalTrxVal = new JLabel("0");
        panel.add(createSingleCard("Total Transaksi", lblTotalTrxVal, YELLOW_ACCENT, iconTrx)); 

        lblTotalSampahVal = new JLabel("0.0 Kg");
        panel.add(createSingleCard("Sampah Terkumpul", lblTotalSampahVal, GREEN_PRIMARY, iconSampah)); 

        return panel;
    }

    private JPanel createSingleCard(String title, JLabel valueLabel, Color accentColor, String icon) {
        JPanel card = new JPanel(new BorderLayout(15, 0)); 
        card.setBackground(CARD_BACKGROUND);
        
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_LIGHT.darker(), 1, true),
            new EmptyBorder(25, 20, 25, 20) 
        ));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Fredoka Emoji", Font.PLAIN, 40)); 
        lblIcon.setForeground(accentColor);
        card.add(lblIcon, BorderLayout.WEST);

        JPanel content = new JPanel(new GridLayout(2, 1));
        content.setBackground(CARD_BACKGROUND);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_CARD_TITLE);
        lblTitle.setForeground(TEXT_MUTED);

        valueLabel.setFont(FONT_CARD_VALUE);
        valueLabel.setForeground(TEXT_DARK); 

        content.add(lblTitle);
        content.add(valueLabel);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JPanel createCategorySection() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(CARD_BACKGROUND);
        container.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        container.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_LIGHT, 1, true),
            new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel lblTitle = new JLabel("Data Stok Sampah per Kategori");
        lblTitle.setFont(FONT_SECTION_TITLE);
        lblTitle.setForeground(GREEN_PRIMARY); 
        container.add(lblTitle, BorderLayout.NORTH);

        panelCategoryList = new JPanel();
        panelCategoryList.setLayout(new BoxLayout(panelCategoryList, BoxLayout.Y_AXIS));
        panelCategoryList.setBackground(CARD_BACKGROUND);
        panelCategoryList.setBorder(new EmptyBorder(25, 0, 0, 0)); 

        container.add(panelCategoryList, BorderLayout.CENTER);
        
        return container;
    }

    private JPanel createCategoryRow(String name, double totalKg) {
        JPanel row = new JPanel(new BorderLayout(0, 5)); 
        row.setBackground(CARD_BACKGROUND);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 50)); 

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(CARD_BACKGROUND);
        content.setBorder(new EmptyBorder(8, 0, 8, 0)); 

        JLabel lblName = new JLabel("• " + name);
        lblName.setFont(new Font("Fredoka", Font.PLAIN, 15));
        lblName.setForeground(TEXT_DARK);

        JLabel lblVal = new JLabel(String.format("%.1f Kg", totalKg)); 
        lblVal.setFont(new Font("Fredoka", Font.BOLD, 16));
        lblVal.setForeground(GREEN_PRIMARY.darker()); 

        content.add(lblName, BorderLayout.WEST);
        content.add(lblVal, BorderLayout.EAST);
        
        row.add(content, BorderLayout.CENTER);
        
        row.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_LIGHT)); 

        return row;
    }

    private void loadData() {
        if (curreBankSampah == null) return;

        String namaBank = AdminHomePanelController.getService().getBankName(currentUser);
        lblBankName.setText(namaBank.isEmpty() ? "Dashboard Bank Sampah" : namaBank);

        int totalMember = AdminHomePanelController.getService().getTotalMember(curreBankSampah);
        lblTotalMemberVal.setText(String.valueOf(totalMember));

        int totalTrx = AdminHomePanelController.getService().getTotalTransaksi(curreBankSampah);
        lblTotalTrxVal.setText(String.valueOf(totalTrx));

        double totalBerat = AdminHomePanelController.getService().getTotalBeratSampah(curreBankSampah);
        lblTotalSampahVal.setText(String.format("%.1f Kg", totalBerat)); 

        Map<String, Double> dataKategori = AdminHomePanelController.getService().beratPerKategori(curreBankSampah);
        
        panelCategoryList.removeAll(); 

        if (dataKategori.isEmpty()) {
            JLabel emptyLabel = new JLabel("Belum ada data sampah masuk.");
            emptyLabel.setBorder(new EmptyBorder(30, 10, 30, 10)); 
            emptyLabel.setFont(new Font("Fredoka", Font.ITALIC, 14));
            emptyLabel.setForeground(TEXT_MUTED);
            panelCategoryList.add(emptyLabel);
        } else {
            for (Map.Entry<String, Double> entry : dataKategori.entrySet()) {
                panelCategoryList.add(createCategoryRow(entry.getKey(), entry.getValue()));
            }
        }

        panelCategoryList.revalidate();
        panelCategoryList.repaint();
    }
}