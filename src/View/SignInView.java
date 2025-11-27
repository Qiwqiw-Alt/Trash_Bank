package View;

import javax.swing.*;
import Model.Admin;
import Model.Penyetor;
import Model.User;
import java.awt.*;
import java.util.UUID;

public class SignInView extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField namaField;
    private JTextField noHpField;
    private JComboBox<String> roleBox;
   private ImageIcon image = new ImageIcon("D:\\proyek-pbo\\Trash_Bank\\src\\Asset\\Image\\recycle-bin.png"); 
    private JButton registerButton;
    private JCheckBox termsCheckbox;

    private final String[] ROLES = {"Penyetor", "Admin"};

    public SignInView() {
        setTitle("Sign Up - Bank Sampah");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(image.getImage());

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
        headerLabel.setFont(new Font("Fredoka", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0x356A69));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 0));
        leftPanel.add(headerLabel, BorderLayout.NORTH);

        // Center Content (Image Template)
        JPanel imageContainer = new JPanel(new GridBagLayout());
        imageContainer.setBackground(Color.WHITE);
        ImageIcon loginImage = new ImageIcon("D:\\proyek-pbo\\Trash_Bank\\src\\Asset\\Image\\1010042-10 - Edited.png");
        Image img = loginImage.getImage();
        Image scaledImg = img.getScaledInstance(350, 350, Image.SCALE_SMOOTH); 
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
        imageContainer.add(imageLabel);
        leftPanel.add(imageContainer, BorderLayout.CENTER);

        // Footer Text
        JLabel footerLabel = new JLabel("Starts for free and get attractive offers", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Fredoka", Font.BOLD, 16));
        footerLabel.setForeground(new Color(0x356A69));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        leftPanel.add(footerLabel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        // =======================
        // 🔄 PANEL KANAN (FORM SIGN UP) - WARNA HIJAU GELAP
        // =======================
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(0x356A69)); // Warna Hijau Gelap

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 20, 6, 20); // Inset lebih kecil agar muat banyak field
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 0;

        // Title
        JLabel registerTitle = new JLabel("Get's started.", SwingConstants.LEFT);
        registerTitle.setFont(new Font("Fredoka", Font.BOLD, 30));
        registerTitle.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        rightPanel.add(registerTitle, gbc);

        // Subtitle (Already have an account)
        JPanel loginLinkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        loginLinkPanel.setBackground(new Color(0x356A69));
        JLabel loginLabel = new JLabel("Already have an account? ");
        loginLabel.setFont(new Font("Fredoka", Font.PLAIN, 14));
        loginLabel.setForeground(Color.WHITE);

        // Tombol Login (Link)
        JButton loginLinkButton = new JButton("Log in");
        loginLinkButton.setFont(new Font("Fredoka", Font.BOLD, 14));
        loginLinkButton.setForeground(Color.WHITE);
        loginLinkButton.setBackground(new Color(0x356A69));
        loginLinkButton.setBorderPainted(false);
        loginLinkButton.setContentAreaFilled(false);
        loginLinkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLinkButton.setMargin(new Insets(0, 0, 0, 0));

        loginLinkPanel.add(loginLabel);
        loginLinkPanel.add(loginLinkButton);

        gbc.gridy = 1;
        rightPanel.add(loginLinkPanel, gbc);

        // Dummy Space/Divider
        gbc.gridy = 2;
        rightPanel.add(Box.createVerticalStrut(10), gbc);

        // 1. Full Name
        namaField = new JTextField(25);
        namaField.setText("Full Name");
        namaField.setForeground(Color.GRAY);
        styleTextField(namaField);
        gbc.gridy = 5;
        rightPanel.add(namaField, gbc);

        // 2. Phone Number
        noHpField = new JTextField(25);
        noHpField.setText("Phone Number (No. HP)");
        noHpField.setForeground(Color.GRAY);
        styleTextField(noHpField);
        gbc.gridy = 5;
        rightPanel.add(noHpField, gbc);

        // 3. Role
        roleBox = new JComboBox<>(ROLES);
        roleBox.setSelectedItem("Penyetor");
        roleBox.setFont(new Font("Fredoka", Font.PLAIN, 14));
        roleBox.setBackground(Color.WHITE);
        roleBox.setPreferredSize(new Dimension(25, 40));
        gbc.gridy = 3;
        rightPanel.add(roleBox, gbc);
        
        // 4. Username
        usernameField = new JTextField(25);
        usernameField.setText("Username");
        usernameField.setForeground(Color.GRAY);
        styleTextField(usernameField);
        gbc.gridy = 6;
        rightPanel.add(usernameField, gbc);

        // 5. Password
        passwordField = new JPasswordField(25);
        passwordField.setText("Password");
        passwordField.setForeground(Color.GRAY);
        styleTextField(passwordField);
        gbc.gridy = 7;
        rightPanel.add(passwordField, gbc);

        // 6. Confirm Password
        confirmPasswordField = new JPasswordField(25);
        confirmPasswordField.setText("Confirm Password");
        confirmPasswordField.setForeground(Color.GRAY);
        styleTextField(confirmPasswordField);
        gbc.gridy = 8;
        rightPanel.add(confirmPasswordField, gbc);

        // Dummy Space/Divider
        gbc.gridy = 9;
        rightPanel.add(Box.createVerticalStrut(10), gbc);

        // Terms and Policy Checkbox
        termsCheckbox = new JCheckBox("I agree to platforms Terms of service and Privacy policy");
        termsCheckbox.setBackground(new Color(0x356A69));
        termsCheckbox.setForeground(Color.WHITE);
        termsCheckbox.setFocusPainted(false);
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        rightPanel.add(termsCheckbox, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dummy Space/Divider
        gbc.gridy = 11;
        rightPanel.add(Box.createVerticalStrut(5), gbc);

        // Register Button
        registerButton = new JButton("Sign up!");
        registerButton.setFont(new Font("Fredoka", Font.BOLD, 14));
        registerButton.setBackground(Color.WHITE);
        registerButton.setForeground(new Color(0x356A69));
        registerButton.setFocusPainted(false);
        gbc.gridy = 12;
        gbc.ipady = 10;
        rightPanel.add(registerButton, gbc);

        add(rightPanel, BorderLayout.CENTER);

        // Aksi Tombol
        registerButton.addActionListener(e -> registerUser());
        loginLinkButton.addActionListener(e -> {
            new LoginView().setVisible(true);
            dispose();
        });
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(25, 40));
        field.setBackground(Color.WHITE);
        field.setFont(new Font("Fredoka", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void registerUser() {
        String role = roleBox.getSelectedItem().toString();
        String nama = namaField.getText();
        String noHp = noHpField.getText();
        String user = usernameField.getText();
        String pass = String.valueOf(passwordField.getPassword());
        String confirmPass = String.valueOf(confirmPasswordField.getPassword());
        
        // 🔄 VALIDASI LENGKAP
        if (nama.isEmpty() || noHp.isEmpty() || user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mohon isi semua data pendaftaran (Nama Lengkap, No. HP, Username, Password, dan Konfirmasi Password)!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!pass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Konfirmasi Password tidak sama!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!termsCheckbox.isSelected()) {
             JOptionPane.showMessageDialog(this, "Anda harus menyetujui Syarat dan Ketentuan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (User u : LoginView.users) {
            if (u.getUsername().equals(user)) {
                JOptionPane.showMessageDialog(this, "Username sudah dipakai!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 🔄 PEMBUATAN OBJEK SESUAI MODEL
        String idUser = (role.equals("Admin") ? "adm-" : "pny-") + UUID.randomUUID().toString().substring(0, 5);

        if (role.equals("Admin")) {
            // KONSTRUKTOR ADMIN: new Admin(idAdmin, role, username, password, namaAdmin, noHp)
            Admin admin = new Admin(idUser, role, user, pass, nama, noHp);
            admin.setIdBankSampah(LoginView.bankSampah.getIdBank());
            LoginView.users.add(admin);
        } else {
            // KONSTRUKTOR PENYETOR: new Penyetor(idPenyetor, role, username, password, namaLengkap, noHp)
            Penyetor penyetor = new Penyetor(idUser, role, user, pass, nama, noHp);
            penyetor.setIdBankSampah(LoginView.bankSampah.getIdBank());
            LoginView.users.add(penyetor);
        }

        JOptionPane.showMessageDialog(this, "Akun berhasil dibuat sebagai " + role + "! Silakan login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        new LoginView().setVisible(true);
        dispose();
    }
}