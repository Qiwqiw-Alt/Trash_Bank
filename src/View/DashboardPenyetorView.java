package View;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;

import java.awt.*;
import Model.Penyetor;
import Service.BankSampahService;
import View.PenyetorPanels.JoinBankPanel;
import Model.BankSampah;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardPenyetorView extends JFrame {
    private Penyetor currentUser;
    private BankSampah bankSampah;
    private BankSampahService bss = new BankSampahService();
    private String idBankSampah;

    private JPanel contentPanel;
    private JPanel menuPanel;

    // Icon & Style Constants
    private final Color GREEN_PRIMARY = new Color(0, 128, 0);
    private final Color GREEN_LIGHT = new Color(200, 240, 200);
    // private final Color TEXT_DARK = new Color(50, 50, 50);

    public DashboardPenyetorView(Penyetor user) {
        this.currentUser = user;
        this.bankSampah = bss.getObjBankSampah((user.getIdBankSampah()));

        String namaBank = bankSampah != null ? bankSampah.getNamaBank() : "Bank Sampah App";
        setTitle("Dashboard Penyetor - " + namaBank);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        initLayout();
    }

    private void initLayout() {
        setLayout(new BorderLayout());

        menuPanel = createSidebar();
        add(menuPanel, BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.white);
        add(contentPanel, BorderLayout.CENTER);

        if (this.bankSampah == null) {
            switchPanel("JoinBank");
        } else {
            switchPanel("Home");
        }
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(260, 0));
        panel.setBackground(GREEN_PRIMARY);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 1. Header Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(GREEN_PRIMARY);
        JLabel logo = new JLabel("BANK SAMPAH APP", SwingConstants.CENTER);
        logo.setFont(new Font("Arial", Font.BOLD, 20));
        logo.setForeground(Color.WHITE);
        logo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        logoPanel.add(logo);
        panel.add(logoPanel);

        panel.add(new JSeparator());
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // 2. Menu Profil (Selalu Ada)
        panel.add(createMenuLabel("Profil Saya", "Profil"));

        // 3. LOGIKA MENU DINAMIS
        if (this.bankSampah != null) {
            // --- JIKA SUDAH MEMBER ---
            panel.add(createSectionTitle("MENU UTAMA"));
            panel.add(createMenuLabel("Dashboard Home", "Home"));
            panel.add(createMenuLabel("Setor Sampah", "SetorSampah"));
            panel.add(createMenuLabel("Riwayat Transaksi", "Riwayat"));
            panel.add(createMenuLabel("Tukar Poin", "TukarPoin"));
            panel.add(createMenuLabel("Kirim Keluhan", "Keluhan"));
        } else {
            // --- JIKA BELUM MEMBER ---
            panel.add(createSectionTitle("AKTIVITAS"));
            panel.add(createMenuLabel("Gabung Bank Sampah", "JoinBank"));
        }

        // 4. Footer (Logout)
        panel.add(Box.createVerticalGlue()); // Dorong ke bawah
        panel.add(new JSeparator());
        panel.add(createMenuLabel("Logout", "Logout"));

        return panel;
    }

    public void switchPanel(String menuName) {
        JPanel nextPanel = null;

        // Routing ke Panel Pecahan
        switch (menuName) {
            case "Home":
                // Pastikan class ini ada di folder View/Panels/
                nextPanel = new View.PenyetorPanels.PenyetorHomePanel(currentUser);
                break;
            case "Profil":
                nextPanel = new View.PenyetorPanels.ProfilPenyetorPanel(currentUser);
                break;
            case "SetorSampah":
                nextPanel = new View.PenyetorPanels.SetorSampahPanel(currentUser, bankSampah);
                break;
            case "Riwayat":
                nextPanel = new View.PenyetorPanels.RiwayatTransaksiPanel(currentUser);
                break;
            case "TukarPoin":
                nextPanel = new View.PenyetorPanels.TukarPoinPanel(currentUser);
                break;
            case "Keluhan":
                nextPanel = new View.PenyetorPanels.KeluhanPanel(currentUser);
                break;
            case "JoinBank":
                // Passing 'this' agar panel bisa merefresh frame ini setelah join
                nextPanel = new View.PenyetorPanels.JoinBankPanel(currentUser, this);
                break;
            case "Logout":
                logoutAction();
                return; // Stop execution
            default:
                nextPanel = new JPanel(); // Panel kosong fallback
                break;
        }

        if (nextPanel != null) {
            contentPanel.removeAll();
            contentPanel.add(nextPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    private void logoutAction() {
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginView().setVisible(true);
            this.dispose();
        }
    }

    // =========================================================================
    // 4. HELPER UI METHODS
    // =========================================================================

    private JLabel createSectionTitle(String title) {
        JLabel label = new JLabel("  " + title);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(200, 255, 200));
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createMenuLabel(String text, String command) {
        JPanel btnPanel = new JPanel(new BorderLayout());
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnPanel.setBackground(GREEN_PRIMARY);
        btnPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel("   " + text);
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        label.setForeground(Color.WHITE);
        btnPanel.add(label, BorderLayout.CENTER);

        // Hover Effect & Click Listener
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchPanel(command);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                btnPanel.setBackground(GREEN_LIGHT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnPanel.setBackground(GREEN_PRIMARY);
            }
        };

        btnPanel.addMouseListener(ma);
        return btnPanel;
    }

    // ini nanti dipanggil sama join bank panel biar bisa balik ke sini lagi
    public void setBankSampah(BankSampah bank) {
        this.bankSampah = bank;

        refreshSidebar();    // langsung regenerasi menu
        switchPanel("Home"); // pindah ke home
    }

    private void refreshSidebar() {
        remove(menuPanel); // hapus sidebar lama

        menuPanel = createSidebar(); // buat sidebar baru
        add(menuPanel, BorderLayout.WEST);

        revalidate();
        repaint();
    }

}