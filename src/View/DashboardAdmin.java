package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import Model.Admin;
import Model.Penyetor;
import Model.BankSampah;
import Model.Sampah;
import Model.Reward;
import Model.Complain;
import Model.Complain.Status;
import Model.ItemTransaksi;
import Model.Transaksi;
import Model.Poin;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID; 
import java.util.ArrayList;

public class DashboardAdmin extends JFrame {
    
    private Admin currentUser;
    private BankSampah bankSampah; 
    private JPanel contentPanel;
    
    // Icon & Style Constants
    private final Color GREEN_PRIMARY = new Color(0, 128, 0); 
    private final Color GREEN_LIGHT = new Color(200, 240, 200); 
    private final Color TEXT_DARK = new Color(50, 50, 50);

    public DashboardAdmin(Admin user, BankSampah bankSampah) {
        this.currentUser = user;
        this.bankSampah = bankSampah; 
        String namaBank = bankSampah.getNamaBank() != null ? bankSampah.getNamaBank() : "Bank Sampah App";
        setTitle("Dashboard Admin - " + namaBank); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true); 
        
        initDashboard();

        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        showPanel(createHomeAdminPanel());
    }

    private void initDashboard() {
        // ... (kode initDashboard sama seperti sebelumnya) ...
        setLayout(new BorderLayout());
        
        // ===== LEFT MENU PANEL (SIDEBAR) =====
        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(250, 0)); 
        menuPanel.setBackground(GREEN_PRIMARY);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        // Header
        menuPanel.add(createLogoPanel());
        menuPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10))); 

        // Menu Umum
        JPanel lblHome = createMenuLabel("Home", "Home", "icon_home.png");
        JPanel lblProfil = createMenuLabel("Edit Profil", "Profil", "icon_profile.png"); 
        menuPanel.add(lblHome);
        menuPanel.add(lblProfil);
        
        // Menu Admin
        menuPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel lblAdminTitle = createRoleTitle("ADMIN MENU");
        menuPanel.add(lblAdminTitle);
        
        JPanel lblListMember = createMenuLabel("Lihat Penyetor", "ListMember", "icon_members.png");
        JPanel lblGivePoin = createMenuLabel("Berikan Poin Penyetor", "GivePoin", "icon_give_poin.png");
        JPanel lblAddSampah = createMenuLabel("Tambah Jenis Sampah", "AddSampah", "icon_add_trash.png");
        JPanel lblAddReward = createMenuLabel("Tambah Reward", "AddReward", "icon_add_reward.png");
        JPanel lblKomplain = createMenuLabel("Evaluasi Komplain", "Komplain", "icon_complain.png");

        menuPanel.add(lblListMember);
        menuPanel.add(lblGivePoin);
        menuPanel.add(lblAddSampah);
        menuPanel.add(lblAddReward);
        menuPanel.add(lblKomplain);
        
        menuPanel.add(Box.createVerticalGlue()); 
        // Logout
        JPanel lblLogout = createMenuLabel("Logout", "Logout", "icon_logout.png");
        menuPanel.add(lblLogout);
        
        add(menuPanel, BorderLayout.WEST);

        // ===== TOP PANEL (HEADER) =====
        JPanel topPanel = createTopPanel(GREEN_LIGHT, TEXT_DARK);
        add(topPanel, BorderLayout.NORTH);

        // ===== MAIN CONTENT PANEL =====
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);
    }

    // --- FUNGSI UTILITY ---
    // ... (createMenuLabel, createLogoPanel, createRoleTitle, createTopPanel, showPanel, switchPanel, logoutAction, createStatCard sama seperti sebelumnya) ...
    private JPanel createMenuLabel(String text, String actionCommand, String iconFileName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setBackground(GREEN_PRIMARY); 

        JLabel label = new JLabel("  " + text, SwingConstants.LEFT);
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        label.setForeground(Color.WHITE);
        label.setOpaque(false); 

        try {
            ImageIcon originalIcon = new ImageIcon(iconFileName);
            Image image = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); 
            label.setIcon(new ImageIcon(image));
            label.setIconTextGap(10); 
            label.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 10)); 
        } catch (Exception e) {
            label.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 10)); 
        }

        panel.add(label, BorderLayout.WEST);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchPanel(actionCommand);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(0, 170, 0));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(GREEN_PRIMARY);
            }
        };

        panel.addMouseListener(mouseAdapter);
        label.addMouseListener(mouseAdapter); 

        panel.setName(actionCommand); 
        return panel;
    }
    
    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(GREEN_PRIMARY);
        logoPanel.setPreferredSize(new Dimension(250, 100));
        JLabel logo = new JLabel("BANK SAMPAH APP", SwingConstants.CENTER);
        logo.setFont(new Font("Arial", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        logoPanel.add(logo);
        return logoPanel;
    }
    
    private JLabel createRoleTitle(String text) {
        JLabel label = new JLabel("--- " + text + " ---", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        return label;
    }
    
    private JPanel createTopPanel(Color background, Color foreground) {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        topPanel.setBackground(background);
        JLabel lblUser = new JLabel("Logged in as: " + currentUser.getNamaAdmin() + " (Admin)");
        lblUser.setForeground(foreground);
        lblUser.setFont(new Font("Arial", Font.PLAIN, 14));
        topPanel.add(lblUser);
        return topPanel;
    }

    private void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void switchPanel(String actionCommand) {
        JPanel newPanel;
        
        switch (actionCommand) {
            case "Home":
                newPanel = createHomeAdminPanel();
                break;
            case "Profil":
                newPanel = createProfilAdminPanel();
                break;
            case "ListMember":
                newPanel = createListMemberPanel();
                break;
            case "GivePoin":
                newPanel = createGivePoinPanel();
                break;
            case "AddSampah":
                newPanel = createAddSampahPanel();
                break;
            case "AddReward":
                newPanel = createAddRewardPanel();
                break;
            case "Komplain":
                newPanel = createKomplainPanel();
                break;
            case "Logout":
                logoutAction();
                return;
            default:
                newPanel = new JPanel();
                break;
        }
        showPanel(newPanel);
    }
    
    private void logoutAction() {
        JOptionPane.showMessageDialog(this, "Anda berhasil logout.");
        new LoginView().setVisible(true); 
        dispose();
    }

    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createTitledBorder(title));
        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("Arial", Font.BOLD, 30));
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }
    
    // --- CONTENT PANEL METHODS (yang sudah ada) ---
    private JPanel createHomeAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("Selamat Datang di Dashboard Admin!", SwingConstants.CENTER), BorderLayout.NORTH);
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        long totalPenyetorCount = LoginView.users.stream().filter(u -> u instanceof Penyetor).count();
        statsPanel.add(createStatCard("Total Penyetor", String.valueOf(totalPenyetorCount)));
        statsPanel.add(createStatCard("Jenis Sampah Aktif", String.valueOf(LoginView.sampahs.size())));
        statsPanel.add(createStatCard("Total Transaksi", String.valueOf(LoginView.transactions.size())));
        
        panel.add(statsPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createProfilAdminPanel() {
        // ... (kode Edit Profil Admin sama seperti sebelumnya) ...
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("EDIT PROFIL ADMIN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Data Akun"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField namaField = new JTextField(currentUser.getNamaAdmin(), 20);
        JTextField usernameField = new JTextField(currentUser.getUsername(), 20);
        usernameField.setEditable(false); 
        JTextField noHpField = new JTextField(currentUser.getNohp() != null ? currentUser.getNohp() : "", 20);
        JPasswordField passwordField = new JPasswordField("", 20); // Kosongkan untuk input baru

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; formPanel.add(new JLabel("Nama Admin:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(namaField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Username (Tidak bisa diubah):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("No. HP:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(noHpField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Password Baru (kosongkan jika tidak diubah):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(passwordField, gbc);

        JButton simpanButton = new JButton("Simpan Perubahan");
        simpanButton.setBackground(GREEN_PRIMARY);
        simpanButton.setForeground(Color.WHITE);
        gbc.gridx = 1; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST; formPanel.add(simpanButton, gbc);

        // Aksi Simpan
        simpanButton.addActionListener(e -> {
            try {
                currentUser.setNamaAdmin(namaField.getText());
                currentUser.setNohp(noHpField.getText());
                String newPassword = String.valueOf(passwordField.getPassword());
                
                if (newPassword.length() > 0) {
                    currentUser.setPassword(newPassword);
                    JOptionPane.showMessageDialog(this, "Profil dan Password berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Profil berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                }
                showPanel(createProfilAdminPanel()); // Refresh
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan perubahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(formPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createListMemberPanel() {
        // ... (kode Lihat Penyetor sama seperti sebelumnya) ...
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(TEXT_DARK), 
            "DAFTAR LENGKAP PENYETOR TERDAFTAR", 
            TitledBorder.CENTER, TitledBorder.TOP, 
            new Font("Arial", Font.BOLD, 14), TEXT_DARK));
        
        String[] columnNames = {"ID Penyetor", "Nama Lengkap", "Username", "No. HP", "Total Poin", "Total Setoran (x)"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        List<Penyetor> daftarPenyetor = LoginView.users.stream()
            .filter(u -> u instanceof Penyetor)
            .map(u -> (Penyetor) u)
            .collect(Collectors.toList());

        for (Penyetor p : daftarPenyetor) {
            model.addRow(new Object[]{
                p.getIdPenyetor(), 
                p.getNamaLengkap(), 
                p.getUsername(), 
                p.getNoHp() != null ? p.getNoHp() : "-",
                p.getTotalPoin(),
                p.getTotalSetoran()
            });
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setBackground(GREEN_LIGHT);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        int totalPenyetor = model.getRowCount();
        JLabel lblCount = new JLabel("Total Penyetor Terdaftar: " + totalPenyetor, SwingConstants.RIGHT);
        lblCount.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 10));
        panel.add(lblCount, BorderLayout.SOUTH);

        return panel;
    }

    // --- IMPLEMENTASI MENU ADMIN BARU ---

    /**
     * Menu untuk mencatat setoran sampah dan memberikan poin kepada penyetor.
     */
    private JPanel createGivePoinPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("CATAT SETORAN SAMPAH DAN BERIKAN POIN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        JPanel mainFormPanel = new JPanel(new GridBagLayout());
        mainFormPanel.setBorder(BorderFactory.createTitledBorder("Input Data Setoran"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Pilih Penyetor
        List<Penyetor> daftarPenyetor = LoginView.users.stream()
            .filter(u -> u instanceof Penyetor)
            .map(u -> (Penyetor) u)
            .collect(Collectors.toList());
        JComboBox<Penyetor> penyetorBox = new JComboBox<>(daftarPenyetor.toArray(new Penyetor[0]));
        
        // 2. Data Sampah (Untuk dimasukkan ke dalam tabel)
        List<Sampah> daftarSampah = LoginView.sampahs;
        
        // Tabel Item Transaksi
        String[] itemColNames = {"Jenis Sampah", "Harga/Kg", "Berat (Kg)", "Subtotal"};
        DefaultTableModel itemModel = new DefaultTableModel(itemColNames, 0);
        JTable itemTable = new JTable(itemModel);
        JScrollPane itemScrollPane = new JScrollPane(itemTable);
        itemScrollPane.setPreferredSize(new Dimension(400, 150));
        
        // Komponen Input Item
        JComboBox<Sampah> sampahBox = new JComboBox<>(daftarSampah.toArray(new Sampah[0]));
        JTextField beratField = new JTextField(5);
        JButton tambahItemButton = new JButton("Tambah Item");
        
        // Panel Input Item
        JPanel inputItemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputItemPanel.add(new JLabel("Sampah:"));
        inputItemPanel.add(sampahBox);
        inputItemPanel.add(new JLabel("Berat (Kg):"));
        inputItemPanel.add(beratField);
        inputItemPanel.add(tambahItemButton);

        // Total Summary
        JLabel lblTotalHarga = new JLabel("Total Harga Setoran: Rp 0.0");
        JLabel lblTotalPoin = new JLabel("Total Poin Diberikan: 0 Poin");
        
        // Button Finalisasi
        JButton finalisasiButton = new JButton("Finalisasi & Berikan Poin");
        finalisasiButton.setBackground(GREEN_PRIMARY);
        finalisasiButton.setForeground(Color.WHITE);
        
        // Item List sementara untuk transaksi
        List<ItemTransaksi> tempItems = new ArrayList<>();
        
        // Aksi Tambah Item
        tambahItemButton.addActionListener(e -> {
            Sampah selectedSampah = (Sampah) sampahBox.getSelectedItem();
            double berat;
            try {
                berat = Double.parseDouble(beratField.getText());
                if (berat <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Berat harus berupa angka positif.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Buat ItemTransaksi (ID Transaksi masih dummy/null sementara)
            ItemTransaksi newItem = new ItemTransaksi(null, selectedSampah.getIdSampah(), selectedSampah.getHargaPerKg(), berat);
            tempItems.add(newItem);

            // Tambahkan ke Tabel
            itemModel.addRow(new Object[]{
                selectedSampah.getJenis(),
                "Rp " + selectedSampah.getHargaPerKg(),
                berat,
                "Rp " + newItem.getSubtotal()
            });
            
            // Hitung Ulang Total
            double totalHarga = tempItems.stream().mapToDouble(ItemTransaksi::getSubtotal).sum();
            int totalPoin = Poin.konversiKePoin(totalHarga);
            lblTotalHarga.setText("Total Harga Setoran: Rp " + String.format("%.2f", totalHarga));
            lblTotalPoin.setText("Total Poin Diberikan: " + totalPoin + " Poin");
            beratField.setText("");
        });

        // Aksi Finalisasi
        finalisasiButton.addActionListener(e -> {
            if (tempItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Silakan tambahkan item sampah terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Penyetor selectedPenyetor = (Penyetor) penyetorBox.getSelectedItem();
            if (selectedPenyetor == null) {
                JOptionPane.showMessageDialog(this, "Silakan pilih Penyetor.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 1. Buat Transaksi Baru
            String idTrx = "TRX" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
            Transaksi newTrx = new Transaksi(idTrx, selectedPenyetor.getIdPenyetor(), bankSampah.getIdBank());

            // 2. Pindahkan Item ke Transaksi, hitung total
            double finalHarga = 0;
            for (ItemTransaksi item : tempItems) {
                item.setIdTransaksi(idTrx); // Set ID Transaksi final
                newTrx.tambahItem(item); // Ini akan menghitung total harga & poin
                finalHarga += item.getSubtotal();
            }
            
            // 3. Update Poin Penyetor
            selectedPenyetor.tambahPoin(newTrx.getTotalPoin());
            selectedPenyetor.tambahSetoran(1); // Tambah 1x setoran
            
            // 4. Simpan Transaksi
            LoginView.transactions.add(newTrx);

            JOptionPane.showMessageDialog(this, 
                String.format("Transaksi berhasil dicatat! Penyetor %s mendapatkan %d Poin (Total: Rp %.2f).", 
                    selectedPenyetor.getNamaLengkap(), newTrx.getTotalPoin(), finalHarga), 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            // Reset Form
            tempItems.clear();
            itemModel.setRowCount(0);
            lblTotalHarga.setText("Total Harga Setoran: Rp 0.0");
            lblTotalPoin.setText("Total Poin Diberikan: 0 Poin");
            showPanel(createGivePoinPanel()); // Refresh
        });

        // Tata Letak Form
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; mainFormPanel.add(new JLabel("Pilih Penyetor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; mainFormPanel.add(penyetorBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; mainFormPanel.add(new JSeparator(), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; mainFormPanel.add(inputItemPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weighty = 1.0; mainFormPanel.add(itemScrollPane, gbc);
        
        JPanel summaryPanel = new JPanel(new GridLayout(2, 1));
        summaryPanel.add(lblTotalHarga);
        summaryPanel.add(lblTotalPoin);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.weighty = 0; mainFormPanel.add(summaryPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.EAST; mainFormPanel.add(finalisasiButton, gbc);

        panel.add(mainFormPanel, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Menu untuk menambah, mengubah, dan melihat daftar jenis sampah.
     */
    private JPanel createAddSampahPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("MANAJEMEN KATEGORI SAMPAH", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Daftar Jenis Sampah"));

        String[] columnNames = {"ID", "Jenis Sampah", "Harga/Kg (Rp)"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Isi data dari LoginView.sampahs
        for (Sampah s : LoginView.sampahs) {
            model.addRow(new Object[]{s.getIdSampah(), s.getJenis(), s.getHargaPerKg()});
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Form Tambah/Ubah
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Tambah Jenis Sampah Baru"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField jenisField = new JTextField(20);
        JTextField hargaField = new JTextField(20);
        JButton simpanButton = new JButton("Simpan Jenis Sampah");
        simpanButton.setBackground(GREEN_PRIMARY);
        simpanButton.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; rightPanel.add(new JLabel("Jenis Sampah:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; rightPanel.add(jenisField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; rightPanel.add(new JLabel("Harga Beli/Kg (Rp):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; rightPanel.add(hargaField, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; rightPanel.add(simpanButton, gbc);

        simpanButton.addActionListener(e -> {
            String jenis = jenisField.getText();
            double harga;
            if (jenis.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Jenis Sampah tidak boleh kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                harga = Double.parseDouble(hargaField.getText());
                if (harga <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Harga harus berupa angka positif.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Logika Simpan (Tambah Baru)
            String idSampah = "S" + String.format("%03d", LoginView.sampahs.size() + 1);
            Sampah newSampah = new Sampah(idSampah, jenis, harga);
            LoginView.sampahs.add(newSampah);

            JOptionPane.showMessageDialog(this, "Jenis Sampah baru berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            jenisField.setText("");
            hargaField.setText("");
            showPanel(createAddSampahPanel()); // Refresh
        });
        
        // Bagi panel menjadi dua kolom
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 10, 10));
        mainContent.add(leftPanel);
        mainContent.add(rightPanel);
        
        panel.add(mainContent, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Menu untuk menambah, mengubah, dan melihat daftar Reward.
     */
    private JPanel createAddRewardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("MANAJEMEN REWARD", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Daftar Reward Tersedia"));

        String[] columnNames = {"ID", "Nama Hadiah", "Poin Tukar", "Stok", "Deskripsi"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Isi data dari LoginView.rewards
        for (Reward r : LoginView.rewards) {
            model.addRow(new Object[]{r.getIdReward(), r.getNamaHadiah(), (int)r.getPoinTukar(), r.getStok(), r.getDeskripsi()});
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Form Tambah/Ubah
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Tambah Reward Baru"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField namaHadiahField = new JTextField(20);
        JTextField poinTukarField = new JTextField(20);
        JTextField stokField = new JTextField(20);
        JTextArea deskripsiArea = new JTextArea(3, 20);
        deskripsiArea.setLineWrap(true);
        deskripsiArea.setWrapStyleWord(true);
        JScrollPane deskripsiScrollPane = new JScrollPane(deskripsiArea);
        JButton simpanButton = new JButton("Simpan Reward Baru");
        simpanButton.setBackground(GREEN_PRIMARY);
        simpanButton.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; rightPanel.add(new JLabel("Nama Hadiah:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; rightPanel.add(namaHadiahField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; rightPanel.add(new JLabel("Poin Tukar:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; rightPanel.add(poinTukarField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; rightPanel.add(new JLabel("Stok Awal:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; rightPanel.add(stokField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.NORTHWEST; rightPanel.add(new JLabel("Deskripsi:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weighty = 1.0; rightPanel.add(deskripsiScrollPane, gbc);
        
        gbc.gridx = 1; gbc.gridy = 4; gbc.weighty = 0; gbc.anchor = GridBagConstraints.EAST; rightPanel.add(simpanButton, gbc);

        simpanButton.addActionListener(e -> {
            String nama = namaHadiahField.getText();
            String deskripsi = deskripsiArea.getText();
            int poin, stok;
            
            if (nama.isEmpty() || deskripsi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama dan Deskripsi tidak boleh kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                poin = Integer.parseInt(poinTukarField.getText());
                stok = Integer.parseInt(stokField.getText());
                if (poin <= 0 || stok < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Poin Tukar dan Stok harus berupa angka positif/nol.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Logika Simpan (Tambah Baru)
            String idReward = "R" + String.format("%03d", LoginView.rewards.size() + 1);
            Reward newReward = new Reward(idReward, nama, deskripsi, poin, stok);
            LoginView.rewards.add(newReward);

            JOptionPane.showMessageDialog(this, "Reward baru berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            namaHadiahField.setText("");
            poinTukarField.setText("");
            stokField.setText("");
            deskripsiArea.setText("");
            showPanel(createAddRewardPanel()); // Refresh
        });
        
        // Bagi panel menjadi dua kolom
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 10, 10));
        mainContent.add(leftPanel);
        mainContent.add(rightPanel);
        
        panel.add(mainContent, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createKomplainPanel() {
        // ... (kode Evaluasi Komplain sama seperti sebelumnya) ...
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("EVALUASI KELUHAN PENGGUNA"));
        
        String[] columnNames = {"ID Komplain", "ID Penyetor", "Judul", "Tanggal", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        List<Complain> daftarKomplain = LoginView.complains; 

        for (Complain c : daftarKomplain) {
            model.addRow(new Object[]{
                c.getIdComplain(), 
                c.getIdPenyetor(), 
                c.getJudul(), 
                c.getTanggal(), 
                c.getStatus()
            });
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton prosesButton = new JButton("Proses Komplain Terpilih");
        prosesButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih baris komplain untuk diproses.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String idComplain = model.getValueAt(selectedRow, 0).toString();
            Complain selectedComplain = daftarKomplain.stream()
                                            .filter(c -> c.getIdComplain().equals(idComplain))
                                            .findFirst().orElse(null);

            if (selectedComplain != null) {
                String tanggapan = JOptionPane.showInputDialog(this, "Masukkan Tanggapan Admin untuk Komplain " + idComplain + ":");
                if (tanggapan != null && !tanggapan.trim().isEmpty()) {
                    
                    Object[] options = {"DITERIMA", "DITOLAK"};
                    int statusChoice = JOptionPane.showOptionDialog(this, 
                        "Ubah status komplain menjadi:", 
                        "Pilih Status", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE, 
                        null, 
                        options, 
                        options[0]);

                    Status newStatus = statusChoice == JOptionPane.YES_OPTION ? Status.DITERIMA : Status.DITOLAK;
                    
                    selectedComplain.setTanggapanAdmin(tanggapan);
                    selectedComplain.setStatus(newStatus);
                    
                    JOptionPane.showMessageDialog(this, "Komplain berhasil diproses. Status: " + newStatus, "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    showPanel(createKomplainPanel()); // Refresh
                }
            }
        });
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(prosesButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }
}