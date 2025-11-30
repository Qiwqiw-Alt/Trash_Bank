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

        initComponents();
    }

    // Method untuk setup data awal (sebelum ada database)

    private void initComponents() {

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(500, 600));
        leftPanel.setBackground(Color.WHITE);

        // BAGIAN ATAS ATAU HEADER

        JLabel headerLabel = new JLabel("BANK SAMPAH", SwingConstants.LEFT);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0x356A69)); // Warna Hijau Gelap
        headerLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 0)); // Padding
        leftPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel imageContainer = new JPanel(new GridBagLayout());
        imageContainer.setBackground(Color.WHITE);
        // Menggunakan path absolut yang Anda berikan
        ImageIcon loginImage = new ImageIcon("src\\Asset\\Image\\1010042-10 - Edited.png");
        // Ukuran gambar diskala 350x350
        Image img = loginImage.getImage();
        Image scaledImg = img.getScaledInstance(350, 350, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
        imageContainer.add(imageLabel);
        leftPanel.add(imageContainer, BorderLayout.CENTER);

        // Footer Text
        JLabel footerLabel = new JLabel("Starts for free and get attractive offers", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        footerLabel.setForeground(new Color(0x356A69));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0)); // Padding bawah
        leftPanel.add(footerLabel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(0x356A69)); // Warna Hijau Gelap

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 0; // Reset internal padding

        // Title
        JLabel title = new JLabel("Welcome back.", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        rightPanel.add(title, gbc);

        // Subtitle (No Account Label)
        JPanel subtitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        subtitlePanel.setOpaque(false); // Agar background mengikuti rightPanel

        JLabel noAccountLabel = new JLabel("Don't have an account? ");
        noAccountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        noAccountLabel.setForeground(Color.WHITE);
        subtitlePanel.add(noAccountLabel);

        // Teks "Sign up" yang dapat diklik (Link Teks)
        JLabel signupLink = new JLabel("Sign up");
        signupLink.setFont(new Font("Arial", Font.BOLD, 14));
        signupLink.setForeground(new Color(0x67AE6E));
        signupLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        subtitlePanel.add(signupLink);

        gbc.gridy = 1;
        rightPanel.add(subtitlePanel, gbc);

        // Dummy Space/Divider
        gbc.gridy = 2;
        rightPanel.add(Box.createVerticalStrut(20), gbc);

        // Username Field (Langsung tanpa label di atas)
        usernameField = new JTextField(25);
        usernameField.setText("Username"); // Placeholder-like
        usernameField.setForeground(Color.GRAY);
        styleTextField(usernameField);
        gbc.gridy = 3;
        rightPanel.add(usernameField, gbc);

        // Password Field (Langsung tanpa label di atas)
        passwordField = new JPasswordField(25);
        passwordField.setText("Password"); // Placeholder-like
        passwordField.setForeground(Color.GRAY);
        styleTextField(passwordField);
        gbc.gridy = 4;
        rightPanel.add(passwordField, gbc);

        // Dummy Space/Divider
        gbc.gridy = 5;
        rightPanel.add(Box.createVerticalStrut(20), gbc);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(0x67AE6E)); // Warna Hijau
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        gbc.gridy = 6;
        gbc.ipady = 10;
        rightPanel.add(loginButton, gbc);
        gbc.ipady = 0;

        // Dummy Space/Divider
        gbc.gridy = 7;
        rightPanel.add(Box.createVerticalStrut(10), gbc);

        // Sign Up Button (Sebagai link di bawah)
        signupButton = new JButton("Sign up");
        signupButton.setFont(new Font("Arial", Font.PLAIN, 14));
        signupButton.setForeground(new Color(0x67AE6E));
        signupButton.setBackground(new Color(0x356A69));
        signupButton.setBorderPainted(false);
        signupButton.setContentAreaFilled(false);
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 8;
        rightPanel.add(signupButton, gbc);

        add(rightPanel, BorderLayout.CENTER); // Pasang ke posisi KANAN/TENGAH (CENTER)

        // Action Listeners
        loginButton.addActionListener(e -> loginAction());

        // 1. Action listener untuk link teks "Sign up" (di subtitle)
        signupLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new SignInView().setVisible(true);
                dispose(); // Tutup jendela login saat pindah
            }
        });

        // 2. Action listener untuk tombol "Sign up" (di gbc.gridy=8)
        signupButton.addActionListener(e -> {
            new SignInView().setVisible(true);
            dispose(); // Tutup jendela login saat pindah
        });

    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(25, 40));
        field.setBackground(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    // ===========================
    // LOGIC LOGIN (TIDAK DIUBAH)
    // ===========================
    private void loginAction() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan username dan password!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object user = LoginController.getService().loginUser(username, password);

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
}