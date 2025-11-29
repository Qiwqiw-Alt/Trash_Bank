package View.PenyetorPanels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Database.DatabaseTransaksi;
import Model.Penyetor;
import Model.Transaksi;

import java.awt.*;
import java.util.ArrayList;

public class RiwayatTransaksiPanel extends JPanel {

    public RiwayatTransaksiPanel(Penyetor user) {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Riwayat Transaksi", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(20, 0, 20, 0));

        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        ArrayList<Transaksi> allTransaksi = DatabaseTransaksi.loadData();

        boolean found = false;

        for (Transaksi trx : allTransaksi) {
            if (trx.getIdPenyetor().equals(user.getIdPenyetor())) {  
                found = true;
                content.add(buildTransaksiCard(trx));
                content.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        if (!found) {
            JLabel kosong = new JLabel("Belum ada riwayat transaksi.", SwingConstants.CENTER);
            kosong.setFont(new Font("Arial", Font.PLAIN, 18));
            kosong.setBorder(new EmptyBorder(50, 0, 0, 0));
            add(kosong, BorderLayout.CENTER);
        } else {
            add(scrollPane, BorderLayout.CENTER);
        }
    }

    private JPanel buildTransaksiCard(Transaksi trx) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel id = new JLabel("ID Transaksi: " + trx.getIdTransaksi());
        id.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel tanggal = new JLabel("Tanggal: " + trx.getTanggal());
        tanggal.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel berat = new JLabel("Total Berat: " + trx.getTotalBerat() + " kg");
        berat.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel harga = new JLabel("Total Harga: Rp " + trx.getTotalHarga());
        harga.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel status = new JLabel("Status: " + trx.getStatus());
        status.setFont(new Font("Arial", Font.BOLD, 14));
        status.setForeground(setStatusColor(trx.getStatus()));

        card.add(id);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(tanggal);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(berat);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(harga);
        card.add(Box.createRigidArea(new Dimension(0, 7)));
        card.add(status);

        return card;
    }

    private Color setStatusColor(Transaksi.Status status) {
        switch (status) {
            case DITERIMA:
                return new Color(34, 139, 34); // hijau
            case DITOLAK:
                return new Color(178, 34, 34); // merah
            case SEDANG_DITINJAU:
                return new Color(255, 165, 0); // orange
            default:
                return Color.GRAY;
        }
    }
}
