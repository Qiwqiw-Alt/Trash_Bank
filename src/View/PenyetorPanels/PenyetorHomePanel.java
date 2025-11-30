package View.PenyetorPanels;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

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

    public PenyetorHomePanel(Penyetor user) {
        this.penyetor = user;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245)); // soft grey

        this.listTransaksi = sps.getDaftarTransaksi(penyetor);
        user.setTotalSetoran(this.listTransaksi.size());
        double totalPoinPendapatanTransaksi = DatabaseTransaksi.hitungPoinPenyetor(user);
        double totalPoinPengeluranTukarReward = DatabasePenukaranReward.hitungPoinPenyetor(user);
        double totalPoinUser = totalPoinPendapatanTransaksi - totalPoinPengeluranTukarReward;

        if (totalPoinUser < 0) {
            user.setTotalPoin(0);
        } else {

            user.setTotalPoin(totalPoinPendapatanTransaksi - totalPoinPengeluranTukarReward);
        }

        // Panel utama berisi semuanya
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        container.setBackground(new Color(245, 245, 245));

        container.add(createInfoCards(penyetor));
        container.add(Box.createVerticalStrut(20));
        container.add(createTablePanel(penyetor));

        add(container, BorderLayout.CENTER);
    }

    // -------------------------
    // CARD INFO
    // -------------------------
    private JPanel createInfoCards(Penyetor user) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setOpaque(false);

        panel.add(createSingleCard("Total Setoran", String.valueOf(bss.getTotalSetoran(user))));
        panel.add(createSingleCard("Total Poin", String.valueOf(user.getTotalPoin())));

        return panel;
    }

    private JPanel createSingleCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);

        // Rounded border
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(80, 80, 80));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(new Color(40, 100, 200)); // biru modern

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(10));
        card.add(lblValue);

        return card;
    }

    // -------------------------
    // TABEL TRANSAKSI
    // -------------------------

    private JPanel createTablePanel(Penyetor user) {

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel title = new JLabel("Riwayat Transaksi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        wrapper.add(title, BorderLayout.NORTH);

        String[] columns = { "ID Transaksi", "Tanggal", "Berat (kg)", "Harga (Rp)", "Status" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Transaksi trx : listTransaksi) {
            if (trx.getIdPenyetor().equals(user.getIdPenyetor())) {
                Object[] row = {
                        trx.getIdTransaksi(),
                        trx.getTanggal(),
                        trx.getTotalBerat(),
                        trx.getTotalHarga(),
                        trx.getStatus()
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
}
