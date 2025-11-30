package View.PenyetorPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;

import Database.DatabaseBankSampah;
import Database.DatabaseRequestJoin;
import Model.BankSampah;
import Model.Penyetor;
import Model.TransaksiJoin;
import View.DashboardPenyetorView;

public class JoinBankPanel extends JPanel {
    Penyetor user;
    JPanel mainContent;
    ArrayList<BankSampah> listBankSampah;
    DashboardPenyetorView mainFrame;

    private final String IMAGE_PATH = "src\\\\Asset\\\\Image\\\\bank.png";
    private final String LOCATION_ICON_PATH = "src\\\\Asset\\\\Image\\\\location.png"; 
    private final String NAME_ICON = "src\\\\Asset\\\\Image\\\\tag.png";

    public JoinBankPanel(Penyetor User, DashboardPenyetorView mainFrame) {
        this.user = User;
        this.mainFrame = mainFrame;
        listBankSampah = DatabaseBankSampah.loadData();

        initLayout();
    }
    
    public void initLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

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
            main.add(Box.createVerticalStrut(15)); 
        }

        JScrollPane scroll = new JScrollPane(main);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        return scroll;
    }

    public JPanel headerSection() {
        JPanel main = new JPanel();
        main.setLayout(new FlowLayout(FlowLayout.CENTER));

        main.setBackground(new Color(245, 245, 245));
        main.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));

        JLabel title = new JLabel();
        title.setText("SELAMAT DATANG DI APLIKASI BANK SAMPAH, SILAHKAN BERGABUNG KE BANK!");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.BLACK);

        main.add(title);

        return main;
    }

    public JPanel createCard(BankSampah bank) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(15, 0)); 
        cardPanel.setBackground(Color.WHITE);
        
        cardPanel.setMaximumSize(new Dimension(1000, 120)); 
        cardPanel.setPreferredSize(new Dimension(800, 120)); 
        cardPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        cardPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(220, 220, 220), 1, true), 
                        new EmptyBorder(15, 20, 15, 20)));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(100, 90));
        
        JLabel bankImageLabel = createImageLabel(IMAGE_PATH, 60, 60, "BANK");
        leftPanel.add(bankImageLabel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        titlePanel.setOpaque(false);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel iconBankTitle = createImageLabel(NAME_ICON, 16, 16, "B");
        iconBankTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5)); 
        
        JLabel title = new JLabel();
        title.setText(bank.getNamaBank());
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(50, 50, 50));
        
        titlePanel.add(iconBankTitle);
        titlePanel.add(title);
        
        JPanel alamatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        alamatPanel.setOpaque(false);
        alamatPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel iconLocation = createImageLabel(LOCATION_ICON_PATH, 16, 16, "L");
        iconLocation.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        
        JLabel alamat = new JLabel();
        alamat.setText("<html><div style='width: 400px; font-size: 12px;'>" + bank.getAlamat() + "</div></html>"); 
        alamat.setFont(new Font("Arial", Font.PLAIN, 12));
        alamat.setForeground(new Color(100, 100, 100));

        alamatPanel.add(iconLocation);
        alamatPanel.add(alamat);
        
        centerPanel.add(titlePanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(alamatPanel);
        centerPanel.add(Box.createVerticalGlue()); 

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(120, 90));

        JButton joinButton = new JButton();
        joinButton.setText("Gabung Bank");
        joinButton.setFont(new Font("Arial", Font.BOLD, 12)); 

        joinButton.setBackground(new Color(76, 175, 80)); 
        joinButton.setForeground(Color.WHITE);
        joinButton.setFocusPainted(false);
        joinButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(67, 160, 71), 1, true), 
                new EmptyBorder(8, 15, 8, 15) 
        ));

        joinButton.addActionListener(e -> {
            joinBank(bank);
        });

        rightPanel.add(joinButton, BorderLayout.CENTER);

        cardPanel.add(leftPanel, BorderLayout.WEST);
        cardPanel.add(centerPanel, BorderLayout.CENTER);
        cardPanel.add(rightPanel, BorderLayout.EAST);

        return cardPanel;
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
            imageLabel.setBorder(BorderFactory.createLineBorder(new Color(53, 106, 105), 1));
            imageLabel.setFont(new Font("Arial", Font.BOLD, 10));
            imageLabel.setForeground(new Color(53, 106, 105));
        }
        return imageLabel;
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