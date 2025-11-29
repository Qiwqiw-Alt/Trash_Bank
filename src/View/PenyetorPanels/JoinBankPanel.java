package View.PenyetorPanels;

import java.awt.BorderLayout;
// import java.awt.Color;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.chrono.JapaneseEra;
import java.util.ArrayList;
import java.util.concurrent.Flow;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
// import javax.swing.JButton;
// import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
// import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Database.DatabaseBankSampah;
import Database.DatabasePenyetor;
import Database.DatabaseRequestJoin;
import Model.BankSampah;
// import Model.BankSampah;
import Model.Penyetor;
import Model.TransaksiJoin;
import Service.BankSampahService;
import View.DashboardPenyetorView;

public class JoinBankPanel extends JPanel {
    Penyetor user;
    JPanel mainContent;
    ArrayList<BankSampah> listBankSampah;
    DashboardPenyetorView mainFrame;

    public JoinBankPanel(Penyetor User, DashboardPenyetorView mainFrame) {
        this.user = User;
        this.mainFrame = mainFrame;
        listBankSampah = DatabaseBankSampah.loadData();

        initLayout();
    }

    public void initLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(245, 245, 245));
        mainContent.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BorderLayout());
        wrapper.setBackground(new Color(245, 245, 245));
        wrapper.setBorder(new EmptyBorder(20, 30, 20, 30));

        wrapper.add(headerSection(), BorderLayout.NORTH);


        wrapper.add(mainSection(), BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);

    }

    public JScrollPane mainSection() {
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(new Color(245, 245, 245));

        for (BankSampah bankSampah : listBankSampah) {
            main.add(createCard(bankSampah));
            main.add(Box.createVerticalStrut(15)); // spasi antar kartu
        }

        JScrollPane scroll = new JScrollPane(main);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        return scroll; // ✔ return scroll, bukan panel

    }

    public JPanel headerSection() {
        JPanel main = new JPanel();
        main.setLayout(new FlowLayout(FlowLayout.CENTER));

        main.setBackground(new Color(245, 245, 245));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setMaximumSize(new Dimension(Short.MAX_VALUE, 20));

        JLabel title = new JLabel();
        title.setText("SELAMAT DATANG DI APLIKASI BANK SAMPAH, SILAHKAN BERGABUNG KE BANK!");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.BLACK);

        main.add(title);

        return main;
    }

    public JPanel createCard(BankSampah bank) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(20, 0)); // Spasi horizontal 20px
        cardPanel.setBackground(Color.WHITE);

        // Padding dan Border halus
        cardPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(220, 220, 220), 1, true), // Border abu-abu tipis dengan sudut membulat
                        new EmptyBorder(15, 20, 15, 20)));

        // --- Konten Teks (Kiri) ---
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false); // Transparan
        textPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Label Nama Bank
        JLabel title = new JLabel();
        title.setText(bank.getNamaBank());
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(50, 50, 50));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Label Alamat
        JLabel alamat = new JLabel();
        alamat.setText(bank.getAlamat());
        alamat.setFont(new Font("Arial", Font.PLAIN, 12));
        alamat.setForeground(new Color(100, 100, 100));
        alamat.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(5)); // Spasi kecil
        textPanel.add(alamat);

        // --- Tombol (Kanan) ---
        JPanel buttonWrapper = new JPanel();
        buttonWrapper.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonWrapper.setOpaque(false);

        JButton joinButton = new JButton();
        joinButton.setText("Gabung Bank");
        joinButton.setFont(new Font("Arial", Font.BOLD, 12));

        // Desain Tombol Modern (Warna Hijau Khas Bank Sampah)
        joinButton.setBackground(new Color(76, 175, 80)); // Hijau
        joinButton.setForeground(Color.WHITE);
        joinButton.setFocusPainted(false); // Hilangkan fokus aneh
        joinButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(67, 160, 71), 1, true), // Border halus
                new EmptyBorder(10, 15, 10, 15) // Padding
        ));

        joinButton.addActionListener(e -> {
            joinBank(bank);
        });

        buttonWrapper.add(joinButton);

        // Tambahkan ke CardPanel
        cardPanel.add(textPanel, BorderLayout.CENTER); // Konten di tengah
        cardPanel.add(buttonWrapper, BorderLayout.EAST); // Tombol di kanan

        return cardPanel;
    }

    private void joinBank(BankSampah bank) {
        ArrayList<TransaksiJoin> dafTransaksiJoins = DatabaseRequestJoin.loadData();

        for (TransaksiJoin transaksiJoin : dafTransaksiJoins) {
            if (transaksiJoin.getIdPenyetor().equals(user.getIdPenyetor()) && transaksiJoin.getStatusRequest() == TransaksiJoin.Status.DITERIMA) {
                
                
        JOptionPane.showMessageDialog(this,
                "Anda berhasil mendaftar ke " + bank.getNamaBank() + "!",
                "Pendaftaran Berhasil",
                JOptionPane.INFORMATION_MESSAGE);
                
                mainFrame.setBankSampah(bank);
                return;
            } else if (transaksiJoin.getIdPenyetor().equals(user.getIdPenyetor()) && transaksiJoin.getStatusRequest() == TransaksiJoin.Status.DITOLAK) {
                JOptionPane.showMessageDialog(this,
                "Anda Ditolak mendaftar ke " + bank.getNamaBank() + "!",
                "Pendaftaran Ggagal",
                JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        String idTransaksiJoin = DatabaseRequestJoin.generateRewardId();

        TransaksiJoin baru = new TransaksiJoin(idTransaksiJoin, user.getIdPenyetor(), bank.getIdBank());
        dafTransaksiJoins.add(baru);
        DatabaseRequestJoin.writeData(dafTransaksiJoins);
        DatabaseRequestJoin.writeData(dafTransaksiJoins, bank.getFileRequestJoin());

        JOptionPane.showMessageDialog(this,
                "Anda berhasil mendaftar ke " + bank.getNamaBank() + "!" + ", Tunggu Sampai Admin ",
                "Pendaftaran Menunggu Konfirmasi Admin",
                JOptionPane.INFORMATION_MESSAGE);
        return;
    }

}
