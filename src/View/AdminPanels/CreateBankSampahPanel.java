package View.AdminPanels;

import Model.Admin;
import Model.BankSampah;
import View.DashboardAdminView; // Import view utama untuk callback
import View.LoginView; // Import untuk akses list bank sampah global (simulasi database)

import javax.swing.*;

import Database.DataBaseAdmin;
import Database.DatabaseBankSampah;

import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.UUID;

public class CreateBankSampahPanel extends JPanel {

    // Simpan referensi ke Frame utama agar bisa memanggil method refresh
    private DashboardAdminView parentFrame;
    private Admin currentAdmin;

    // Komponen Form
    private JTextField tfNamaBank;
    private JTextField tfAlamat;
    private JButton btnSimpan;

    // Warna (Konsisten dengan Dashboard)
    private final Color GREEN_PRIMARY = new Color(0, 128, 0);

    /**
     * Constructor
     * @param parentFrame : Referensi ke DashboardAdminView (untuk memanggil onBankCreatedSuccess)
     * @param admin : Admin yang sedang login (untuk disambungkan dengan bank baru)
     */
    public CreateBankSampahPanel(DashboardAdminView parentFrame, Admin admin) {
        this.parentFrame = parentFrame;
        this.currentAdmin = admin;

        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout()); // Gunakan GridBagLayout agar form rapi di tengah
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margin antar komponen
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Judul
        JLabel lblTitle = new JLabel("SETUP BANK SAMPAH ANDA");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(GREEN_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; // Span 2 kolom
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitle, gbc);

        // Deskripsi Kecil
        JLabel lblDesc = new JLabel("Halo " + currentAdmin.getNamaAdmin() + ", silakan buat Bank Sampah baru untuk memulai.");
        gbc.gridy = 1;
        add(lblDesc, gbc);

        // 2. Input Nama Bank
        gbc.gridy = 2; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Nama Bank Sampah:"), gbc);

        tfNamaBank = new JTextField(30);
        gbc.gridx = 1; 
        add(tfNamaBank, gbc);

        // 3. Input Alamat
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Alamat Lengkap:"), gbc);

        tfAlamat = new JTextField(30);
        gbc.gridx = 1;
        add(tfAlamat, gbc);

        // 4. Tombol Simpan
        btnSimpan = new JButton("Buat & Simpan Bank Sampah");
        btnSimpan.setBackground(GREEN_PRIMARY);
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFont(new Font("Arial", Font.BOLD, 14));
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        gbc.ipady = 10; // Tinggi tombol
        add(btnSimpan, gbc);

        // --- ACTION LISTENER (LOGIKA UTAMA) ---
        btnSimpan.addActionListener(e -> handleSimpan());
    }

    private void handleSimpan() {
        String nama = tfNamaBank.getText().trim();
        String alamat = tfAlamat.getText().trim();

        // Validasi Input
        if (nama.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Bank dan Alamat tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Generate ID Bank Baru
        // Menggunakan UUID simple atau counter dari size list
        DatabaseBankSampah.loadData();
        String idBank = DatabaseBankSampah.generateBankId();
        BankSampah newBank = new BankSampah(idBank, nama, alamat);
        DatabaseBankSampah.loadData();
        DatabaseBankSampah.addBankSampah(newBank);

        // 4. Update Admin (Hubungkan Admin dengan Bank ini)
        ArrayList<Admin> listAdmin = DataBaseAdmin.loadData();
        for (Admin a : listAdmin) {
            if (a.getIdAdmin().equals(currentAdmin.getIdAdmin())) {
                a.setIdBankSampah(idBank); // Update data di list static
                break;
            }
        }
    DataBaseAdmin.writeData();
        // 5. Tampilkan Pesan Sukses
        JOptionPane.showMessageDialog(this, "Bank Sampah '" + nama + "' berhasil dibuat!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        // 6. PANGGIL METHOD DI PARENT FRAME UNTUK REFRESH DASHBOARD
        // Ini langkah krusial agar tampilan berubah dari form create ke dashboard menu
        if (parentFrame != null) {
            parentFrame.onBankCreatedSuccess(newBank);
        }
    }
}