package View.PenyetorPanels;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.*;

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

    public TukarPoinPanel(Penyetor user) {
        this.user = user;
        bank = bss.getObjBankSampah(user.getIdBankSampah());

        BankSampahService bss = new BankSampahService();
        BankSampah bankSampah = bss.getObjBankSampah(user.getIdBankSampah());
        this.listReward = DatabaseReward.loadData(bankSampah.getFileReward());

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // TITLE
        JLabel title = new JLabel("Tukar Poin Reward", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        add(title, BorderLayout.NORTH);

        // LIST PANEL (Card container)
        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(245, 245, 245));

        wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));
        wrapperPanel.add(content, BorderLayout.NORTH);

        for (Reward r : listReward) {
            JPanel card = createRewardCard(r);

            card.setAlignmentX(Component.CENTER_ALIGNMENT); // biar center
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220)); // panjang mengikuti panel, tinggi fix

            content.add(card);
            content.add(Box.createVerticalStrut(20)); // jarak antar card
        }

        JPanel riwayatTukar = createTablePenukaranPanel(user);
        JScrollPane historyScroll = new JScrollPane(riwayatTukar);
        historyScroll.setPreferredSize(new Dimension(800, 250));
        historyScroll.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        wrapperPanel.add(Box.createVerticalStrut(20));
        wrapperPanel.add(historyScroll, BorderLayout.CENTER);

        scroll = new JScrollPane(wrapperPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scroll, BorderLayout.CENTER);
    }

    private void refreshPanel() {
        // reload data reward
        listReward = DatabaseReward.loadData(bank.getFileReward());

        content.removeAll(); // hapus semua card lama

        for (Reward r : listReward) {
            JPanel card = createRewardCard(r);

            card.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

            content.add(card);
            content.add(Box.createVerticalStrut(20));
        }

        content.revalidate();
        content.repaint();
    }

    private JPanel createRewardCard(Reward reward) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // HOVER EFFECT
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(250, 250, 250));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        // =============================
        // LEFT PANEL (Text)
        // =============================
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel name = new JLabel(reward.getNamaHadiah());
        name.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel desc = new JLabel("<html>" + reward.getDeskripsi() + "</html>");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        desc.setForeground(new Color(80, 80, 80));

        JLabel harga = new JLabel("Harga Poin: " + reward.getHargaTukar());
        harga.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel stok = new JLabel("Stok: " + reward.getStok());
        stok.setFont(new Font("SansSerif", Font.PLAIN, 14));

        left.add(name);
        left.add(Box.createVerticalStrut(10));
        left.add(desc);
        left.add(Box.createVerticalStrut(10));
        left.add(harga);
        left.add(stok);

        // =============================
        // RIGHT PANEL (Button)
        // =============================
        JPanel right = new JPanel(new FlowLayout(FlowLayout.CENTER));
        right.setOpaque(false);

        JButton btnTukar = new JButton("Tukar");
        btnTukar.setPreferredSize(new Dimension(100, 35));
        btnTukar.setFocusPainted(false);
        btnTukar.setBackground(new Color(52, 152, 219));
        btnTukar.setForeground(Color.WHITE);
        btnTukar.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Hover button
        btnTukar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnTukar.setBackground(new Color(41, 128, 185));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnTukar.setBackground(new Color(52, 152, 219));
            }
        });

        btnTukar.addActionListener(e -> tukarReward(reward));

        right.add(btnTukar);

        // Add to card
        card.add(left, BorderLayout.WEST);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    // -------------------------
    // TABEL PENUKARAN REWARD
    // -------------------------

    private JPanel createTablePenukaranPanel(Penyetor user) {

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(15, 15, 15, 15)));

        // Title
        JLabel title = new JLabel("Riwayat Penukaran");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        wrapper.add(title, BorderLayout.NORTH);

        // Kolom
        String[] columns = { "ID Penukaran", "ID Reward", "Tanggal", "Poin" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        // Ambil data penukaran
        ArrayList<PenukaranReward> list = DatabasePenukaranReward.loadData();

        for (PenukaranReward pr : list) {
            if (pr.getIdPenyetor().equals(user.getIdPenyetor())) {
                Object[] row = {
                        pr.getIdPenukaran(),
                        pr.getIdReward(),
                        pr.getTanggal(),
                        pr.getPoin()
                };
                model.addRow(row);
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(230, 230, 230));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);

        wrapper.add(scroll, BorderLayout.CENTER);

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
                "Tukar " + reward.getNamaHadiah() + " dengan " + (int) hargaReward + " poin?",
                "Konfirmasi Penukaran",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            user.setTotalPoin(poinUser);
            reward.kurangiStok(1);

            String idPR = DatabasePenukaranReward.generatePenukaranId();
            PenukaranReward transaksi = new PenukaranReward(idPR, reward.getIdReward(), user.getIdPenyetor());
            transaksi.setPoint(poinUser - reward.getHargaTukar());

            DatabasePenukaranReward.addPenukaran(transaksi);

            DatabaseReward.updateReward(reward, bank.getFileReward());

            JOptionPane.showMessageDialog(this, "Penukaran reward berhasil!");
            refreshPanel();
        }
    }
}
