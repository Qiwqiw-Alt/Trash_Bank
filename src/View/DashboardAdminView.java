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
    private ImageIcon iconImage = new ImageIcon("Trash_Bank\\src\\Asset\\Image\\recycle-bin.png");
    private final Color GREEN_PRIMARY = new Color(0x356A69);
    private final Color GREEN_HOVER = new Color(0x67AE6E); 
    private final Color GREEN_LIGHT = new Color(0x67AE6E); 
    private String activeMenu = "Home"; 
    
    private JPanel homeNavButton;
    private JPanel createBankNavButton;
    private JPanel listMemberNavButton;
    private JPanel givePoinNavButton;
    private JPanel addSampahNavButton;
    private JPanel addRewardNavButton;
    private JPanel komplainNavButton;


    public DashboardAdminView(Admin user, String IdBankSampah) {
        this.currentUser = user;
        this.IdBankSampah = IdBankSampah;
        
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
        setIconImage(iconImage.getImage());
        initLayout();

        if(currentBankSampah == null){
            switchPanel("EmptyState"); 
        } else {
            switchPanel("Home");
        }
    }

    private void initLayout() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        createHeader();

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }
    
    private void createHeader() {
        Component existingHeader = ((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.NORTH);
        if (existingHeader != null) {
            remove(existingHeader);
        }
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(GREEN_PRIMARY); 
        headerPanel.setPreferredSize(new Dimension(this.getWidth(), 70));
        
   
        String titleText = (currentBankSampah != null) ? currentBankSampah.getNamaBank().toUpperCase() : "BANK SAMPAH ADMIN";
        JLabel headerTitle = new JLabel(titleText);
        headerTitle.setFont(new Font("Fredoka", Font.BOLD, 24));
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        headerPanel.add(headerTitle, BorderLayout.WEST);
        
        JPanel centerNavPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); 
        centerNavPanel.setOpaque(false);
        
        JPanel eastWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        eastWrapper.setOpaque(false);
        
        JButton profileButton = new JButton("EDIT PROFIL");
        profileButton.setFont(new Font("Fredoka", Font.BOLD, 14));
        profileButton.setBackground(Color.WHITE); 
        profileButton.setForeground(GREEN_PRIMARY); 
        profileButton.setFocusPainted(false);
        profileButton.setMargin(new Insets(10, 15, 10, 15));
        profileButton.addActionListener(e -> {
            activeMenu = "Profil"; 
            updateHeaderMenuHighlight(); 
            switchPanel("Profil");
        });
        eastWrapper.add(profileButton);

        JButton logoutButton = new JButton("LOGOUT");
        logoutButton.setFont(new Font("Fredoka", Font.BOLD, 14));
        logoutButton.setBackground(GREEN_LIGHT); 
        logoutButton.setForeground(Color.WHITE); 
        logoutButton.setFocusPainted(false);
        logoutButton.setMargin(new Insets(10, 15, 10, 15));
        logoutButton.addActionListener(e -> switchPanel("Logout"));
        eastWrapper.add(logoutButton);
        
        headerPanel.add(eastWrapper, BorderLayout.EAST);
        
        
        if (currentBankSampah == null) {
            createBankNavButton = createHeaderMenuButton("BUAT BANK SAMPAH", "CreateBank");
            
            centerNavPanel.add(createBankNavButton);
        } else {
            homeNavButton = createHeaderMenuButton("HOME", "Home");
            listMemberNavButton = createHeaderMenuButton("LIHAT PENYETOR", "ListMember");
            givePoinNavButton = createHeaderMenuButton("INPUT SETORAN", "GivePoin");
            addSampahNavButton = createHeaderMenuButton("JENIS SAMPAH", "AddSampah");
            addRewardNavButton = createHeaderMenuButton("REWARD", "AddReward");
            komplainNavButton = createHeaderMenuButton("KOMPLAIN", "Komplain");
            
            centerNavPanel.add(homeNavButton);
            centerNavPanel.add(listMemberNavButton);
            centerNavPanel.add(givePoinNavButton);
            centerNavPanel.add(addSampahNavButton);
            centerNavPanel.add(addRewardNavButton);
            centerNavPanel.add(komplainNavButton);
        }
        
        headerPanel.add(centerNavPanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH); 
        updateHeaderMenuHighlight();
    }
    
    private JPanel createHeaderMenuButton(String text, String command) {
        JPanel btnPanel = new JPanel(new BorderLayout()); 
        btnPanel.setBackground(GREEN_PRIMARY);
        btnPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnPanel.setPreferredSize(new Dimension(text.length() * 8 + 30, 70)); 

        JLabel label = new JLabel(text, SwingConstants.CENTER); 
        label.setFont(new Font("Fredoka", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        btnPanel.add(label, BorderLayout.CENTER);

        btnPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (command.equals("Logout")) {
                    switchPanel("Logout"); 
                    return;
                }
                
                activeMenu = command;
                updateHeaderMenuHighlight();
                switchPanel(command);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!command.equals(activeMenu)) {
                    btnPanel.setBackground(GREEN_HOVER); 
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!command.equals(activeMenu)) {
                    btnPanel.setBackground(GREEN_PRIMARY); 
                }
            }
        });
        
        return btnPanel;
    }
    
    private void updateHeaderMenuHighlight() {
        boolean isDashboardMode = this.currentBankSampah != null;
        
        JPanel[] buttonsToProcess;
        
        if (isDashboardMode) {
            buttonsToProcess = new JPanel[]{
                homeNavButton, listMemberNavButton, 
                givePoinNavButton, addSampahNavButton, addRewardNavButton, 
                komplainNavButton
            };
        } else {
            buttonsToProcess = new JPanel[]{createBankNavButton};
        }
        
        for (JPanel btn : buttonsToProcess) {
            if (btn != null) {
                boolean isActive = false;
                
                if (btn == homeNavButton && activeMenu.equals("Home")) isActive = true;
                if (btn == createBankNavButton && activeMenu.equals("CreateBank")) isActive = true;
                if (btn == listMemberNavButton && activeMenu.equals("ListMember")) isActive = true;
                if (btn == givePoinNavButton && activeMenu.equals("GivePoin")) isActive = true;
                if (btn == addSampahNavButton && activeMenu.equals("AddSampah")) isActive = true;
                if (btn == addRewardNavButton && activeMenu.equals("AddReward")) isActive = true;
                if (btn == komplainNavButton && activeMenu.equals("Komplain")) isActive = true;
                if (activeMenu.equals("EmptyState") && btn == createBankNavButton) isActive = true;

                if (isActive) {
                    btn.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.WHITE));
                    btn.setBackground(GREEN_PRIMARY);
                } else {
                    btn.setBorder(BorderFactory.createEmptyBorder());
                    btn.setBackground(GREEN_PRIMARY);
                }
            }
        }
    }


    public void switchPanel(String menuName) {
        if(currentBankSampah == null) {
            boolean isAllowed = menuName.equals("Logout") || 
                                menuName.equals("Profil") || 
                                menuName.equals("CreateBank") || 
                                menuName.equals("EmptyState"); 
        
            if (!isAllowed) {
                JOptionPane.showMessageDialog(this, 
                    "Anda belum membuat akun bank sampah, silahkan buat terlebih dahulu.", 
                    "Akses Ditolak", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        else { 
            if (menuName.equals("CreateBank") || menuName.equals("EmptyState")) {
                JOptionPane.showMessageDialog(this, 
                    "Anda sudah memiliki akun bank sampah!", 
                    "Akses Ditolak", 
                    JOptionPane.WARNING_MESSAGE);
                activeMenu = "Home";
                switchPanel("Home");
                return;
            }
        }
        
        JPanel nextPanel = null;
        activeMenu = menuName;

        switch (menuName) {
            case "EmptyState":
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
                nextPanel = new View.AdminPanels.SetoranPenyetorPanel(currentBankSampah); 
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
            
            updateHeaderMenuHighlight(); 
            
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    public void onBankCreatedSuccess(BankSampah newBank) {
        this.currentBankSampah = newBank;
        this.IdBankSampah = newBank.getIdBank();
        
        setTitle("Dashboard Admin - " + newBank.getNamaBank());
        
        createHeader();
        
        switchPanel("Home");
        
        revalidate();
    }
    
    // --- DIHAPUS/DIKOSONGKAN (Mantan Sidebar) ---
    private JPanel createSidebar() { return new JPanel(); }
}