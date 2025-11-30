package View;

import javax.swing.*;

import Controller.SignInController;

import java.awt.*;

public class SignInView extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField namaField;
//    private JTextField idField;
    private JTextField noHpField;
    private JComboBox<String> roleBox;
    private ImageIcon image = new ImageIcon("Trash_Bank\\\\src\\\\Asset\\\\Image\\\\recycle-bin.png");
    private JButton registerButton, backButton;

    public SignInView() {
        setTitle("Sign Up - Bank Sampah");
        setSize(800, 600); // Diperbesar untuk menampung field tambahan
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(image.getImage());

        initComponents();
    }

   private void initComponents() {

    // Ukuran frame 800x700 (diatur di constructor), lebih besar dari LoginView 800x600 untuk menampung field lebih banyak.
    setLayout(new BorderLayout()); 

    // ===================================
    // PANEL KIRI (500px - IMAGE/HEADER)
    // Mirip dengan sisi KIRI LoginView (putih)
    // ===================================
    JPanel leftPanel = new JPanel(new BorderLayout());
    leftPanel.setPreferredSize(new Dimension(400, 600));
    leftPanel.setBackground(Color.WHITE);

    // BAGIAN ATAS ATAU HEADER (Logo)
    JLabel headerLabel = new JLabel("BANK SAMPAH", SwingConstants.LEFT);
    headerLabel.setFont(new Font("Fredoka", Font.BOLD, 24));
    headerLabel.setForeground(new Color(0x356A69)); // Warna Hijau Gelap
    headerLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 0)); // Padding
    leftPanel.add(headerLabel, BorderLayout.NORTH);

    // Kontainer Gambar (di Tengah)
    JPanel imageContainer = new JPanel(new GridBagLayout());
    imageContainer.setBackground(Color.WHITE);
    
    // Placeholder untuk Gambar Ilustrasi (Anda bisa mengganti path ini)
    ImageIcon signupImage = new ImageIcon("Trash_Bank\\src\\Asset\\Image\\1010042-10 - Edited.png\\"); // Ganti dengan gambar Sign Up
    Image img = signupImage.getImage();
    // Skala gambar lebih kecil karena panel lebih tinggi
    Image scaledImg = img.getScaledInstance(350, 350, Image.SCALE_SMOOTH); 
    JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
    imageContainer.add(imageLabel);
    leftPanel.add(imageContainer, BorderLayout.CENTER);

    // Footer Text
    JLabel footerLabel = new JLabel("Daftarkan dirimu dan mulai peduli lingkungan!", SwingConstants.CENTER);
    footerLabel.setFont(new Font("Fredoka", Font.BOLD, 16));
    footerLabel.setForeground(new Color(0x356A69));
    footerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0)); // Padding bawah
    leftPanel.add(footerLabel, BorderLayout.SOUTH);

    add(leftPanel, BorderLayout.WEST);

    // ===================================
    // PANEL KANAN (300px - FORM)
    // Mirip dengan sisi KANAN LoginView (Hijau Gelap)
    // ===================================
    JPanel rightPanel = new JPanel(new GridBagLayout());
    rightPanel.setBackground(new Color(0x356A69)); // Warna Hijau Gelap

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 20, 8, 20); // Padding antara komponen
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.ipadx = 0; // Reset internal padding
    gbc.gridwidth = 2; // Lebar field mencakup 2 kolom

    int y = 0;

    // Title
    JLabel title = new JLabel("Create Your Account", SwingConstants.LEFT);
    title.setFont(new Font("Fredoka", Font.BOLD, 26));
    title.setForeground(Color.WHITE);
    gbc.gridx = 0;
    gbc.gridy = y++;
    rightPanel.add(title, gbc);

    // Subtitle (Text Link to LoginView)
    JPanel subtitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    subtitlePanel.setOpaque(false);

    JLabel alreadyHaveAccount = new JLabel("Sudah punya akun? ");
    alreadyHaveAccount.setFont(new Font("Fredoka", Font.PLAIN, 12));
    alreadyHaveAccount.setForeground(Color.WHITE);
    subtitlePanel.add(alreadyHaveAccount);

    JLabel loginLink = new JLabel("Login di sini");
    loginLink.setFont(new Font("Fredoka", Font.BOLD, 12));
    loginLink.setForeground(new Color(0x67AE6E));
    loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
    subtitlePanel.add(loginLink);
    
    gbc.gridy = y++;
    gbc.insets = new Insets(0, 20, 20, 20); // Sedikit lebih rapat ke atas
    rightPanel.add(subtitlePanel, gbc);
    gbc.insets = new Insets(8, 20, 8, 20); // Reset padding

    // Role Box (Daftar Sebagai)
    roleBox = new JComboBox<>(new String[]{"Admin", "Penyetor"});
    styleField(roleBox);
    gbc.gridy = y++;
    rightPanel.add(roleBox, gbc);

    // Nama Lengkap
    namaField = new JTextField(25);
    namaField.setText("Nama Lengkap");
    styleTextField(namaField);
    gbc.gridy = y++;
    rightPanel.add(namaField, gbc);

    // No. HP
    noHpField = new JTextField(25);
    noHpField.setText("Nomor HP");
    styleTextField(noHpField);
    gbc.gridy = y++;
    rightPanel.add(noHpField, gbc);

    // Username
    usernameField = new JTextField(25);
    usernameField.setText("Username"); 
    styleTextField(usernameField);
    gbc.gridy = y++;
    rightPanel.add(usernameField, gbc);

    // Password
    passwordField = new JPasswordField(25);
    passwordField.setText("Password");
    styleTextField(passwordField);
    gbc.gridy = y++;
    rightPanel.add(passwordField, gbc);

    // Dummy Space/Divider
    gbc.gridy = y++;
    rightPanel.add(Box.createVerticalStrut(10), gbc);

    // Button Container (Register dan Back)
    JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
    buttonContainer.setOpaque(false);

    // Register Button
    registerButton = new JButton("Register");
    registerButton.setFont(new Font("Fredoka", Font.BOLD, 14));
    registerButton.setBackground(new Color(0x67AE6E)); // Warna Hijau
    registerButton.setForeground(Color.WHITE);
    registerButton.setFocusPainted(false);
    registerButton.setPreferredSize(new Dimension(130, 40)); 
    buttonContainer.add(registerButton);

    // Back Button
    backButton = new JButton("Back");
    backButton.setFont(new Font("Fredoka", Font.BOLD, 14));
    backButton.setBackground(new Color(0x67AE6E)); 
    backButton.setForeground(Color.WHITE);
    backButton.setFocusPainted(false);
    backButton.setPreferredSize(new Dimension(130, 40));
    buttonContainer.add(backButton);

    gbc.gridy = y++;
    gbc.ipady = 0; // Reset internal padding
    rightPanel.add(buttonContainer, gbc);

    add(rightPanel, BorderLayout.CENTER); 

    // Aksi Tombol (Logic tidak diubah)
    registerButton.addActionListener(e -> registerUser());
    
    // Action listener untuk link teks "Login di sini"
    loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            new LoginView().setVisible(true);
            dispose();
        }
    });

    backButton.addActionListener(e -> {
        new LoginView().setVisible(true);
        dispose();
    });
}

// Tambahkan atau ubah helper methods ini jika belum ada:
private void styleTextField(JTextField field) {
    field.setPreferredSize(new Dimension(25, 40));
    field.setBackground(Color.WHITE);
    field.setFont(new Font("Fredoka", Font.PLAIN, 14));
    field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    field.setForeground(Color.GRAY);
}

private void styleField(JComboBox<String> box) {
    box.setPreferredSize(new Dimension(25, 40));
    box.setBackground(Color.WHITE);
    box.setFont(new Font("Fredoka", Font.PLAIN, 14));
    box.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
}
    // ==============================
    // LOGIC PENDAFTARAN (DIPERBAIKI)
    // ==============================
    private void registerUser() {
        String role = roleBox.getSelectedItem().toString();
        String nama = namaField.getText();
        String noHp = noHpField.getText();
        String user = usernameField.getText();
        String pass = String.valueOf(passwordField.getPassword());

        if (nama.isEmpty() || noHp.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi semua data!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (SignInController.getService().isUsernameTaken(user, role)) {
            JOptionPane.showMessageDialog(this, "Username sudah dipakai!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Buat user baru berdasarkan role
        if (role.equals("Admin")) {
            SignInController.getService().registerAdmin(role, nama, user, pass, noHp);
        } else {
            SignInController.getService().registerPenyetor(role, nama, user, pass, noHp);
        }

        JOptionPane.showMessageDialog(this, "Akun berhasil dibuat!");
        new LoginView().setVisible(true);
        dispose();
    }
}