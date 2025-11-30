package View;

import javax.swing.*;

import java.awt.*;
import Model.Penyetor;
import Service.BankSampahService;
import Model.BankSampah;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardPenyetorView extends JFrame {
    private Penyetor currentUser;
    private BankSampah bankSampah;
    private BankSampahService bss = new BankSampahService();

    private JPanel contentPanel; 
    private JPanel menuPanel; 
    
    // Icon & Style Constants
    private final Color GREEN_PRIMARY = new Color(0x356A69); // Hijau Gelap
    private final Color GREEN_LIGHT = new Color(0x67AE6E); // Hijau Terang/Aksen
    private final Color GRAY_NAV = new Color(0xF0F0F0); // Warna Latar Navigasi Landing

    // Variabel untuk melacak tombol mana yang sedang aktif
    private String activeLandingMenu = "Home"; 
    
    // Variabel untuk menyimpan referensi tombol Navigasi di Header (Landing & Dashboard)
    private JPanel homeNavButton;
    private JPanel joinBankNavButton; // Khusus mode Landing Page
    private JPanel logoutNavButton; // Khusus mode Landing Page

    // BARU: Variabel untuk tombol Navigasi Dashboard di Header
    private JPanel setorSampahNavButton;
    private JPanel riwayatNavButton;
    private JPanel tukarPoinNavButton;
    private JPanel keluhanNavButton;
    private JButton profileButtonEast; 

    private ImageIcon iconImage = new ImageIcon("Trash_Bank\\src\\Asset\\Image\\recycle-bin.png");

    public DashboardPenyetorView(Penyetor user) {
        this.currentUser = user;
        String idBank = user.getIdBankSampah();
        this.bankSampah = (idBank == null || idBank.equals("null") || idBank.isEmpty()) 
                             ? null 
                             : bss.getObjBankSampah(idBank);

        String namaBank = bankSampah != null ? bankSampah.getNamaBank() : "Bank Sampah App";
        setTitle("Dashboard Penyetor - " + namaBank);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(iconImage.getImage());
        setLocationRelativeTo(null);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        

        initLayout();
    }

    private void initLayout() {
        // Hapus semua komponen yang mungkin ada sebelumnya
        getContentPane().removeAll();
        setLayout(new BorderLayout()); 
        
        // Hapus menuPanel lama (Sidebar) jika ada
        if (menuPanel != null) {
            remove(menuPanel); 
            menuPanel = null;
        }

        // Inisialisasi contentPanel agar selalu ada di CENTER
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.white);
        add(contentPanel, BorderLayout.CENTER);


        if (this.bankSampah == null) {
            // Mode Belum Terdaftar: Menggunakan Landing Page Layout (Header/Navbar di NORTH)
            createLandingPageLayout();
        } else {
            // Mode Sudah Terdaftar: Menggunakan DASHBOARD HEADER LAYOUT
            createDashboardHeaderLayout();
        }
        
        switchPanel("Home"); // Muat panel Home secara default

        revalidate();
        repaint();
    }

    // =========================================================================
    // --- Layout Khusus Mode Sudah Daftar (Dashboard Header) ---
    // =========================================================================
    
    private void createDashboardHeaderLayout() {
        // --- Header (Navbar) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(GREEN_PRIMARY);
        headerPanel.setPreferredSize(new Dimension(this.getWidth(), 70));
        
        // 1. Logo (WEST)
        String namaBank = bankSampah != null ? bankSampah.getNamaBank().toUpperCase() : "DASHBOARD";
        JLabel headerTitle = new JLabel(namaBank); 
        headerTitle.setFont(new Font("Fredoka", Font.BOLD, 24));
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        headerPanel.add(headerTitle, BorderLayout.WEST);
        
        // 2. Tombol Navigasi Dashboard di Tengah (CENTER) 
        JPanel centerNavPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); 
        centerNavPanel.setOpaque(false);
        
        // Menu Dashboard
        homeNavButton = createHeaderMenuButton("HOME", "Home"); 
        setorSampahNavButton = createHeaderMenuButton("SETOR SAMPAH", "SetorSampah");
        riwayatNavButton = createHeaderMenuButton("RIWAYAT TRANSAKSI", "Riwayat");
        tukarPoinNavButton = createHeaderMenuButton("TUKAR POIN", "TukarPoin");
        keluhanNavButton = createHeaderMenuButton("KIRIM KELUHAN", "Keluhan");
        
        centerNavPanel.add(homeNavButton);
        centerNavPanel.add(setorSampahNavButton);
        centerNavPanel.add(riwayatNavButton);
        centerNavPanel.add(tukarPoinNavButton);
        centerNavPanel.add(keluhanNavButton);
        
        headerPanel.add(centerNavPanel, BorderLayout.CENTER);
        
        // 3. Tombol Profile & Logout (EAST)
        JPanel eastWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        eastWrapper.setOpaque(false);
        
        // Tombol Profile 
        profileButtonEast = new JButton("PROFILE (" + currentUser.getNamaLengkap().split(" ")[0].toUpperCase() + ")"); 
        profileButtonEast.setFont(new Font("Fredoka", Font.BOLD, 14));
        profileButtonEast.setBackground(Color.WHITE); 
        profileButtonEast.setForeground(GREEN_PRIMARY); 
        profileButtonEast.setFocusPainted(false);
        profileButtonEast.setMargin(new Insets(10, 15, 10, 15));
        
        profileButtonEast.addActionListener(e -> {
            activeLandingMenu = "Profil"; 
            updateHeaderMenuHighlight(); 
            switchPanel("Profil");
        });
        eastWrapper.add(profileButtonEast);
        
        // Tombol Logout
        JButton logoutButton = new JButton("LOGOUT");
        logoutButton.setFont(new Font("Fredoka", Font.BOLD, 14));
        logoutButton.setBackground(GREEN_LIGHT); 
        logoutButton.setForeground(Color.WHITE); 
        logoutButton.setFocusPainted(false);
        logoutButton.setMargin(new Insets(10, 15, 10, 15));
        logoutButton.addActionListener(e -> logoutAction());
        eastWrapper.add(logoutButton);

        headerPanel.add(eastWrapper, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH); 
        updateHeaderMenuHighlight();
    }


    // =========================================================================
    // --- Layout Khusus Mode Belum Daftar (Landing Page) --- (TIDAK DIUBAH)
    // =========================================================================
    private void createLandingPageLayout() {
        // --- Header (Navbar) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(GREEN_PRIMARY);
        headerPanel.setPreferredSize(new Dimension(this.getWidth(), 70));
        
        // 1. Logo (WEST)
        JLabel headerTitle = new JLabel("BANK SAMPAH");
        headerTitle.setFont(new Font("Fredoka", Font.BOLD, 24));
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        headerPanel.add(headerTitle, BorderLayout.WEST);
        
        // 2. Tombol Navigasi di Tengah (CENTER) 
        JPanel centerNavPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0)); 
        centerNavPanel.setOpaque(false);
        
        // Tombol HOME
        homeNavButton = createHeaderMenuButton("HOME", "Home");
        centerNavPanel.add(homeNavButton);
        
        // Tombol DAFTAR BANK SAMPAH
        joinBankNavButton = createHeaderMenuButton("DAFTAR BANK SAMPAH", "JoinBank");
        centerNavPanel.add(joinBankNavButton);
        
        // Tombol LOGOUT 
        logoutNavButton = createHeaderMenuButton("LOGOUT", "Logout"); 
        centerNavPanel.add(logoutNavButton);
        
        headerPanel.add(centerNavPanel, BorderLayout.CENTER); 
        
        // 3. Tombol Profile (EAST)
        JButton profileButton = new JButton("LIHAT PROFILE");
        profileButton.setFont(new Font("Fredoka", Font.BOLD, 14));
        profileButton.setBackground(Color.WHITE); 
        profileButton.setForeground(GREEN_PRIMARY); 
        profileButton.setFocusPainted(false);
        profileButton.setMargin(new Insets(10, 20, 10, 20)); 

        JPanel profileWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 15)); 
        profileWrapper.setOpaque(false);
        profileWrapper.add(profileButton);
        
        headerPanel.add(profileWrapper, BorderLayout.EAST);
        
        // Logic untuk LIHAT PROFILE
        profileButton.addActionListener(e -> {
            activeLandingMenu = "Profil"; 
            updateHeaderMenuHighlight(); 
            switchPanel("Profil");
        });
        
        add(headerPanel, BorderLayout.NORTH); 
        updateHeaderMenuHighlight();
    }
    
    // =========================================================================
    // --- UI HELPER METHODS ---
    // =========================================================================

    private JPanel createHeaderMenuButton(String text, String command) {
        JPanel btnPanel = new JPanel(new BorderLayout()); 
        btnPanel.setBackground(GREEN_PRIMARY);
        btnPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnPanel.setPreferredSize(new Dimension(text.length() * 8 + 30, 70)); 

        JLabel label = new JLabel(text, SwingConstants.CENTER); 
        label.setFont(new Font("Fredoka", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        btnPanel.add(label, BorderLayout.CENTER);

        // Hover & Click Listener
        btnPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (command.equals("Logout")) {
                    logoutAction(); 
                    return;
                }
                
                activeLandingMenu = command;
                updateHeaderMenuHighlight();
                switchPanel(command);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!command.equals(activeLandingMenu)) {
                    btnPanel.setBackground(GREEN_LIGHT); 
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!command.equals(activeLandingMenu)) {
                    btnPanel.setBackground(GREEN_PRIMARY); 
                }
            }
        });
        
        return btnPanel;
    }
    
    // --- Mengatur Highlight untuk Navigasi Header (Kompatibel Dua Mode) ---
    private void updateHeaderMenuHighlight() {
        boolean isDashboardMode = this.bankSampah != null;
        JPanel[] dashboardButtons = {homeNavButton, setorSampahNavButton, riwayatNavButton, tukarPoinNavButton, keluhanNavButton};
        JPanel[] landingButtons = {homeNavButton, joinBankNavButton};
        
        JPanel[] buttonsToProcess = isDashboardMode ? dashboardButtons : landingButtons;
        
        // 1. Proses Highlight
        for (JPanel btn : buttonsToProcess) {
            if (btn != null) {
                boolean isActive = false;
                // Cek apakah tombol yang sedang diproses cocok dengan activeLandingMenu
                if (btn == homeNavButton && activeLandingMenu.equals("Home")) isActive = true;
                if (btn == joinBankNavButton && activeLandingMenu.equals("JoinBank")) isActive = true;
                if (btn == setorSampahNavButton && activeLandingMenu.equals("SetorSampah")) isActive = true;
                if (btn == riwayatNavButton && activeLandingMenu.equals("Riwayat")) isActive = true;
                if (btn == tukarPoinNavButton && activeLandingMenu.equals("TukarPoin")) isActive = true;
                if (btn == keluhanNavButton && activeLandingMenu.equals("Keluhan")) isActive = true;
                
                if (isActive) {
                    btn.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.WHITE));
                    btn.setBackground(GREEN_PRIMARY);
                } else {
                    btn.setBorder(BorderFactory.createEmptyBorder());
                    btn.setBackground(GREEN_PRIMARY);
                }
            }
        }
        
        // 2. Penanganan Khusus untuk Profil dan Logout (Tidak ada highlight aktif)
        if (activeLandingMenu.equals("Profil")) {
             for (JPanel btn : buttonsToProcess) {
                 if (btn != null) btn.setBorder(BorderFactory.createEmptyBorder());
             }
        }
        
        // Logout Nav Button (Hanya ada di Landing Page)
        if (logoutNavButton != null) {
            logoutNavButton.setBorder(BorderFactory.createEmptyBorder());
            logoutNavButton.setBackground(GREEN_PRIMARY);
        }
        
        if (contentPanel != null) {
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }


    private JPanel createHomeLandingPanel() {
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(Color.WHITE);
        
        JPanel mainContentArea = new JPanel(new BorderLayout()); 
        mainContentArea.setBackground(Color.WHITE);

        JPanel contentContainer = new JPanel(new GridBagLayout()); 
        contentContainer.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 50, 0, 50); 
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel textButtonPanel = new JPanel();
        textButtonPanel.setLayout(new BoxLayout(textButtonPanel, BoxLayout.Y_AXIS));
        textButtonPanel.setBackground(Color.WHITE);
        
        JLabel title1 = new JLabel("SELAMAT DATANG DI");
        title1.setFont(new Font("Fredoka", Font.BOLD, 30));
        title1.setForeground(GREEN_PRIMARY);
        title1.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel title2 = new JLabel("BANK SAMPAH!");
        title2.setFont(new Font("Fredoka", Font.BOLD, 40));
        title2.setForeground(GREEN_PRIMARY);
        title2.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        title2.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel description = new JLabel("Ubah barang tidak terpakai menjadi sesuatu yang lebih berharga");
        description.setFont(new Font("Fredoka", Font.PLAIN, 18));
        description.setForeground(GREEN_PRIMARY);
        description.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton startButton = new JButton("DAFTAR BANK SAMPAH");
        startButton.setFont(new Font("Fredoka", Font.BOLD, 14));
        startButton.setBackground(GREEN_PRIMARY);
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setPreferredSize(new Dimension(250, 50)); 
        startButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        startButton.addActionListener(e -> {
            activeLandingMenu = "JoinBank"; 
            updateHeaderMenuHighlight(); 
            switchPanel("JoinBank");
        });

        textButtonPanel.add(title1);
        textButtonPanel.add(title2);
        textButtonPanel.add(description);
        textButtonPanel.add(startButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.LINE_START;
        contentContainer.add(textButtonPanel, gbc);

        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(Color.WHITE);
        
        ImageIcon newIllustrationIcon = new ImageIcon("Trash_Bank\\\\src\\\\Asset\\\\Image\\\\e1xf_1j1j_220225.jpg"); 
        
        JLabel imageLabel;
        if (newIllustrationIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image img = newIllustrationIcon.getImage();
            Image scaledImg = img.getScaledInstance(450, 450, Image.SCALE_SMOOTH); 
            imageLabel = new JLabel(new ImageIcon(scaledImg));
        } else {
            imageLabel = new JLabel("[CONTOH GAMBAR]", SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(550, 550));
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }

        imagePanel.add(imageLabel);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.LINE_END;
        contentContainer.add(imagePanel, gbc);

        mainContentArea.add(contentContainer, BorderLayout.CENTER); 
        homePanel.add(mainContentArea, BorderLayout.CENTER);

        // Footer Kosong
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(Color.WHITE); 
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); 
        
        homePanel.add(footerPanel, BorderLayout.SOUTH);
        
        return homePanel;
    }


    private JPanel createSidebar() {
        return new JPanel();
    }


    public void switchPanel(String menuName) {
        JPanel nextPanel = null;

        // Routing ke Panel Pecahan
        switch (menuName) {
            case "Home":
                activeLandingMenu = "Home"; 
                if (this.bankSampah == null) {
                    nextPanel = createHomeLandingPanel();
                } else {
                    nextPanel = new View.PenyetorPanels.PenyetorHomePanel(currentUser);
                }
                break;
            case "Profil":
                activeLandingMenu = "Profil"; 
                nextPanel = new View.PenyetorPanels.ProfilPenyetorPanel(currentUser);
                break;
            case "SetorSampah":
                activeLandingMenu = "SetorSampah"; 
                nextPanel = new View.PenyetorPanels.SetorSampahPanel(currentUser, bankSampah);
                break;
            case "Riwayat":
                activeLandingMenu = "Riwayat"; 
                nextPanel = new View.PenyetorPanels.RiwayatTransaksiPanel(currentUser);
                break;
            case "TukarPoin":
                activeLandingMenu = "TukarPoin"; 
                nextPanel = new View.PenyetorPanels.TukarPoinPanel(currentUser);
                break;
            case "Keluhan":
                activeLandingMenu = "Keluhan"; 
                nextPanel = new View.PenyetorPanels.KeluhanPanel(currentUser);
                break;
            case "JoinBank":
                activeLandingMenu = "JoinBank"; 
                nextPanel = new View.PenyetorPanels.JoinBankPanel(currentUser, this);
                break;
            case "Logout":
                logoutAction();
                return; 
            default:
                nextPanel = new JPanel(); 
                break;
        }

        // Handle panel switching
        if (contentPanel != null) {
            // Hapus komponen di CENTER
            contentPanel.removeAll();
            
            // Tambahkan nextPanel ke CENTER
            contentPanel.add(nextPanel, BorderLayout.CENTER);
            
            // Update highlight setelah panel diswitch 
            updateHeaderMenuHighlight();
            
            contentPanel.revalidate();
            contentPanel.repaint();
        } else {
             // Fallback
             getContentPane().add(nextPanel, BorderLayout.CENTER);
             getContentPane().revalidate();
             getContentPane().repaint();
        }
    }

    private void logoutAction() {
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // **MEMBUKA KEMBALI JENDELA LOGIN**
                new View.LoginView().setVisible(true); 
            } catch (Exception ex) {
                System.err.println("Gagal memuat LoginView. Pastikan class LoginView ada di package View.");
            }
            this.dispose(); // Tutup jendela Dashboard saat ini
        }
    }

    // =========================================================================
    // HELPER UI METHODS (Sidebar style - tidak digunakan di mode dashboard baru)
    // =========================================================================

    private JLabel createSectionTitle(String title) {
        JLabel label = new JLabel("  " + title);
        label.setFont(new Font("Fredoka", Font.BOLD, 12));
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

        JLabel label = new JLabel("   " + text);
        label.setFont(new Font("Fredoka", Font.PLAIN, 15));
        label.setForeground(Color.WHITE);
        btnPanel.add(label, BorderLayout.CENTER);

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

    public void setBankSampah(BankSampah bank) {
        this.bankSampah = bank;
        initLayout(); 
    }
}