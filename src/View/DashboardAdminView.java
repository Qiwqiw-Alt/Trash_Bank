package View;

import Model.Admin;
import Model.BankSampah;
import javax.swing.*;

import Controller.BankSampahController;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardAdminView extends JFrame {
    
    private Admin currentUser;
    private String IdBankSampah;
    private BankSampah currentBankSampah;
    private JPanel contentPanel;
   
    // Constants
    private final Color GREEN_PRIMARY = new Color(0, 128, 0); 
    private final Color GREEN_HOVER = new Color(0, 150, 0); 

    public DashboardAdminView(Admin user, String IdBankSampah) {
        this.currentUser = user;
        this.IdBankSampah = IdBankSampah;
        
        // Cek apakah Admin punya bank sampah
        if (this.IdBankSampah != null) {
             currentBankSampah = BankSampahController.getService().getObjBankSampah(this.IdBankSampah);
        } else {
             currentBankSampah = null;
        }

        String namaBank = (currentBankSampah != null) ? currentBankSampah.getNamaBank() : "Bank Sampah (Belum Ada)";

        setTitle("Dashboard Admin - " + namaBank);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true); 

        initLayout();
 
        if(currentBankSampah == null){
   
            switchPanel("EmptyState"); 
        } else {
            switchPanel("Home");
        }
    }

    private void initLayout() {
        setLayout(new BorderLayout());

        // Sidebar
        add(createSidebar(), BorderLayout.WEST);

        // Content
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(260, 0)); 
        panel.setBackground(GREEN_PRIMARY);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // --- 1. LOGO AREA ---
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(GREEN_PRIMARY);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0)); // Padding atas bawah

        JLabel logoIcon = new JLabel("♻", SwingConstants.CENTER); // Ikon recycle
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.BOLD, 40));
        logoIcon.setForeground(Color.WHITE);
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoText = new JLabel("BANK SAMPAH", SwingConstants.CENTER);
        logoText.setFont(new Font("SansSerif", Font.BOLD, 20));
        logoText.setForeground(Color.WHITE);
        logoText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel logoSubText = new JLabel("ADMINISTRATOR", SwingConstants.CENTER);
        logoSubText.setFont(new Font("SansSerif", Font.PLAIN, 12));
        logoSubText.setForeground(new Color(200, 255, 200));
        logoSubText.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(logoIcon);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        logoPanel.add(logoText);
        logoPanel.add(logoSubText);
        
        panel.add(logoPanel);
        
        // Garis pemisah di bawah logo
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(220, 1));
        separator.setForeground(new Color(255, 255, 255, 100));
        separator.setBackground(new Color(255, 255, 255, 50));
        panel.add(separator);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Jarak setelah garis

        // --- 2. MENU ITEMS ---
        
        // Tombol Create Bank Sampah (Hanya muncul jika bank null)
        if (currentBankSampah == null) {
            panel.add(createMenuLabel("Buat Bank Sampah", "CreateBank"));
            panel.add(Box.createRigidArea(new Dimension(0, 10))); 
        }

        panel.add(createMenuLabel("Home", "Home"));
        panel.add(createMenuLabel("Edit Profil", "Profil"));

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createSectionTitle("MANAJEMEN USER"));
        panel.add(createMenuLabel("Lihat Penyetor", "ListMember"));

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createSectionTitle("TRANSAKSI"));
        panel.add(createMenuLabel("Input Setoran", "GivePoin"));

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createSectionTitle("DATA MASTER"));
        panel.add(createMenuLabel("Jenis Sampah", "AddSampah"));
        panel.add(createMenuLabel("Reward", "AddReward"));

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createSectionTitle("LAINNYA"));
        panel.add(createMenuLabel("Evaluasi Komplain", "Komplain"));
        
        // --- 3. FOOTER (LOGOUT) ---
        panel.add(Box.createVerticalGlue()); // Dorong ke bawah
        
        JSeparator footerSep = new JSeparator();
        footerSep.setMaximumSize(new Dimension(220, 1));
        footerSep.setForeground(new Color(255, 255, 255, 100));
        panel.add(footerSep);
        
        panel.add(createMenuLabel("🚪  Logout", "Logout"));
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Jarak bawah

        return panel;
    }

    // Method helper untuk membuat Label Menu lebih rapi
    private JPanel createMenuLabel(String text, String command) {
        JPanel btn = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 12)); // Layout kiri, gap icon-teks
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Tinggi tombol konsisten
        btn.setBackground(GREEN_PRIMARY);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15)); // Font lebih modern
        
        btn.add(lbl);

        // Hover Effect yang lebih halus
        btn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { switchPanel(command); }
            public void mouseEntered(MouseEvent e) { 
                btn.setBackground(GREEN_HOVER); 
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 15)); // Bold saat hover
            }
            public void mouseExited(MouseEvent e) { 
                btn.setBackground(GREEN_PRIMARY); 
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15)); // Normal lagi
            }
        });
        return btn;
    }

    // Method helper untuk judul Section
    private JLabel createSectionTitle(String title) {
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(new Color(180, 255, 180)); // Warna hijau sangat muda
        // Padding: Atas, Kiri, Bawah, Kanan
        lbl.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 0)); 
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Wrapper agar alignment works di BoxLayout
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, lbl.getPreferredSize().height + 10));
        return lbl;
    }

    public void switchPanel(String menuName) {
        
        // --- LOGIKA GATEKEEPER (PEMBATASAN AKSES) ---
        if(currentBankSampah == null) {
            // Daftar menu yang BOLEH diakses
            boolean isAllowed = menuName.equals("Logout") || 
                                menuName.equals("Profil") || 
                                menuName.equals("CreateBank") || 
                                menuName.equals("EmptyState"); // Tambahkan ini agar panel peringatan bisa tampil
        
            if (!isAllowed) {
                // Pesan Error jika menekan tombol lain
                JOptionPane.showMessageDialog(this, 
                    "Anda belum membuat akun bank sampah, silahkan buat terlebih dahulu.", 
                    "Akses Ditolak", 
                    JOptionPane.WARNING_MESSAGE);
                return; // Stop, jangan ganti panel
            }
        }
        else { 
            if (menuName.equals("CreateBank")) {
                JOptionPane.showMessageDialog(this, 
                    "Anda sudah memiliki akun bank sampah!", 
                    "Akses Ditolak", 
                    JOptionPane.WARNING_MESSAGE);
                return; // Stop
            }
        }
        
        JPanel nextPanel = null;

        switch (menuName) {
            case "EmptyState":
                // Panel Putih Peringatan
                nextPanel = new View.AdminPanels.NoBankViewPanel(this);
                break;
            case "CreateBank":
                nextPanel = new View.AdminPanels.CreateBankSampahPanel(this, currentUser); 
                break;
            case "Home":
                nextPanel = new View.AdminPanels.AdminHomePanel(currentUser, currentBankSampah);
                break;
            case "Profil":
                nextPanel = new View.AdminPanels.ProfilAdminPanel(currentUser);
                break;
            case "ListMember":
                nextPanel = new View.AdminPanels.ListMemberPanel(currentBankSampah);
                break;
            case "GivePoin":
                nextPanel = new View.AdminPanels.InputSetoranPanel(currentBankSampah); 
                break;
            case "AddSampah":
                nextPanel = new View.AdminPanels.ManajemenSampahPanel(currentBankSampah);
                break;
            case "AddReward":
                nextPanel = new View.AdminPanels.ManajemenRewardPanel(currentBankSampah);
                break;
            case "Komplain":
                nextPanel = new View.AdminPanels.EvaluasiKomplainPanel(currentUser, currentBankSampah);
                break;
            case "Logout":
                new LoginView().setVisible(true);
                dispose();
                return;
        }

        if (nextPanel != null) {
            contentPanel.removeAll();
            contentPanel.add(nextPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    public void onBankCreatedSuccess(BankSampah newBank) {
        this.currentBankSampah = newBank;
        this.IdBankSampah = newBank.getIdBank();
        
        setTitle("Dashboard Admin - " + newBank.getNamaBank());
        
        // Refresh Sidebar (agar menu Create hilang)
        getContentPane().remove(0); 
        add(createSidebar(), BorderLayout.WEST); 
        
        // Pindah ke Home
        switchPanel("Home");
        
        revalidate();
    }

}