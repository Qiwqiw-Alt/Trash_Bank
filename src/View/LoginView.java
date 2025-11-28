package View;

import javax.swing.*;

import Controller.LoginController;

import java.awt.*;

import Model.Admin;
import Model.Penyetor;


public class LoginView extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton;
    private ImageIcon image = new ImageIcon("recycle-bin.png");
    


    public LoginView() {
        setTitle("Login - Bank Sampah");
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(image.getImage());
        
        // Setup data awal jika kosong

        
        initComponents();
    }
    
    // Method untuk setup data awal (sebelum ada database)

    private void initComponents() {

        // Kode initComponents tetap sama seperti sebelumnya

        JPanel mainPanel = new JPanel(null);

        add(mainPanel);



        // PANEL KIRI (HIJAU)

        JPanel leftPanel = new JPanel(null);

        leftPanel.setBounds(0, 0, 350, 600);

        leftPanel.setBackground(new Color(0x67AE6E));



        JLabel welcomeTitle = new JLabel("Welcome Back!", SwingConstants.CENTER);

        welcomeTitle.setFont(new Font("Fredoka", Font.BOLD, 26));

        welcomeTitle.setForeground(Color.WHITE);

        welcomeTitle.setBounds(0, 200, 350, 40);

        leftPanel.add(welcomeTitle);



        JLabel desc1 = new JLabel("Silakan login untuk", SwingConstants.CENTER);

        desc1.setFont(new Font("Fredoka", Font.PLAIN, 16));

        desc1.setForeground(Color.WHITE);

        desc1.setBounds(0, 250, 350, 30);

        leftPanel.add(desc1);



        JLabel desc2 = new JLabel("mengakses aplikasi", SwingConstants.CENTER);

        desc2.setFont(new Font("Fredoka", Font.PLAIN, 16));

        desc2.setForeground(Color.WHITE);

        desc2.setBounds(0, 280, 350, 30);

        leftPanel.add(desc2);



        // PANEL KANAN (FORM LOGIN)

        JPanel rightPanel = new JPanel(null);

        rightPanel.setBounds(350, 0, 450, 600);

        rightPanel.setBackground(Color.WHITE);



        JLabel titleRight = new JLabel("Login Akun");

        titleRight.setFont(new Font("Fredoka", Font.BOLD, 26));

        titleRight.setBounds(160, 40, 300, 40);

        rightPanel.add(titleRight);



        // Username

        JLabel userLabel = new JLabel("Username:");

        userLabel.setFont(new Font("Fredoka", Font.PLAIN, 16));

        userLabel.setBounds(60, 140, 200, 25);

        rightPanel.add(userLabel);

        

        usernameField = new JTextField();

        usernameField.setFont(new Font("Fredoka", Font.PLAIN, 15));

        usernameField.setBounds(60, 170, 330, 35);

        rightPanel.add(usernameField);



        // Password

        JLabel passLabel = new JLabel("Password:");

        passLabel.setFont(new Font("Fredoka", Font.PLAIN, 16));

        passLabel.setBounds(60, 230, 200, 25);

        rightPanel.add(passLabel);



        passwordField = new JPasswordField();

        passwordField.setFont(new Font("Fredoka", Font.PLAIN, 15));

        passwordField.setBounds(60, 260, 330, 35);

        rightPanel.add(passwordField);



        // Tombol Login

        loginButton = new JButton("Login");

        loginButton.setBounds(60, 330, 145, 40);

        loginButton.setBackground(new Color(0x328E6E));

        loginButton.setForeground(Color.WHITE);

        loginButton.setFont(new Font("Fredoka", Font.BOLD, 15));

        loginButton.setFocusPainted(false);

        rightPanel.add(loginButton);



        // Tombol Sign Up

        signupButton = new JButton("Sign Up");

        signupButton.setBounds(245, 330, 145, 40);

        signupButton.setBackground(new Color(0x328E6E));

        signupButton.setForeground(Color.WHITE);

        signupButton.setFont(new Font("Fredoka", Font.BOLD, 15));

        signupButton.setFocusPainted(false);

        rightPanel.add(signupButton);



        // Tambahkan panel ke frame

        mainPanel.add(leftPanel);

        mainPanel.add(rightPanel);



        // Aksi tombol

        loginButton.addActionListener(e -> loginAction());

        signupButton.addActionListener(e -> {

            new SignInView().setVisible(true);

            dispose();

        });

    }

    // ===========================
    // LOGIC LOGIN (TIDAK DIUBAH)
    // ===========================
    private void loginAction() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan username dan password!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object user = LoginController.login(username, password);

        if (user == null) {
            JOptionPane.showMessageDialog(this, "Username atau password salah!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // USER DITEMUKAN → CEK TYPE
        if (user instanceof Admin) {
            Admin admin = (Admin) user;
            
            String idBank = admin.getIdBankSampah();
            if (idBank != null && idBank.equalsIgnoreCase("null")) {
                idBank = null;
            }

            JOptionPane.showMessageDialog(this,
                    "Login berhasil sebagai ADMIN: " + admin.getNamaAdmin());

            new DashboardAdminView(admin, idBank).setVisible(true);
            dispose();
            return;
        }

        if (user instanceof Penyetor) {
            Penyetor p = (Penyetor) user;
            JOptionPane.showMessageDialog(this,
                    "Login berhasil sebagai PENYETOR: " + p.getNamaLengkap());

            new DashboardPenyetorView(p).setVisible(true);
            dispose();
            return;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}