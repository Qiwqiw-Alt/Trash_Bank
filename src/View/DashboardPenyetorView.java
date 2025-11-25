package View;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import Model.Penyetor;
import Model.BankSampah;
import Model.Sampah;
import Model.ItemTransaksi;
import Model.Transaksi;
import Model.Reward;
import Model.Complain;
import Model.PenukaranReward;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardPenyetorView extends JFrame {
    
    private Penyetor currentUser;
    private BankSampah bankSampah; 
    private JPanel contentPanel;
    
    // Icon & Style Constants
    private final Color GREEN_PRIMARY = new Color(0, 128, 0); 
    private final Color GREEN_LIGHT = new Color(200, 240, 200); 
    private final Color TEXT_DARK = new Color(50, 50, 50);

    public DashboardPenyetor(Penyetor user, BankSampah bankSampah) {
        this.currentUser = user;
        this.bankSampah = bankSampah; 
        String namaBank = bankSampah.getNamaBank() != null ? bankSampah.getNamaBank() : "Bank Sampah App";
        setTitle("Dashboard Penyetor - " + namaBank);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true); 
        
        initDashboard();

        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        showPanel(createHomePenyetorPanel());
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
        
        // Menu Penyetor
        menuPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel lblPenyetorTitle = createRoleTitle("PENYETOR MENU");
        menuPanel.add(lblPenyetorTitle);

        JPanel lblSetor = createMenuLabel("Setor Sampah", "SetorSampah", "icon_deposit.png");
        JPanel lblRiwayat = createMenuLabel("Riwayat Transaksi", "RiwayatTransaksi", "icon_history.png"); 
        JPanel lblTukarPoin = createMenuLabel("Tukar Poin / Reward", "TukarPoin", "icon_reward.png");
        JPanel lblKeluhan = createMenuLabel("Kirim Keluhan", "Keluhan", "icon_complain.png");

        menuPanel.add(lblSetor);
        menuPanel.add(lblRiwayat);
        menuPanel.add(lblTukarPoin);
        menuPanel.add(lblKeluhan);
        
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
        JLabel lblUser = new JLabel("Logged in as: " + currentUser.getNamaLengkap() + " (Penyetor)");
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
                newPanel = createHomePenyetorPanel();
                break;
            case "Profil":
                newPanel = createProfilPenyetorPanel(); 
                break;
            case "SetorSampah":
                newPanel = createSetorSampahPanel();
                break;
            case "RiwayatTransaksi": 
                newPanel = createRiwayatTransaksiPanel();
                break;
            case "TukarPoin":
                newPanel = createTukarPoinPanel();
                break;
            case "Keluhan":
                newPanel = createKeluhanPanel();
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
    
    // --- CONTENT PANEL METHODS ---

    private JPanel createHomePenyetorPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("Selamat Datang di Dashboard Penyetor!", SwingConstants.CENTER), BorderLayout.NORTH);
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        statsPanel.add(createStatCard("Total Poin Anda", String.valueOf(currentUser.getTotalPoin())));
        statsPanel.add(createStatCard("Total Setoran", String.valueOf(currentUser.getTotalSetoran()) + "x"));
        
        panel.add(statsPanel, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Menu untuk melihat daftar harga sampah dan memberikan form permintaan setoran.
     */
    private JPanel createSetorSampahPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("INFORMASI HARGA SAMPAH DAN PERMINTAAN SETORAN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);
        
        // Tabel Harga Sampah
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Daftar Harga Beli Sampah (per Kg)"));
        
        String[] columnNames = {"ID", "Jenis Sampah", "Harga/Kg (Rp)"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Sampah s : LoginView.sampahs) {
            model.addRow(new Object[]{s.getIdSampah(), s.getJenis(), s.getHargaPerKg()});
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setEnabled(false); // Hanya tampilan
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        // Bagian Bawah: Instruksi
        JLabel instruction = new JLabel(
            "<html><p align='center'>Setoran akan dicatat oleh Admin setelah diverifikasi.<br>Silakan hubungi Admin dengan ID Penyetor Anda (" + 
            currentUser.getIdPenyetor() + ") dan jenis sampah yang akan disetor.</p></html>", 
            SwingConstants.CENTER);
        instruction.setFont(new Font("Arial", Font.ITALIC, 14));
        tablePanel.add(instruction, BorderLayout.SOUTH);

        panel.add(tablePanel, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Menu untuk menampilkan riwayat transaksi penyetor.
     */
    private JPanel createRiwayatTransaksiPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("RIWAYAT TRANSAKSI PENYETORAN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);
        
        // Filter Transaksi Penyetor
        List<Transaksi> riwayatTransaksi = LoginView.transactions.stream()
            .filter(t -> t.getIdPenyetor().equals(currentUser.getIdPenyetor()))
            .collect(Collectors.toList());

        String[] columnNames = {"ID Transaksi", "Tanggal", "Total Harga (Rp)", "Total Poin"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        for (Transaksi t : riwayatTransaksi) {
            model.addRow(new Object[]{
                t.getIdTransaksi(), 
                t.getTanggal().format(formatter), 
                String.format("%.2f", t.getTotalHarga()), 
                t.getTotalPoin()
            });
        }
        
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        
        if (riwayatTransaksi.isEmpty()) {
            JLabel emptyLabel = new JLabel("Anda belum memiliki riwayat transaksi setoran.", SwingConstants.CENTER);
            panel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);
            
            // Detail Button (Opsional: Klik 2x atau Button untuk lihat detail item)
            JButton detailButton = new JButton("Lihat Detail Item Transaksi");
            detailButton.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String idTrx = model.getValueAt(selectedRow, 0).toString();
                    Transaksi selectedTrx = riwayatTransaksi.stream().filter(t -> t.getIdTransaksi().equals(idTrx)).findFirst().orElse(null);
                    
                    if (selectedTrx != null) {
                        StringBuilder detail = new StringBuilder("Detail Transaksi " + idTrx + ":\n");
                        detail.append("----------------------------------------------------\n");
                        for (ItemTransaksi item : selectedTrx.getItems()) {
                            // Mencari nama sampah dari ID
                            Sampah s = LoginView.sampahs.stream().filter(sam -> sam.getIdSampah().equals(item.getIdSampah())).findFirst().orElse(null);
                            String jenis = s != null ? s.getJenis() : "Sampah Tidak Dikenal";
                            
                            detail.append(String.format("- %s: %.2f Kg (Rp %.2f) = Rp %.2f\n", 
                                jenis, 
                                item.getBeratKg(), 
                                item.getHargaPerKg(),
                                item.getSubtotal()));
                        }
                        detail.append("----------------------------------------------------\n");
                        detail.append(String.format("Total Harga: Rp %.2f\n", selectedTrx.getTotalHarga()));
                        detail.append(String.format("Total Poin Diterima: %d Poin\n", selectedTrx.getTotalPoin()));
                        
                        JOptionPane.showMessageDialog(this, detail.toString(), "Detail Transaksi", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                     JOptionPane.showMessageDialog(this, "Pilih baris transaksi untuk melihat detail.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
            });
            
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomPanel.add(detailButton);
            panel.add(bottomPanel, BorderLayout.SOUTH);
        }

        return panel;
    }

    /**
     * Menu untuk menukar poin dengan reward.
     */
    private JPanel createTukarPoinPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("TUKAR POIN DENGAN REWARD", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        JLabel lblPoinAnda = new JLabel("Total Poin Anda Saat Ini: " + currentUser.getTotalPoin() + " Poin", SwingConstants.LEFT);
        lblPoinAnda.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblPoinAnda, BorderLayout.PAGE_START);
        
        // Tampilkan Daftar Reward dalam Grid
        JPanel rewardListPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 kolom
        JScrollPane scrollPane = new JScrollPane(rewardListPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Pilih Reward"));

        List<Reward> daftarReward = LoginView.rewards;

        if (daftarReward.isEmpty()) {
            rewardListPanel.add(new JLabel("Belum ada reward yang tersedia saat ini.", SwingConstants.CENTER));
        } else {
            for (Reward r : daftarReward) {
                JPanel card = createRewardCard(r);
                rewardListPanel.add(card);
            }
        }
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRewardCard(Reward reward) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(TEXT_DARK, 1));
        
        JLabel lblNama = new JLabel(reward.getNamaHadiah(), SwingConstants.CENTER);
        lblNama.setFont(new Font("Arial", Font.BOLD, 14));
        card.add(lblNama, BorderLayout.NORTH);
        
        JTextArea deskripsi = new JTextArea(reward.getDeskripsi());
        deskripsi.setEditable(false);
        deskripsi.setWrapStyleWord(true);
        deskripsi.setLineWrap(true);
        deskripsi.setBackground(card.getBackground());
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.add(deskripsi, BorderLayout.CENTER);
        card.add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblPoin = new JLabel(String.format("%d Poin | Stok: %d", (int)reward.getPoinTukar(), reward.getStok()), SwingConstants.CENTER);
        lblPoin.setForeground(GREEN_PRIMARY);
        bottomPanel.add(lblPoin);
        
        JButton tukarButton = new JButton("Tukar Sekarang");
        tukarButton.setEnabled(currentUser.getTotalPoin() >= reward.getPoinTukar() && reward.getStok() > 0);
        
        tukarButton.addActionListener(e -> tukarRewardAction(reward));
        
        bottomPanel.add(tukarButton);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    private void tukarRewardAction(Reward reward) {
        if (currentUser.getTotalPoin() < reward.getPoinTukar()) {
            JOptionPane.showMessageDialog(this, "Poin Anda tidak mencukupi untuk menukar hadiah ini.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (reward.getStok() <= 0) {
            JOptionPane.showMessageDialog(this, "Stok hadiah ini sudah habis.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            String.format("Yakin ingin menukar %s dengan %d Poin?", reward.getNamaHadiah(), (int)reward.getPoinTukar()), 
            "Konfirmasi Penukaran", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Logika Penukaran
            currentUser.setTotalPoin(currentUser.getTotalPoin() - (int)reward.getPoinTukar());
            reward.kurangiStok(1); // Kurangi stok
            
            // Catat Penukaran
            String idTukar = "TRD" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
            PenukaranReward pr = new PenukaranReward(idTukar, reward.getIdReward(), currentUser.getIdPenyetor());
            LoginView.penukaranRewards.add(pr);
            
            JOptionPane.showMessageDialog(this, 
                String.format("Penukaran berhasil! Anda mendapatkan %s. Sisa poin: %d.", 
                    reward.getNamaHadiah(), currentUser.getTotalPoin()), 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            showPanel(createTukarPoinPanel()); // Refresh
        }
    }

    private JPanel createProfilPenyetorPanel() {
        // ... (kode Edit Profil Penyetor sama seperti sebelumnya) ...
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("EDIT PROFIL PENYETOR", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Data Akun"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField namaLengkapField = new JTextField(currentUser.getNamaLengkap(), 20);
        JTextField usernameField = new JTextField(currentUser.getUsername(), 20);
        usernameField.setEditable(false); 
        JTextField noHpField = new JTextField(currentUser.getNoHp() != null ? currentUser.getNoHp() : "", 20);
        JPasswordField passwordField = new JPasswordField("", 20); 

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; formPanel.add(new JLabel("Nama Lengkap:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(namaLengkapField, gbc);
        
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
                currentUser.setNamaLengkap(namaLengkapField.getText());
                currentUser.setNoHp(noHpField.getText());
                String newPassword = String.valueOf(passwordField.getPassword());
                
                if (newPassword.length() > 0) {
                    currentUser.setPassword(newPassword);
                    JOptionPane.showMessageDialog(this, "Profil dan Password berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Profil berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                }
                showPanel(createProfilPenyetorPanel()); // Refresh
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan perubahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(formPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createKeluhanPanel() {
        // ... (kode Keluhan sama seperti sebelumnya) ...
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Bagian atas: Form Keluhan
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Kirim Keluhan Baru"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField judulField = new JTextField(30);
        JTextArea isiArea = new JTextArea(5, 30);
        isiArea.setLineWrap(true);
        isiArea.setWrapStyleWord(true);
        JScrollPane isiScrollPane = new JScrollPane(isiArea);
        JButton kirimButton = new JButton("Kirim Keluhan");
        kirimButton.setBackground(GREEN_PRIMARY);
        kirimButton.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; formPanel.add(new JLabel("Judul:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(judulField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST; formPanel.add(new JLabel("Isi Keluhan:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weighty = 1.0; formPanel.add(isiScrollPane, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2; gbc.weighty = 0.0; gbc.anchor = GridBagConstraints.EAST; formPanel.add(kirimButton, gbc);
        
        kirimButton.addActionListener(e -> {
            String judul = judulField.getText();
            String isi = isiArea.getText();
            
            if (judul.isEmpty() || isi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Judul dan isi keluhan tidak boleh kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String idComplain = "CMP" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
            Complain newComplain = new Complain(idComplain, currentUser.getIdPenyetor(), currentUser.getIdBankSampah(), judul, isi);
            LoginView.complains.add(newComplain);
            
            JOptionPane.showMessageDialog(this, 
                "Keluhan Anda berhasil dikirim (ID: " + idComplain + "). Status: SEDANG DITINJAU.", 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            judulField.setText("");
            isiArea.setText("");
            showPanel(createKeluhanPanel()); // Refresh untuk melihat daftar
        });

        // Bagian bawah: Daftar Riwayat Keluhan Penyetor
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder("Riwayat Keluhan Anda"));
        
        String[] columnNames = {"ID", "Judul", "Tanggal", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        List<Complain> riwayatKeluhan = LoginView.complains.stream()
            .filter(c -> c.getIdPenyetor().equals(currentUser.getIdPenyetor()))
            .collect(Collectors.toList());

        for (Complain c : riwayatKeluhan) {
            model.addRow(new Object[]{c.getIdComplain(), c.getJudul(), c.getTanggal(), c.getStatus()});
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(formPanel);
        panel.add(historyPanel);
        
        return panel;
    }
}