package View;

import javax.swing.*;

import Controller.SignInController;
import Database.DataBaseAdmin;
import Database.DatabasePenyetor;
import Model.Admin;
import Model.Penyetor;
import Model.User;
import Database.DataBaseAdmin.*;

import java.awt.*;

public class SignInView extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField namaField;
//    private JTextField idField;
    private JTextField noHpField;
    private JComboBox<String> roleBox;
    private ImageIcon image = new ImageIcon("recycle-bin.png");
    private JButton registerButton, backButton;

    public SignInView() {
        setTitle("Sign Up - Bank Sampah");
        setSize(800, 700); // Diperbesar untuk menampung field tambahan
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(image.getImage());

        initComponents();
    }

    private void initComponents() {

        JPanel mainPanel = new JPanel(null);

        // ==============================
        // PANEL KIRI (HIJAU)
        // ==============================
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBackground(new Color(0x59AC77));
        leftPanel.setBounds(0, 0, 350, 700);

        JLabel titleLeft = new JLabel("Create Account", SwingConstants.CENTER);
        titleLeft.setFont(new Font("Fredoka", Font.BOLD, 26));
        titleLeft.setForeground(Color.WHITE);
        titleLeft.setBounds(0, 200, 350, 40);
        leftPanel.add(titleLeft);

        JLabel desc1 = new JLabel("Daftarkan akun baru", SwingConstants.CENTER);
        desc1.setFont(new Font("Fredoka", Font.PLAIN, 16));
        desc1.setForeground(Color.WHITE);
        desc1.setBounds(0, 250, 350, 30);
        leftPanel.add(desc1);

        JLabel desc2 = new JLabel("untuk mengakses aplikasi", SwingConstants.CENTER);
        desc2.setFont(new Font("Fredoka", Font.PLAIN, 16));
        desc2.setForeground(Color.WHITE);
        desc2.setBounds(0, 280, 350, 30);
        leftPanel.add(desc2);

        // ==============================
        // PANEL KANAN (FORM)
        // ==============================
        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBounds(350, 0, 450, 700);

        JLabel titleRight = new JLabel("Buat Akun Baru");
        titleRight.setFont(new Font("Fredoka", Font.BOLD, 26));
        titleRight.setBounds(110, 65, 300, 40);
        rightPanel.add(titleRight);

        // LABEL & INPUT
        JLabel roleLabel = new JLabel("Daftar Sebagai:");
        roleLabel.setBounds(60, 135, 200, 25);
        roleLabel.setFont(new Font("Fredoka", Font.PLAIN, 16));
        rightPanel.add(roleLabel);

        roleBox = new JComboBox<>(new String[]{"Admin", "Penyetor"});
        roleBox.setBounds(60, 170, 330, 35);
        roleBox.setFont(new Font("Fredoka", Font.PLAIN, 14));
        rightPanel.add(roleBox);

        JLabel namaLabel = new JLabel("Nama Lengkap:");
        namaLabel.setBounds(60, 215, 200, 25);
        namaLabel.setFont(new Font("Fredoka", Font.PLAIN, 16));
        rightPanel.add(namaLabel);

        namaField = new JTextField();
        namaField.setBounds(60, 240, 330, 35);
        rightPanel.add(namaField);

        JLabel noHpLabel = new JLabel("No. HP:");
        noHpLabel.setBounds(60, 280, 200, 25);
        noHpLabel.setFont(new Font("Fredoka", Font.PLAIN, 16));
        rightPanel.add(noHpLabel);

        noHpField = new JTextField();
        noHpField.setBounds(60, 310, 330, 35);
        rightPanel.add(noHpField);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(60, 360, 200, 25);
        userLabel.setFont(new Font("Fredoka", Font.PLAIN, 16));
        rightPanel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(60, 390, 330, 35);
        rightPanel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(60, 430, 200, 25);   // dari 410 → 430
        passLabel.setFont(new Font("Fredoka", Font.PLAIN, 16));
        rightPanel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(60, 460, 330, 35);   // dari 440 → 460
        rightPanel.add(passwordField);

        // BUTTON
        registerButton = new JButton("Register");
        registerButton.setBounds(60, 540, 145, 40);
        registerButton.setBackground(new Color(0x59AC77));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Fredoka", Font.BOLD, 15));
        registerButton.setFocusPainted(false);
        rightPanel.add(registerButton);

        backButton = new JButton("Back");
        backButton.setBounds(245, 540, 145, 40);
        backButton.setBackground(new Color(0x59AC77));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Fredoka", Font.BOLD, 15));
        backButton.setFocusPainted(false);
        rightPanel.add(backButton);

        // ADD PANEL
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        add(mainPanel);

        // Aksi Tombol
        registerButton.addActionListener(e -> registerUser());
        backButton.addActionListener(e -> {
            new LoginView().setVisible(true);
            dispose();
        });
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