package View.AdminPanels;

import Model.Admin;
import Model.BankSampah; // Sesuaikan import ini jika perlu

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Controller.AdminHomePanelController;
import Database.DatabaseBankSampah;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminHomePanel extends JPanel {

    private Admin currentUser;
    private BankSampah curreBankSampah;
    // Jika kamu punya object BankSampah di sini, bisa ditambahkan fieldnya
    // private BankSampah currentBank;

    // --- Komponen UI yang butuh di-update datanya ---
    private JLabel lblBankName;
    private JLabel lblAdminName;
    
    private JLabel lblTotalMemberVal;
    private JLabel lblTotalTrxVal;
    private JLabel lblTotalSampahVal; // Total Kg semua sampah
    
    private JPanel panelCategoryList; // Tempat menaruh list kategori

    // --- Styling ---
    private final Color GREEN_PRIMARY = new Color(40, 167, 69);
    private final Color GREEN_SOFT = new Color(209, 231, 221);
    private final Color WHITE = Color.WHITE;
    private final Font FONT_BIG = new Font("Segoe UI", Font.BOLD, 24);
    private final Font FONT_MED = new Font("Segoe UI", Font.BOLD, 16);
    private final Font FONT_NUM = new Font("Segoe UI", Font.BOLD, 28);

    public AdminHomePanel(Admin admin, BankSampah bankSampah) {
        this.currentUser = admin;
        this.curreBankSampah = bankSampah;
        
        initLayout();
        loadData(); // Panggil fungsi untuk mengisi data dummy/database
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245)); // Background abu muda
        
        // Bungkus dengan ScrollPane agar aman jika konten panjang
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(245, 245, 245));
        mainContent.setBorder(new EmptyBorder(20, 30, 20, 30));

        // 1. HEADER SECTION
        mainContent.add(createHeaderSection());
        mainContent.add(Box.createVerticalStrut(25)); // Jarak

        // 2. CARDS SECTION (Ringkasan)
        mainContent.add(createSummaryCards());
        mainContent.add(Box.createVerticalStrut(25));

        // 3. CATEGORY SECTION (Detail Sampah)
        mainContent.add(createCategorySection());
        mainContent.add(Box.createVerticalStrut(20));

        // 4. REFRESH BUTTON (Opsional)
        JButton btnRefresh = new JButton("🔄 Refresh Data");
        btnRefresh.setBackground(Color.DARK_GRAY);
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRefresh.addActionListener(e -> loadData()); // Action Listener Refresh
        mainContent.add(btnRefresh);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ========================================================================
    // UI COMPONENTS CREATION
    // ========================================================================

    private JPanel createHeaderSection() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBackground(new Color(245, 245, 245));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 80));

        // Baris 1: Sapaan Admin
        lblAdminName = new JLabel("Halo, Admin " + currentUser.getNamaAdmin() + "!");
        lblAdminName.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblAdminName.setForeground(Color.GRAY);

        // Baris 2: Nama Bank Sampah
        lblBankName = new JLabel("Dashboard Bank Sampah"); // Nanti di-update loadData
        lblBankName.setFont(FONT_BIG);
        lblBankName.setForeground(new Color(33, 37, 41));

        panel.add(lblAdminName);
        panel.add(lblBankName);

        return panel;
    }

    private JPanel createSummaryCards() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0)); // 1 Baris, 3 Kolom, Jarak 20px
        panel.setBackground(new Color(245, 245, 245));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 120)); // Tinggi fix

        // Card 1: Member
        lblTotalMemberVal = new JLabel("0");
        panel.add(createSingleCard("Total Nasabah", lblTotalMemberVal, new Color(13, 110, 253))); // Biru

        // Card 2: Transaksi
        lblTotalTrxVal = new JLabel("0");
        panel.add(createSingleCard("Total Transaksi", lblTotalTrxVal, new Color(255, 193, 7))); // Kuning

        // Card 3: Total Sampah
        lblTotalSampahVal = new JLabel("0 Kg");
        panel.add(createSingleCard("Total Sampah Terkumpul", lblTotalSampahVal, GREEN_PRIMARY)); // Hijau

        return panel;
    }

    private JPanel createSingleCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true), // Border luar halus
            new EmptyBorder(15, 20, 15, 20) // Padding dalam
        ));

        // Garis warna di kiri sebagai aksen
        JPanel accent = new JPanel();
        accent.setBackground(accentColor);
        accent.setPreferredSize(new Dimension(5, 0));
        card.add(accent, BorderLayout.WEST);

        // Konten text
        JPanel content = new JPanel(new GridLayout(2, 1));
        content.setBackground(WHITE);
        content.setBorder(new EmptyBorder(0, 15, 0, 0)); // Jarak dari garis aksen

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(Color.GRAY);

        valueLabel.setFont(FONT_NUM);
        valueLabel.setForeground(new Color(33, 37, 41));

        content.add(lblTitle);
        content.add(valueLabel);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JPanel createCategorySection() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(WHITE);
        container.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Judul Section
        JLabel lblTitle = new JLabel("📦 Stok Sampah per Kategori");
        lblTitle.setFont(FONT_MED);
        lblTitle.setForeground(GREEN_PRIMARY);
        container.add(lblTitle, BorderLayout.NORTH);

        // Panel List (Nanti diisi looping data)
        panelCategoryList = new JPanel();
        panelCategoryList.setLayout(new BoxLayout(panelCategoryList, BoxLayout.Y_AXIS));
        panelCategoryList.setBackground(WHITE);
        panelCategoryList.setBorder(new EmptyBorder(15, 0, 0, 0));

        container.add(panelCategoryList, BorderLayout.CENTER);
        
        return container;
    }

    // Helper untuk membuat baris kategori
    private JPanel createCategoryRow(String name, double totalKg) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(WHITE);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        row.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel lblName = new JLabel("• " + name);
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblVal = new JLabel(String.format("%.1f Kg", totalKg)); // Format 1 desimal
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblVal.setForeground(Color.DARK_GRAY);

        row.add(lblName, BorderLayout.WEST);
        row.add(lblVal, BorderLayout.EAST);
        
        // Garis pemisah tipis di bawah
        row.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH);

        return row;
    }

    // ========================================================================
    // 🛠️ BAGIAN LOGIKA DATABASE (PLACEHOLDER)
    // Silakan isi bagian ini dengan logika DB kamu sendiri
    // ========================================================================

    // ... kode atas sama ...

    private void loadData() {
        // Pastikan Controller dan Service tidak null
        if (curreBankSampah == null) return;

        // 1. LOAD DATA BANK INFO
        String namaBank = AdminHomePanelController.getService().getBankName(currentUser);
        lblBankName.setText(namaBank.isEmpty() ? "Bank Sampah" : namaBank);

        // 2. LOAD TOTAL MEMBER
        int totalMember = AdminHomePanelController.getService().getTotalMember(curreBankSampah);
        lblTotalMemberVal.setText(String.valueOf(totalMember));

        // 3. LOAD TOTAL TRANSAKSI
        int totalTrx = AdminHomePanelController.getService().getTotalTransaksi(curreBankSampah);
        lblTotalTrxVal.setText(String.valueOf(totalTrx));

        // 4. LOAD TOTAL BERAT SAMPAH (Tambahkan " Kg" biar jelas)
        double totalBerat = AdminHomePanelController.getService().getTotalBeratSampah(curreBankSampah);
        lblTotalSampahVal.setText(String.format("%.1f Kg", totalBerat)); // Format 1 desimal

        // 5. LOAD DETAIL PER KATEGORI
        Map<String, Double> dataKategori = AdminHomePanelController.getService().beratPerKategori(curreBankSampah);
        
        panelCategoryList.removeAll(); // Bersihkan list lama

        if (dataKategori.isEmpty()) {
            // Tampilkan pesan jika belum ada data
            JLabel emptyLabel = new JLabel("Belum ada data sampah masuk.");
            emptyLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            panelCategoryList.add(emptyLabel);
        } else {
            for (Map.Entry<String, Double> entry : dataKategori.entrySet()) {
                panelCategoryList.add(createCategoryRow(entry.getKey(), entry.getValue()));
            }
        }

        // Refresh UI
        panelCategoryList.revalidate();
        panelCategoryList.repaint();
    }
}