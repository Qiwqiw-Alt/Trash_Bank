package View.AdminPanels;

import Controller.SetoranPenyetorController;
import Model.BankSampah;
import Model.ItemTransaksi;
import Model.Transaksi;
import Model.Transaksi.Status;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SetoranPenyetorPanel extends JPanel {
    private BankSampah currentBank;
    private String selectedIdTrx = null;

    private JTable tableMaster;
    private DefaultTableModel modelMaster;
    private JLabel lblIdTrx, lblPenyetor, lblTanggal, lblTotalBerat;
    private JTable tableDetail;
    private DefaultTableModel modelDetail;

    private JComboBox<Status> cbStatus;
    private JButton btnUpdateStatus;

    private final Color PRIMARY_COLOR = new Color(0x316C6C); 
    private final Color ACCENT_COLOR = new Color(0x56A8A8); 
    private final Color DARK_BG = PRIMARY_COLOR.darker(); 
    private final Color SOFT_BG = PRIMARY_COLOR; 
    private final Color HEADER_BG = DARK_BG; 
    private final Color TEXT_COLOR = Color.WHITE; 
    private final Color TABLE_ROW_EVEN = SOFT_BG.brighter(); 
    private final Color TABLE_ROW_ODD = SOFT_BG; 
    private final Color SELECTION_COLOR = new Color(0, 150, 136, 150); 
    private final int BORDER_RADIUS = 15; 

    private final Font FONT_TITLE = new Font("Fredoka", Font.BOLD, 22);
    private final Font FONT_LABEL = new Font("Fredoka", Font.BOLD, 14);


    public SetoranPenyetorPanel(BankSampah bankSampah) {
        this.currentBank = bankSampah;
        initLayout();
        refreshMasterTable();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(TEXT_COLOR);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(TEXT_COLOR);
        mainContent.setBorder(new EmptyBorder(25, 30, 25, 30));

        mainContent.add(createMasterSection());
        mainContent.add(Box.createVerticalStrut(30));

        JSeparator separator = new JSeparator();
        separator.setForeground(ACCENT_COLOR);
        mainContent.add(separator);
        mainContent.add(Box.createVerticalStrut(30));

        mainContent.add(createDetailSection());

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBackground(DARK_BG);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createMasterSection() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(SOFT_BG);

        JPanel pnlContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(SOFT_BG);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, BORDER_RADIUS, BORDER_RADIUS);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        pnlContainer.setLayout(new BorderLayout());
        pnlContainer.setOpaque(false);
        pnlContainer.add(panel, BorderLayout.CENTER);
        panel.setOpaque(false); 

        pnlContainer.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(10, 10, 10, 10), 
            BorderFactory.createCompoundBorder(
                new RoundedBorder(ACCENT_COLOR, 1, BORDER_RADIUS, true), 
                new EmptyBorder(15, 20, 15, 20) 
            )
        ));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false); 

        JLabel lblTitle = new JLabel("Daftar Semua Transaksi");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(ACCENT_COLOR); 

        try {
            java.net.URL imgURL = getClass().getResource("/Asset/Image/transaction.png"); 
            if (imgURL != null) {
                ImageIcon listIcon = new ImageIcon(imgURL);
                Image img = listIcon.getImage();
                Image newImg = img.getScaledInstance(28, 28, Image.SCALE_SMOOTH);
                lblTitle.setIcon(new ImageIcon(newImg));
                lblTitle.setIconTextGap(10);
            }
        } catch (Exception ignored) { }

        JButton btnRefresh = new JButton("Refresh");
        styleButton(btnRefresh, ACCENT_COLOR, DARK_BG); 
        btnRefresh.addActionListener(e -> refreshMasterTable());

        header.add(lblTitle, BorderLayout.WEST);
        header.add(btnRefresh, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        String[] cols = {"ID Trx", "ID Penyetor", "Status", "Tanggal", "Total Berat", "Total Harga"};
        modelMaster = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tableMaster = new JTable(modelMaster);
        styleTableCommon(tableMaster, HEADER_BG, SELECTION_COLOR);

        JScrollPane scroll = new JScrollPane(tableMaster);
        scroll.setPreferredSize(new Dimension(800, 250)); 
        scroll.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));
        panel.add(scroll, BorderLayout.CENTER);

        tableMaster.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableMaster.getSelectedRow();
                if (row != -1) {
                    loadDetailData(row);
                }
            }
        });

        return pnlContainer;
    }

    private JPanel createDetailSection() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(SOFT_BG);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            new RoundedBorder(ACCENT_COLOR, 1, BORDER_RADIUS, true), " Detail & Validasi Transaksi ");
        titledBorder.setTitleFont(FONT_LABEL.deriveFont(Font.BOLD));
        titledBorder.setTitleColor(ACCENT_COLOR); 
        panel.setBorder(BorderFactory.createCompoundBorder(titledBorder, new EmptyBorder(15, 15, 15, 15)));

        JPanel pnlDetailContainer = new JPanel();
        pnlDetailContainer.setLayout(new BoxLayout(pnlDetailContainer, BoxLayout.Y_AXIS));
        pnlDetailContainer.setBackground(SOFT_BG);

        JPanel pnlInfo = new JPanel(new GridBagLayout());
        pnlInfo.setBackground(SOFT_BG);
        pnlInfo.setBorder(new EmptyBorder(0, 0, 15, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblIdTrx = new JLabel("-");
        lblPenyetor = new JLabel("-");
        lblTanggal = new JLabel("-");
        lblTotalBerat = new JLabel("0 Kg");

        JLabel[] valueLabels = {lblIdTrx, lblPenyetor, lblTanggal, lblTotalBerat};
        for(JLabel label : valueLabels) {
            label.setFont(FONT_LABEL.deriveFont(Font.PLAIN, 14f));
            label.setForeground(TEXT_COLOR);
        }

        JLabel lblIdTrxKet = new JLabel("ID Transaksi:");
        JLabel lblPenyetorKet = new JLabel("Penyetor:");
        JLabel lblTanggalKet = new JLabel("Tanggal:");
        JLabel lblTotalBeratKet = new JLabel("Total Berat:");

        JLabel[] ketLabels = {lblIdTrxKet, lblPenyetorKet, lblTanggalKet, lblTotalBeratKet};
        for(JLabel label : ketLabels) {
            label.setFont(FONT_LABEL);
            label.setForeground(TEXT_COLOR.brighter()); 
        }

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1; gbc.anchor = GridBagConstraints.WEST;
        pnlInfo.add(lblIdTrxKet, gbc);
        gbc.gridy = 1;
        pnlInfo.add(lblPenyetorKet, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        pnlInfo.add(lblIdTrx, gbc);
        gbc.gridy = 1;
        pnlInfo.add(lblPenyetor, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlInfo.add(lblTanggalKet, gbc);
        gbc.gridy = 1;
        pnlInfo.add(lblTotalBeratKet, gbc);

        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.4;
        pnlInfo.add(lblTanggal, gbc);
        gbc.gridy = 1;
        pnlInfo.add(lblTotalBerat, gbc);


        pnlDetailContainer.add(pnlInfo);
        pnlDetailContainer.add(Box.createVerticalStrut(15));

        JLabel lblRincianHeader = new JLabel("Rincian Sampah yang Disetor");
        lblRincianHeader.setFont(FONT_LABEL.deriveFont(Font.BOLD, 16f));
        lblRincianHeader.setForeground(ACCENT_COLOR);
        lblRincianHeader.setBorder(new EmptyBorder(0, 10, 10, 0));
        lblRincianHeader.setAlignmentX(Component.LEFT_ALIGNMENT); 
        pnlDetailContainer.add(lblRincianHeader);

        String[] cols = {"ID Sampah", "Berat (Kg)", "Harga/Kg", "Subtotal"};
        modelDetail = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tableDetail = new JTable(modelDetail);
        styleTableCommon(tableDetail, PRIMARY_COLOR.brighter().darker(), SELECTION_COLOR);

        JScrollPane scroll = new JScrollPane(tableDetail);
        scroll.setPreferredSize(new Dimension(800, 150));
        scroll.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));
        pnlDetailContainer.add(scroll);

        panel.add(pnlDetailContainer, BorderLayout.NORTH);

        JPanel pnlAction = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlAction.setBackground(SOFT_BG);
        pnlAction.setBorder(new EmptyBorder(20, 0, 0, 0));

        JLabel lblStatus = new JLabel("Ubah Status Validasi:");
        lblStatus.setFont(FONT_LABEL);
        lblStatus.setForeground(TEXT_COLOR);
        pnlAction.add(lblStatus);

        cbStatus = new JComboBox<>(Status.values());
        cbStatus.setPreferredSize(new Dimension(200, 35));
        cbStatus.setFont(new Font("Fredoka", Font.PLAIN, 14));
        cbStatus.setBackground(TEXT_COLOR); 
        cbStatus.setForeground(DARK_BG); 
        pnlAction.add(cbStatus);

        btnUpdateStatus = new JButton("Simpan Status");
        styleButton(btnUpdateStatus, ACCENT_COLOR, DARK_BG);
        btnUpdateStatus.setPreferredSize(new Dimension(150, 35));
        btnUpdateStatus.addActionListener(e -> handleUpdateStatus());
        pnlAction.add(btnUpdateStatus);

        panel.add(pnlAction, BorderLayout.SOUTH);

        toggleDetail(false);

        return panel;
    }

    private void refreshMasterTable() {
        modelMaster.setRowCount(0);
        toggleDetail(false);

        ArrayList<Transaksi> listTrx = SetoranPenyetorController.getService().daftarTransaksis(currentBank);

        for (Transaksi t : listTrx) {
            modelMaster.addRow(new Object[]{
                t.getIdTransaksi(),
                t.getIdPenyetor(),
                t.getStatus(),
                t.getTanggal().toString(),
                t.getTotalBerat(),
                t.getTotalHarga()
            });
        }
    }

    private void loadDetailData(int row) {
        selectedIdTrx = (String) modelMaster.getValueAt(row, 0);

        Transaksi trx = SetoranPenyetorController.getService().getTrxById(selectedIdTrx, currentBank);

        if (trx != null) {
            lblIdTrx.setText(trx.getIdTransaksi());
            lblPenyetor.setText(trx.getIdPenyetor());
            lblTanggal.setText(trx.getTanggal().toString());
            lblTotalBerat.setText(trx.getTotalBerat() + " Kg");

            cbStatus.setSelectedItem(trx.getStatus());
        }

        modelDetail.setRowCount(0);
        ArrayList<ItemTransaksi> allItems = SetoranPenyetorController.getService().daftarItemTransaksi(currentBank);
        for (ItemTransaksi itm : allItems) {
            if (itm.getIdTransaksi().equals(selectedIdTrx)) {
                modelDetail.addRow(new Object[]{
                    itm.getIdSampah(),
                    itm.getBeratKg(),
                    itm.getHargaPerKg(),
                    itm.getSubtotal()
                });
            }
        }

        toggleDetail(true);
    }

    private void handleUpdateStatus() {
        if (selectedIdTrx == null) return;

        Status newStatus = (Status) cbStatus.getSelectedItem();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Ubah status transaksi ini menjadi " + newStatus + "?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Transaksi trx = SetoranPenyetorController.getService().getTrxById(selectedIdTrx, currentBank);
            if (trx != null) {
                trx.setStatus(newStatus);
                SetoranPenyetorController.getService().updateTransaksiStatus(trx, currentBank);

                JOptionPane.showMessageDialog(this, "Status berhasil diperbarui!");
                refreshMasterTable();
            }
        }
    }

    private void toggleDetail(boolean enable) {
        cbStatus.setEnabled(enable);
        btnUpdateStatus.setEnabled(enable);
        tableDetail.setEnabled(enable);

        if (!enable) {
            lblIdTrx.setText("-");
            lblPenyetor.setText("-");
            lblTanggal.setText("-");
            lblTotalBerat.setText("0 Kg");
            modelDetail.setRowCount(0);
            selectedIdTrx = null;
        }
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Fredoka", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new RoundedBorder(bg.darker(), 1, 8, false)); 
        btn.setMargin(new Insets(8, 15, 8, 15));
    }

    private void styleTableCommon(JTable table, Color headerColor, Color selectionColor) {
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Fredoka", Font.BOLD, 13));
        header.setBackground(headerColor);
        header.setForeground(TEXT_COLOR);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        final Color BORDER_COLOR = new Color(255, 255, 255, 40); 
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBackground(headerColor);
                label.setForeground(TEXT_COLOR);
                label.setFont(new Font("Fredoka", Font.BOLD, 13));

                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

                return label;
            }
        });

        table.setRowHeight(30);
        table.setFont(new Font("Fredoka", Font.PLAIN, 13));
        table.setGridColor(new Color(60, 120, 120, 100));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                ((DefaultTableCellRenderer) c).setHorizontalAlignment(SwingConstants.CENTER);

                c.setForeground(TEXT_COLOR); 

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? TABLE_ROW_EVEN : TABLE_ROW_ODD);
                }

                return c;
            }
        });

        table.setSelectionBackground(selectionColor);
        table.setSelectionForeground(TEXT_COLOR);
        table.setFillsViewportHeight(true);
    }

    private static class RoundedBorder extends LineBorder {
        private int radius;
        private boolean antiAlias;

        public RoundedBorder(Color color, int thickness, int radius, boolean antiAlias) {
            super(color, thickness);
            this.radius = radius;
            this.antiAlias = antiAlias;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            if (antiAlias) {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }

            g2d.setColor(getLineColor());

            g2d.setStroke(new BasicStroke(getThickness()));
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);

            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            int t = getThickness();
            int padding = radius / 3;
            return new Insets(t + padding, t + padding, t + padding, t + padding);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
}