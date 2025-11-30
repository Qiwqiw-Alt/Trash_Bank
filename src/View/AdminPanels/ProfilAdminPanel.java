package View.AdminPanels;

import Database.DataBaseAdmin;
import Model.Admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ProfilAdminPanel extends JPanel {

    private Admin currentAdmin;

    // --- Komponen Bagian Kiri (Profil) ---
    private JTextField tfIdAdmin;
    private JTextField tfIdBank;
    private JTextField tfNama;
    private JTextField tfUsername;
    private JTextField tfNoHp;
    private JButton btnSaveProfile;

    // --- Komponen Bagian Kanan (Password) ---
    private JPasswordField pfOldPass;
    private JPasswordField pfNewPass;
    private JPasswordField pfConfirmPass;
    private JButton btnChangePass;

    // --- Styling ---
    private final Color GREEN_PRIMARY = new Color(40, 167, 69);
    private final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 12);
    private final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);

    public ProfilAdminPanel(Admin admin) {
        this.currentAdmin = admin;
        initLayout();
        loadData();
    }

    private void initLayout() {
        setLayout(new GridLayout(1, 2, 20, 0)); // Grid 2 Kolom (Kiri & Kanan)
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(createProfileSection());
        add(createPasswordSection());
    }


    private JPanel createProfileSection() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        // Border dengan Judul
        TitledBorder border = BorderFactory.createTitledBorder(
            new LineBorder(Color.LIGHT_GRAY), " Informasi Pribadi ");
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 16));
        border.setTitleColor(GREEN_PRIMARY);
        panel.setBorder(BorderFactory.createCompoundBorder(border, new EmptyBorder(10, 15, 10, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 0;

        // 1. ID Admin (Read Only)
        panel.add(createLabel("ID Admin (Tidak dapat diubah):"), gbc);
        gbc.gridy++;
        tfIdAdmin = createTextField(false); // False = Read Only
        panel.add(tfIdAdmin, gbc);

        // 2. ID Bank (Read Only)
        gbc.gridy++;
        panel.add(createLabel("ID Bank Sampah (Tidak dapat diubah):"), gbc);
        gbc.gridy++;
        tfIdBank = createTextField(false);
        panel.add(tfIdBank, gbc);

        // 3. Nama Lengkap
        gbc.gridy++;
        panel.add(createLabel("Nama Lengkap:"), gbc);
        gbc.gridy++;
        tfNama = createTextField(true);
        panel.add(tfNama, gbc);

        // 4. Username
        gbc.gridy++;
        panel.add(createLabel("Username:"), gbc);
        gbc.gridy++;
        tfUsername = createTextField(true);
        panel.add(tfUsername, gbc);

        // 5. No HP
        gbc.gridy++;
        panel.add(createLabel("Nomor HP:"), gbc);
        gbc.gridy++;
        tfNoHp = createTextField(true);
        panel.add(tfNoHp, gbc);

        // Tombol Simpan Profil
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        btnSaveProfile = new JButton("💾 Simpan Perubahan Profil");
        styleButton(btnSaveProfile);
        btnSaveProfile.addActionListener(e -> handleSaveProfile());
        panel.add(btnSaveProfile, gbc);

        // Spacer ke bawah
        gbc.gridy++;
        gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }


    private JPanel createPasswordSection() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        TitledBorder border = BorderFactory.createTitledBorder(
            new LineBorder(Color.LIGHT_GRAY), " Keamanan (Ganti Password) ");
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 16));
        border.setTitleColor(new Color(220, 53, 69)); // Merah dikit biar warning
        panel.setBorder(BorderFactory.createCompoundBorder(border, new EmptyBorder(10, 15, 10, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 0;

        // 1. Password Lama
        panel.add(createLabel("Password Lama:"), gbc);
        gbc.gridy++;
        pfOldPass = new JPasswordField();
        styleTextField(pfOldPass);
        panel.add(pfOldPass, gbc);

        // 2. Password Baru
        gbc.gridy++;
        panel.add(createLabel("Password Baru:"), gbc);
        gbc.gridy++;
        pfNewPass = new JPasswordField();
        styleTextField(pfNewPass);
        panel.add(pfNewPass, gbc);

        // 3. Konfirmasi Password
        gbc.gridy++;
        panel.add(createLabel("Konfirmasi Password Baru:"), gbc);
        gbc.gridy++;
        pfConfirmPass = new JPasswordField();
        styleTextField(pfConfirmPass);
        panel.add(pfConfirmPass, gbc);

        // Tombol Ganti Password
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        btnChangePass = new JButton("🔒 Update Password");
        styleButton(btnChangePass);
        btnChangePass.setBackground(new Color(220, 53, 69)); // Merah
        btnChangePass.addActionListener(e -> handleChangePassword());
        panel.add(btnChangePass, gbc);

        // Spacer
        gbc.gridy++;
        gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }

    // ========================================================================
    // LOGIC & DATA HANDLING
    // ========================================================================

    private void loadData() {
        if (currentAdmin != null) {
            tfIdAdmin.setText(currentAdmin.getIdAdmin());
            
            // Handle kemungkinan Bank ID null (misal baru create admin)
            String bankId = currentAdmin.getIdBankSampah();
            tfIdBank.setText((bankId == null || bankId.equals("null")) ? "Belum Terhubung" : bankId);
            
            tfNama.setText(currentAdmin.getNamaAdmin());
            tfUsername.setText(currentAdmin.getUsername());
            tfNoHp.setText(currentAdmin.getNohp());
        }
    }

    private void handleSaveProfile() {
        String nama = tfNama.getText().trim();
        String user = tfUsername.getText().trim();
        String hp = tfNoHp.getText().trim();

        if (nama.isEmpty() || user.isEmpty() || hp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom profil harus diisi!");
            return;
        }

        currentAdmin.setNamaAdmin(nama);
        currentAdmin.setUsername(user);
        currentAdmin.setNohp(hp);

        String path = "src\\Database\\Admin\\admin_" + currentAdmin.getIdBankSampah() + ".txt";

        DataBaseAdmin.updateAdmin(currentAdmin, path);

        JOptionPane.showMessageDialog(this, "Profil berhasil diperbarui!");
    }

    private void handleChangePassword() {
        String oldPass = new String(pfOldPass.getPassword());
        String newPass = new String(pfNewPass.getPassword());
        String confirmPass = new String(pfConfirmPass.getPassword());

        // 1. Validasi Input Kosong
        if (oldPass.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password tidak boleh kosong!");
            return;
        }

        // 2. Cek Password Lama Benar/Salah
        if (!oldPass.equals(currentAdmin.getPassword())) {
            JOptionPane.showMessageDialog(this, "Password Lama Salah!", "Akses Ditolak", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Cek Konfirmasi Password
        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Konfirmasi password baru tidak cocok!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Update Object
        currentAdmin.setPassword(newPass);

        // 5. Simpan ke File
        String path = "src\\Database\\Admin\\admin_" + currentAdmin.getIdBankSampah() + ".txt";
        DataBaseAdmin.updateAdmin(currentAdmin, path);

        JOptionPane.showMessageDialog(this, "Password berhasil diubah!");
        
        // Bersihkan field password
        pfOldPass.setText("");
        pfNewPass.setText("");
        pfConfirmPass.setText("");
    }

    // --- Helpers Styling ---
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(Color.DARK_GRAY);
        return lbl;
    }

    private JTextField createTextField(boolean isEditable) {
        JTextField tf = new JTextField();
        styleTextField(tf);
        tf.setEditable(isEditable);
        if (!isEditable) {
            tf.setBackground(new Color(240, 240, 240)); // Abu-abu kalau read-only
            tf.setForeground(Color.GRAY);
        }
        return tf;
    }

    private void styleTextField(JTextField tf) {
        tf.setFont(FONT_INPUT);
        tf.setPreferredSize(new Dimension(200, 35));
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleButton(JButton btn) {
        btn.setBackground(GREEN_PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}