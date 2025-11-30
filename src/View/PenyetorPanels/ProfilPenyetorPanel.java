package View.PenyetorPanels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import Database.DatabasePenyetor;
import Model.Penyetor;

public class ProfilPenyetorPanel extends JPanel {
    private Penyetor user;

    private JTextField tfIdPenyetor;
    private JTextField tfIdBank;
    private JTextField tfNama;
    private JTextField tfUsername;
    private JTextField tfNoHp;
    private JButton btnSaveProfile;

    private JPasswordField pfOldPass;
    private JPasswordField pfNewPass;
    private JPasswordField pfConfirmPass;
    private JButton btnChangePass;

    private final Color GREEN_PRIMARY = new Color(40, 167, 69);
    private final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 12);
    private final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);

    public ProfilPenyetorPanel(Penyetor user) {
        this.user = user;
        initLayout();
        loadData();
    }

    private void initLayout() {
        setLayout(new GridLayout(1, 2, 20, 0)); 
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(createProfileSection());
        add(createPasswordSection());
    }

    private JPanel createProfileSection() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
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

        panel.add(createLabel("ID Penyetor (Tidak dapat diubah):"), gbc);
        gbc.gridy++;
        tfIdPenyetor = createTextField(false); 
        panel.add(tfIdPenyetor, gbc);

        gbc.gridy++;
        panel.add(createLabel("ID Bank Sampah (Tidak dapat diubah):"), gbc);
        gbc.gridy++;
        tfIdBank = createTextField(false);
        panel.add(tfIdBank, gbc);

        gbc.gridy++;
        panel.add(createLabel("Nama Lengkap:"), gbc);
        gbc.gridy++;
        tfNama = createTextField(true);
        panel.add(tfNama, gbc);

        gbc.gridy++;
        panel.add(createLabel("Username:"), gbc);
        gbc.gridy++;
        tfUsername = createTextField(true);
        panel.add(tfUsername, gbc);

        gbc.gridy++;
        panel.add(createLabel("Nomor HP:"), gbc);
        gbc.gridy++;
        tfNoHp = createTextField(true);
        panel.add(tfNoHp, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        btnSaveProfile = new JButton("Simpan Perubahan Profil");
        styleButton(btnSaveProfile);
        btnSaveProfile.addActionListener(e -> handleSaveProfile());
        panel.add(btnSaveProfile, gbc);

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
        border.setTitleColor(new Color(220, 53, 69)); 
        panel.setBorder(BorderFactory.createCompoundBorder(border, new EmptyBorder(10, 15, 10, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 0;

        panel.add(createLabel("Password Lama:"), gbc);
        gbc.gridy++;
        pfOldPass = new JPasswordField();
        styleTextField(pfOldPass);
        panel.add(pfOldPass, gbc);

        gbc.gridy++;
        panel.add(createLabel("Password Baru:"), gbc);
        gbc.gridy++;
        pfNewPass = new JPasswordField();
        styleTextField(pfNewPass);
        panel.add(pfNewPass, gbc);

        gbc.gridy++;
        panel.add(createLabel("Konfirmasi Password Baru:"), gbc);
        gbc.gridy++;
        pfConfirmPass = new JPasswordField();
        styleTextField(pfConfirmPass);
        panel.add(pfConfirmPass, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        btnChangePass = new JButton("Update Password");
        styleButton(btnChangePass);
        btnChangePass.setBackground(new Color(220, 53, 69)); 
        btnChangePass.addActionListener(e -> handleChangePassword());
        panel.add(btnChangePass, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }

    private void loadData() {
        if (user != null) {
            tfIdPenyetor.setText(user.getIdPenyetor());
            
            String bankId = user.getIdBankSampah();
            tfIdBank.setText((bankId == null || bankId.equals("null")) ? "Belum Terhubung" : bankId);
            
            tfNama.setText(user.getNamaLengkap());
            tfUsername.setText(user.getUsername());
            tfNoHp.setText(user.getNoHp());
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

        this.user.setNamaLengkap(nama);
        this.user.setUsername(user);
        this.user.setNoHp(hp);

        String lokal = "src\\Database\\Penyetor\\penyetor_" + this.user.getIdBankSampah() + ".txt";
        
        DatabasePenyetor.updatePenyetor(this.user, lokal);
        DatabasePenyetor.updatePenyetorGlobal(this.user);

        JOptionPane.showMessageDialog(this, "Profil berhasil diperbarui!");
    }

    private void handleChangePassword() {
        String oldPass = new String(pfOldPass.getPassword());
        String newPass = new String(pfNewPass.getPassword());
        String confirmPass = new String(pfConfirmPass.getPassword());

        if (oldPass.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password tidak boleh kosong!");
            return;
        }

        if (!oldPass.equals(user.getPassword())) {
            JOptionPane.showMessageDialog(this, "Password Lama Salah!", "Akses Ditolak", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Konfirmasi password baru tidak cocok!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        user.setPassword(newPass);

        String lokal = "src\\Database\\Penyetor\\penyetor_" + this.user.getIdBankSampah() + ".txt";
        
        DatabasePenyetor.updatePenyetor(this.user, lokal);
        DatabasePenyetor.updatePenyetorGlobal(this.user);

        JOptionPane.showMessageDialog(this, "Password berhasil diubah!");
        
        pfOldPass.setText("");
        pfNewPass.setText("");
        pfConfirmPass.setText("");
    }

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
            tf.setBackground(new Color(240, 240, 240)); 
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