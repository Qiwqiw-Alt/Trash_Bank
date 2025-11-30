package View.AdminPanels;

import Database.DatabaseBankSampah; 
import Model.BankSampah;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class EditBankSampahProfile extends JPanel {

    private BankSampah currentBank;

    private JTextField tfIdBank;
    private JTextField tfNamaBank;
    private JTextArea taAlamat; 
    private JButton btnSave;

    private final Color GREEN_PRIMARY = new Color(40, 167, 69);
    private final Font FONT_LABEL = new Font("Fredoka", Font.BOLD, 12);
    private final Font FONT_INPUT = new Font("Fredoka", Font.PLAIN, 14);

    public EditBankSampahProfile(BankSampah bankSampah) {
        this.currentBank = bankSampah;
        initLayout();
        loadData();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245)); 
        setBorder(new EmptyBorder(30, 30, 30, 30)); 

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        
        TitledBorder border = BorderFactory.createTitledBorder(
            new LineBorder(Color.LIGHT_GRAY), " Informasi Bank Sampah ");
        border.setTitleFont(new Font("Fredoka", Font.BOLD, 16));
        border.setTitleColor(GREEN_PRIMARY);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            border, new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 0;

        contentPanel.add(createLabel("ID Bank (Permanen):"), gbc);
        gbc.gridy++;
        tfIdBank = new JTextField();
        styleTextField(tfIdBank);
        tfIdBank.setEditable(false);
        tfIdBank.setBackground(new Color(240, 240, 240)); 
        contentPanel.add(tfIdBank, gbc);

        gbc.gridy++;
        contentPanel.add(createLabel("Nama Bank Sampah:"), gbc);
        gbc.gridy++;
        tfNamaBank = new JTextField();
        styleTextField(tfNamaBank);
        contentPanel.add(tfNamaBank, gbc);

        gbc.gridy++;
        contentPanel.add(createLabel("Alamat Lengkap:"), gbc);
        gbc.gridy++;
        taAlamat = new JTextArea(4, 20); 
        taAlamat.setFont(FONT_INPUT);
        taAlamat.setLineWrap(true);
        taAlamat.setWrapStyleWord(true);
        
        JScrollPane scrollAlamat = new JScrollPane(taAlamat);
        scrollAlamat.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(5, 5, 5, 5)
        ));
        contentPanel.add(scrollAlamat, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(30, 0, 0, 0); 
        btnSave = new JButton("💾 Simpan Perubahan");
        styleButton(btnSave);
        btnSave.addActionListener(e -> handleSave());
        contentPanel.add(btnSave, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        contentPanel.add(Box.createVerticalGlue(), gbc);

        add(contentPanel, BorderLayout.NORTH);
    }

    private void loadData() {
        if (currentBank != null) {
            tfIdBank.setText(currentBank.getIdBank());
            tfNamaBank.setText(currentBank.getNamaBank());
            taAlamat.setText(currentBank.getAlamat());
        }
    }

    private void handleSave() {
        String nama = tfNamaBank.getText().trim();
        String alamat = taAlamat.getText().trim();

        if (nama.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Bank dan Alamat tidak boleh kosong!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Simpan perubahan profil Bank Sampah?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            currentBank.setNamaBank(nama);
            currentBank.setAlamat(alamat);

            DatabaseBankSampah.updateBankSampah(currentBank);

            JOptionPane.showMessageDialog(this, "Data Bank Sampah berhasil diperbarui!");
        }
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(Color.DARK_GRAY);
        return lbl;
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
        btn.setFont(new Font("Fredoka", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}