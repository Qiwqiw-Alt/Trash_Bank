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
        currentBankSampah = BankSampahController.getBankSampah(this.IdBankSampah);
        String namaBank = (currentBankSampah != null) ? currentBankSampah.getNamaBank() : "Bank Sampah App";

        setTitle("Dashboard Admin - " + namaBank);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true); 

        initLayout();
        if(currentBankSampah == null){
            switchPanel("CreateBank");
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

        // Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(GREEN_PRIMARY);
        JLabel logo = new JLabel("BANK SAMPAH ADMIN", SwingConstants.CENTER);
        logo.setFont(new Font("Arial", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        logo.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        logoPanel.add(logo);
        panel.add(logoPanel);

        // Menu Items
        if (currentBankSampah == null) {
            panel.add(createMenuLabel("Buat Bank Sampah", "CreateBank"));
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        }

        panel.add(createMenuLabel("Home", "Home"));
        panel.add(createMenuLabel("Edit Profil", "Profil"));
        panel.add(createSectionTitle("MANAJEMEN USER"));
        panel.add(createMenuLabel("Lihat Penyetor", "ListMember"));
        panel.add(createSectionTitle("TRANSAKSI"));
        panel.add(createMenuLabel("Input Setoran", "GivePoin"));
        panel.add(createSectionTitle("DATA MASTER"));
        panel.add(createMenuLabel("Jenis Sampah", "AddSampah"));
        panel.add(createMenuLabel("Reward", "AddReward"));
        panel.add(createSectionTitle("LAINNYA"));
        panel.add(createMenuLabel("Evaluasi Komplain", "Komplain"));
        
        panel.add(Box.createVerticalGlue());
        panel.add(createMenuLabel("Logout", "Logout"));

        return panel;
    }

    public void switchPanel(String menuName) {
        if(currentBankSampah == null){
            boolean isAllowed = menuName.equals("Logout") || menuName.equals("Profil") || menuName.equals("Create Bank");
        
            if (!isAllowed) {
                JOptionPane.showMessageDialog(this, 
                    "Anda belum berada/membuat bank sampah, silahkan membuat bank sampah terlebih dahulu.", 
                    "Akses Ditolak", 
                    JOptionPane.WARNING_MESSAGE);
                return; 
            }
        }
        JPanel nextPanel = null;

        switch (menuName) {
            case "CreateBank":
                nextPanel = new View.AdminPanels.CreateBankSampahPanel(this, currentUser); 
                break;
            case "Home":
                nextPanel = new View.AdminPanels.AdminHomePanel(currentUser);
                break;
            case "Profil":
                nextPanel = new View.AdminPanels.ProfilAdminPanel(currentUser);
                break;
            case "ListMember":
                nextPanel = new View.AdminPanels.ListMemberPanel();
                break;
            case "GivePoin":
                // Butuh bankSampah untuk catat ID Bank di transaksi
                nextPanel = new View.AdminPanels.InputSetoranPanel(currentBankSampah); 
                break;
            case "AddSampah":
                nextPanel = new View.AdminPanels.ManajemenSampahPanel();
                break;
            case "AddReward":
                nextPanel = new View.AdminPanels.ManajemenRewardPanel();
                break;
            case "Komplain":
                nextPanel = new View.AdminPanels.EvaluasiKomplainPanel(currentUser);
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
        
        // Refresh Sidebar (agar menu Create hilang, atau layout terupdate)
        getContentPane().remove(0); // Hapus sidebar lama (posisi index 0 di BorderLayout)
        add(createSidebar(), BorderLayout.WEST); // Tambah sidebar baru
        
        // Pindah ke Home
        switchPanel("Home");
        
        revalidate();
    }

    // ... (Helper method createMenuLabel & createSectionTitle sama seperti sebelumnya) ...
    private JPanel createMenuLabel(String text, String command) {
        JPanel btn = new JPanel(new BorderLayout());
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setBackground(GREEN_PRIMARY);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel lbl = new JLabel("   " + text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.add(lbl, BorderLayout.CENTER);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { switchPanel(command); }
            public void mouseEntered(MouseEvent e) { btn.setBackground(GREEN_HOVER); }
            public void mouseExited(MouseEvent e) { btn.setBackground(GREEN_PRIMARY); }
        });
        return btn;
    }

    private JLabel createSectionTitle(String title) {
        JLabel lbl = new JLabel("  " + title);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(new Color(200, 255, 200));
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
}