package View.PenyetorPanels;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import Model.Reward;
import Service.BankSampahService;
import Model.Penyetor;
import Model.BankSampah;
import Model.PenukaranReward;

import Database.DatabaseReward;
import Database.DatabasePenukaranReward;

import java.util.ArrayList;

public class TukarPoinPanel extends JPanel {

    private JPanel content;
    private JPanel wrapperPanel;
    private JScrollPane scroll;

    private ArrayList<Reward> listReward;
    private Penyetor user;
    private BankSampah bank;
    private BankSampahService bss = new BankSampahService();

    private final Color PRIMARY_COLOR = new Color(0x356A69); 
    private final Color ACCENT_COLOR = new Color(0x3498DB); 
    private final Color SOFT_BG = new Color(245, 245, 245);
    private final Color CARD_HOVER_COLOR = new Color(235, 245, 255);
    private final Color SUCCESS_COLOR = new Color(0x67AE6E);
    private final NumberFormat integerFormatter = new DecimalFormat("#,##0");

    private final String REWARD_ICON_PATH = "src\\\\Asset\\\\Image\\\\gift (2).png"; 
    private final String HISTORY_ICON_PATH = "src\\\\Asset\\\\Image\\\\history.png";
    private final int ICON_SIZE_TITLE = 30;
    private final int ICON_SIZE_CARD = 20;

    public TukarPoinPanel(Penyetor user) {
        this.user = user;
        bank = bss.getObjBankSampah(user.getIdBankSampah());

        BankSampahService bss = new BankSampahService();
        BankSampah bankSampah = bss.getObjBankSampah(user.getIdBankSampah());
        this.listReward = DatabaseReward.loadData(bankSampah.getFileReward());

        setLayout(new BorderLayout());
        setBackground(SOFT_BG);

        JLabel title = new JLabel(" Tukar Poin Reward", SwingConstants.CENTER);
        title.setIcon(getScaledIcon(REWARD_ICON_PATH, ICON_SIZE_TITLE, ICON_SIZE_TITLE));
        title.setFont(new Font("Fredoka", Font.BOLD, 26));
        title.setForeground(PRIMARY_COLOR.darker());
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        add(title, BorderLayout.NORTH);

        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(SOFT_BG);

        wrapperPanel = new JPanel(); 
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS)); 
        wrapperPanel.setBackground(SOFT_BG);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));
        
        JPanel rewardCardsWrapper = new JPanel(new BorderLayout());
        rewardCardsWrapper.add(content, BorderLayout.NORTH);
        rewardCardsWrapper.setBackground(SOFT_BG);
        
        for (Reward r : listReward) {
            JPanel card = createRewardCard(r);
            card.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220)); 

            content.add(card);
            content.add(Box.createVerticalStrut(20));
        }
        
        wrapperPanel.add(rewardCardsWrapper);
        
        wrapperPanel.add(Box.createVerticalStrut(30)); 
        
        JPanel riwayatTukar = createTablePenukaranPanel(user);
        
        JScrollPane historyScroll = new JScrollPane(riwayatTukar); 
        historyScroll.setPreferredSize(new Dimension(800, 450)); 
        historyScroll.setBorder(null); 

        JPanel historyContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        historyContainer.setBackground(SOFT_BG);
        historyContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
        historyContainer.add(historyScroll);

        wrapperPanel.add(historyContainer);

        scroll = new JScrollPane(wrapperPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scroll, BorderLayout.CENTER);
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

    private void refreshPanel() {
        listReward = DatabaseReward.loadData(bank.getFileReward());

        content.removeAll(); 

        for (Reward r : listReward) {
            JPanel card = createRewardCard(r);

            card.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

            content.add(card);
            content.add(Box.createVerticalStrut(20));
        }
        
        JPanel historyContainer = (JPanel) wrapperPanel.getComponent(2); 
        historyContainer.removeAll();
        
        JPanel riwayatTukarBaru = createTablePenukaranPanel(user);
        JScrollPane historyScrollBaru = new JScrollPane(riwayatTukarBaru);
        historyScrollBaru.setPreferredSize(new Dimension(800, 450));
        historyScrollBaru.setBorder(null);
        historyContainer.add(historyScrollBaru);


        content.revalidate();
        content.repaint();
        wrapperPanel.revalidate();
        wrapperPanel.repaint();
    }

    private JPanel createRewardCard(Reward reward) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(CARD_HOVER_COLOR); 
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel name = new JLabel("✨ " + reward.getNamaHadiah());
        name.setFont(new Font("Fredoka", Font.BOLD, 18));
        name.setForeground(PRIMARY_COLOR.darker()); 

        JLabel desc = new JLabel("<html>" + reward.getDeskripsi() + "</html>");
        desc.setFont(new Font("Fredoka", Font.PLAIN, 13));
        desc.setForeground(new Color(100, 100, 100)); 

        JLabel harga = new JLabel("💰 Harga Poin: " + integerFormatter.format(reward.getHargaTukar()));
        harga.setFont(new Font("Fredoka", Font.BOLD, 15));
        harga.setForeground(ACCENT_COLOR); 

        JLabel stok = new JLabel("📦 Stok Tersedia: " + integerFormatter.format(reward.getStok()));
        stok.setFont(new Font("Fredoka", Font.PLAIN, 14));
        stok.setForeground(reward.getStok() > 5 ? SUCCESS_COLOR : Color.RED); 

        left.add(name);
        left.add(Box.createVerticalStrut(10));
        left.add(desc);
        left.add(Box.createVerticalStrut(15));
        left.add(harga);
        left.add(stok);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 50)); 
        right.setOpaque(false);

        JButton btnTukar = new JButton("Tukar Sekarang");
        btnTukar.setPreferredSize(new Dimension(150, 40));
        btnTukar.setFocusPainted(false);
        btnTukar.setBackground(ACCENT_COLOR); 
        btnTukar.setForeground(Color.WHITE);
        btnTukar.setFont(new Font("Fredoka", Font.BOLD, 14));
        btnTukar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        if (user.getTotalPoin() < reward.getHargaTukar() || reward.getStok() <= 0) {
            btnTukar.setEnabled(false);
            btnTukar.setBackground(Color.GRAY);
            btnTukar.setText("Tidak Tersedia");
        }

        btnTukar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btnTukar.isEnabled())
                    btnTukar.setBackground(ACCENT_COLOR.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btnTukar.isEnabled())
                    btnTukar.setBackground(ACCENT_COLOR);
            }
        });

        btnTukar.addActionListener(e -> tukarReward(reward));

        right.add(btnTukar);

        card.add(left, BorderLayout.WEST);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    private JPanel createTablePenukaranPanel(Penyetor user) {

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel title = new JLabel("Riwayat Penukaran Anda");
        title.setIcon(getScaledIcon(HISTORY_ICON_PATH, ICON_SIZE_CARD, ICON_SIZE_CARD));
        title.setFont(new Font("Fredoka", Font.BOLD, 18));
        title.setForeground(PRIMARY_COLOR);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        wrapper.add(title, BorderLayout.NORTH);

        String[] columns = { "ID Penukaran", "ID Reward", "Tanggal", "Poin Dikeluarkan" }; 
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        ArrayList<PenukaranReward> list = DatabasePenukaranReward.loadData();

        for (PenukaranReward pr : list) {
            if (pr.getIdPenyetor().equals(user.getIdPenyetor())) {
                Object[] row = {
                        pr.getIdPenukaran(),
                        pr.getIdReward(),
                        pr.getTanggal(),
                        integerFormatter.format(pr.getPoin()) 
                };
                model.addRow(row);
            }
        }

        JTable table = new JTable(model);
        
        table.setRowHeight(30);
        table.setFont(new Font("Fredoka", Font.PLAIN, 14));
        table.setShowGrid(true);
        table.setGridColor(new Color(240, 240, 240));
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(CARD_HOVER_COLOR.darker());
        table.setShowVerticalLines(false);


        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Fredoka", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR); 
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(new LineBorder(new Color(200, 200, 200)));


        wrapper.add(scrollTable, BorderLayout.CENTER);

        return wrapper;
    }

    private void tukarReward(Reward reward) {
        double poinUser = user.getTotalPoin();
        double hargaReward = reward.getHargaTukar();

        if (poinUser < hargaReward) {
            JOptionPane.showMessageDialog(this, "Poin tidak cukup untuk menukar reward ini!");
            return;
        }

        if (reward.getStok() <= 0) {
            JOptionPane.showMessageDialog(this, "Stok reward habis!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Tukar " + reward.getNamaHadiah() + " dengan " + integerFormatter.format(hargaReward) + " poin?",
                "Konfirmasi Penukaran",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            
            user.setTotalPoin(poinUser - hargaReward); 
            reward.kurangiStok(1);

            String idPR = DatabasePenukaranReward.generatePenukaranId();
            PenukaranReward transaksi = new PenukaranReward(idPR, reward.getIdReward(), user.getIdPenyetor());
            transaksi.setPoint(hargaReward); 

            DatabasePenukaranReward.addPenukaran(transaksi);

            DatabaseReward.updateReward(reward, bank.getFileReward());

            JOptionPane.showMessageDialog(this, "Penukaran reward berhasil! Sisa Poin Anda: " + integerFormatter.format(user.getTotalPoin()));
            refreshPanel();
        }
    }
}