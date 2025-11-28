package View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import Model.Admin;
import Model.Penyetor;
import Model.User;
import Model.BankSampah;
import Model.Complain;
import Model.Sampah;
import Model.Reward;
import Model.Transaksi;
import Model.PenukaranReward;
import java.util.UUID;

public class LoginView extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton;
    // Menggunakan path absolut yang Anda berikan
    private ImageIcon image = new ImageIcon("D:\\proyek-pbo\\Trash_Bank\\src\\Asset\\Image\\recycle-bin.png"); 

    // Global Static Data Lists
    public static ArrayList<User> users = new ArrayList<>();
    public static List<Complain> complains = new ArrayList<>();
    public static List<Sampah> sampahs = new ArrayList<>();
    public static List<Reward> rewards = new ArrayList<>();
    public static List<Transaksi> transactions = new ArrayList<>();
    public static List<PenukaranReward> penukaranRewards = new ArrayList<>();
    public static BankSampah bankSampah = new BankSampah("BS001", "Bank Sampah Hijau Lestari", "Jl. Pegangsaan Timur No. 17");


    // --- DUMMY DATA UNTUK TEST LOGIN ---
    private void setupInitialData() {
        if (users.isEmpty()) {
            // KONSTRUKTOR ADMIN BARU: new Admin(idAdmin, role, username, password, namaAdmin, noHp)
            Admin admin = new Admin("adm-001", "Admin", "admin", "admin", "Admin Utama", "081234567890");
            admin.setIdBankSampah(bankSampah.getIdBank());
            users.add(admin);

            // KONSTRUKTOR PENYETOR BARU: new Penyetor(idPenyetor, role, username, password, namaLengkap, noHp)
            Penyetor penyetor = new Penyetor("pny-001", "Penyetor", "penyetor", "penyetor", "Penyetor Biasa", "089876543210");
            penyetor.setIdBankSampah(bankSampah.getIdBank());
            users.add(penyetor);
        }
    }
    // --- AKHIR DUMMY DATA ---

    public LoginView() {
        setTitle("Login - Bank Sampah");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(image.getImage());

        setupInitialData();

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // =======================
        // 🔄 PANEL KIRI (GAMBAR) - WARNA PUTIH
        // =======================
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(500, 600));
        leftPanel.setBackground(Color.WHITE);

        // Header Bank Sampah
        JLabel headerLabel = new JLabel("BANK SAMPAH", SwingConstants.LEFT);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0x356A69)); // Warna Hijau Gelap
        headerLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 0)); // Padding
        leftPanel.add(headerLabel, BorderLayout.NORTH);

        // Center Content (Image Template)
        JPanel imageContainer = new JPanel(new GridBagLayout());
        imageContainer.setBackground(Color.WHITE);
        // Menggunakan path absolut yang Anda berikan
        ImageIcon loginImage = new ImageIcon("D:\\proyek-pbo\\Trash_Bank\\src\\Asset\\Image\\1010042-10 - Edited.png");
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

        add(leftPanel, BorderLayout.WEST); // Pasang ke posisi KIRI (WEST)

        // =======================
        // 🔄 PANEL KANAN (FORM LOGIN) - WARNA HIJAU GELAP
        // =======================
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

    // Helper method untuk styling JTextField
    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(25, 40));
        field.setBackground(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }


    private void loginAction() {
        String userInput = usernameField.getText();
        String passInput = String.valueOf(passwordField.getPassword());

        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Belum ada akun terdaftar! Buat akun dulu.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (User user : users) {
            if (user.getUsername().equals(userInput) && user.getPassword().equals(passInput)) {

                String role = (user instanceof Admin) ? "ADMIN" : "PENYETOR";
                String nama = (user instanceof Admin) ? ((Admin) user).getNamaAdmin() : ((Penyetor) user).getNamaLengkap();

                JOptionPane.showMessageDialog(this,
                    "Login berhasil sebagai " + role + ": " + nama + "\n(Dashboard dinonaktifkan sementara)", "Sukses", JOptionPane.INFORMATION_MESSAGE);

                dispose();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Username atau password salah!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LandingView().setVisible(true));
    }
}