package View.AdminPanels;

import Model.BankSampah;
import Model.Penyetor;
import Model.TransaksiJoin;
import Database.DatabasePenyetor;
import Database.DatabaseRequestJoin;
import Controller.ListMemberController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ListMemberPanel extends JPanel {
    private BankSampah currentBank;

    private JTabbedPane tabLeft;
    private JTextField tfUserId;
    private JButton btnAdd;

    private JTable tableRequest;
    private DefaultTableModel modelRequest;
    private JButton btnAccept;
    private JButton btnReject;

    private JTable tableMember;
    private DefaultTableModel tableModel;

    private final Color PRIMARY_COLOR = new Color(0x316C6C); 
    private final Color ACCENT_COLOR = new Color(0x56A8A8); 
    private final Color DARK_BG = PRIMARY_COLOR.darker(); 
    private final Color SOFT_BG = PRIMARY_COLOR; 
    private final Color HEADER_BG = DARK_BG; 
    private final Color TEXT_COLOR = Color.WHITE; 
    private final Color TABLE_ROW_EVEN = SOFT_BG.brighter(); 
    private final Color TABLE_ROW_ODD = SOFT_BG; 
    private final Color SELECTION_COLOR = new Color(0x70C4C4); 

    private final Color GREEN_ACTION = new Color(0x28A745); 
    private final Color RED_DANGER = new Color(0xDC3545); 

    private final Font FONT_TITLE = new Font("Fredoka", Font.BOLD, 20);
    private final Font FONT_TEXT = new Font("Fredoka", Font.PLAIN, 14);
    private final Font FONT_LABEL = new Font("Fredoka", Font.BOLD, 14);
    private final int BORDER_RADIUS = 15;


    public ListMemberPanel(BankSampah bankSampah) {
        this.currentBank = bankSampah;
        initLayout();
        refreshTable();
        refreshRequestTable();
    }

    private void initLayout() {
        setLayout(new GridLayout(1, 2, 20, 20));
        setBackground(TEXT_COLOR); 
        setBorder(new EmptyBorder(25, 30, 25, 30));

        add(createLeftPanel());

        add(createRightPanel());
    }

    private JPanel createLeftPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false); 

        JPanel pnlContainerStyled = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(SOFT_BG); 
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, BORDER_RADIUS, BORDER_RADIUS);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        pnlContainerStyled.setLayout(new BorderLayout());
        pnlContainerStyled.setOpaque(false); 

        pnlContainerStyled.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(ACCENT_COLOR, 1, BORDER_RADIUS, true),
            new EmptyBorder(10, 10, 10, 10) 
        ));

        tabLeft = new JTabbedPane();
        tabLeft.setFont(FONT_LABEL.deriveFont(Font.BOLD, 13f));
        tabLeft.setOpaque(false);
        tabLeft.setBackground(SOFT_BG); 
        tabLeft.setForeground(TEXT_COLOR); 
        
        tabLeft.addTab("Tambah Manual", createManualAddPanel());
        
        tabLeft.addTab("Permintaan Masuk", createRequestPanel());

        pnlContainerStyled.add(tabLeft, BorderLayout.CENTER);
        return pnlContainerStyled;
    }
    
    private JPanel createManualAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(SOFT_BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; 

        JLabel lblTitle = new JLabel("Input ID Penyetor");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(ACCENT_COLOR);
        gbc.gridy = 0; panel.add(lblTitle, gbc);
        
        gbc.insets = new Insets(5, 0, 15, 0);
        JLabel lblDesc = new JLabel("<html>Masukkan ID Penyetor yang ingin<br>didaftarkan secara manual.</html>");
        lblDesc.setFont(FONT_TEXT);
        lblDesc.setForeground(TEXT_COLOR.brighter());
        gbc.gridy = 1; panel.add(lblDesc, gbc);

        gbc.insets = new Insets(15, 0, 5, 0);
        JLabel lblInput = new JLabel("ID Penyetor (cth: UP001):");
        lblInput.setFont(FONT_LABEL);
        lblInput.setForeground(TEXT_COLOR);
        gbc.gridy = 2; panel.add(lblInput, gbc);

        tfUserId = new JTextField();
        tfUserId.setFont(FONT_TEXT);
        tfUserId.setPreferredSize(new Dimension(200, 40));
        tfUserId.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR.darker(), 1));
        gbc.gridy = 3; panel.add(tfUserId, gbc);

        btnAdd = new JButton("Tambahkan Member");
        styleButton(btnAdd, GREEN_ACTION, TEXT_COLOR);
        btnAdd.setPreferredSize(new Dimension(200, 40));
        btnAdd.addActionListener(e -> handleAddMember());
        
        gbc.gridy = 4;
        gbc.insets = new Insets(25, 0, 0, 0);
        panel.add(btnAdd, gbc);
        
        gbc.gridy = 5; gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }

    private JPanel createRequestPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(SOFT_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Daftar Request Bergabung");
        lblTitle.setFont(FONT_LABEL.deriveFont(Font.BOLD, 16f));
        lblTitle.setForeground(ACCENT_COLOR);
        panel.add(lblTitle, BorderLayout.NORTH);

        String[] cols = {"ID Penyetor", "Nama Penyetor", "Status"}; 
        modelRequest = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableRequest = new JTable(modelRequest);
        styleTableCommon(tableRequest, HEADER_BG, SELECTION_COLOR); 
        tableRequest.setRowHeight(35); 
        
        tableRequest.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
             @Override
             public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                 JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                 label.setHorizontalAlignment(SwingConstants.CENTER);
                 
                 if (value.toString().equals("SEDANG_DITINJAU")) {
                     label.setForeground(new Color(0xFFC107)); 
                     label.setFont(label.getFont().deriveFont(Font.BOLD));
                 } else if (value.toString().equals("DITERIMA")) {
                     label.setForeground(GREEN_ACTION); 
                 } else if (value.toString().equals("DITOLAK")) {
                     label.setForeground(RED_DANGER); 
                 } else {
                     label.setForeground(TEXT_COLOR);
                 }

                 if (!isSelected) {
                     label.setBackground(row % 2 == 0 ? TABLE_ROW_EVEN : TABLE_ROW_ODD);
                 }
                 
                 return label;
             }
        });


        JScrollPane scrollPane = new JScrollPane(tableRequest);
        scrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(SOFT_BG);
        btnPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        btnAccept = new JButton("Terima");
        styleButton(btnAccept, GREEN_ACTION, TEXT_COLOR);
        btnAccept.addActionListener(e -> handleRequestAction(true));

        btnReject = new JButton("Tolak");
        styleButton(btnReject, RED_DANGER, TEXT_COLOR);
        btnReject.addActionListener(e -> handleRequestAction(false));

        btnPanel.add(btnAccept);
        btnPanel.add(btnReject);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(SOFT_BG);

        JPanel pnlContainerStyled = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(SOFT_BG); 
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, BORDER_RADIUS, BORDER_RADIUS);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        pnlContainerStyled.setLayout(new BorderLayout());
        pnlContainerStyled.setOpaque(false);
        pnlContainerStyled.add(panel, BorderLayout.CENTER);
        panel.setOpaque(false);
        
        pnlContainerStyled.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(ACCENT_COLOR, 1, BORDER_RADIUS, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel("Daftar Anggota Aktif");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(ACCENT_COLOR);
        panel.add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"ID", "Nama Lengkap", "No. HP"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tableMember = new JTable(tableModel);
        styleTableCommon(tableMember, HEADER_BG, SELECTION_COLOR);

        JScrollPane scrollPane = new JScrollPane(tableMember);
        scrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        return pnlContainerStyled;
    }

    private void handleAddMember() {
        String targetId = tfUserId.getText().trim();
        
        if (targetId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Penyetor tidak boleh kosong!");
            return;
        }

        if (ListMemberController.getService().isUserAvilable(targetId)) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Tambahkan User ID: " + targetId + " ke database lokal bank ini?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                prosesTambahMember(targetId);
                tfUserId.setText(""); 
            }
        } else {
            JOptionPane.showMessageDialog(this, "ID tidak ditemukan atau sudah punya Bank lain.");
        }
    }

    private void handleRequestAction(boolean isAccepted) {
        int row = tableRequest.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih user dari tabel request dulu!");
            return;
        }

        String userId = (String) modelRequest.getValueAt(row, 0);
        String userName = (String) modelRequest.getValueAt(row, 1);
        
        String currentStatus = modelRequest.getValueAt(row, 2).toString();
        if(currentStatus.equals("DITERIMA") || currentStatus.equals("DITOLAK")){
             JOptionPane.showMessageDialog(this, "Request ini sudah diproses sebelumnya.");
             return;
        }

        if (isAccepted) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Terima " + userName + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                
                prosesTambahMember(userId); 
                
                
                DatabaseRequestJoin.updateStatus(userId, TransaksiJoin.Status.DITERIMA, currentBank.getFileRequestJoin());
                
                JOptionPane.showMessageDialog(this, "Anggota Diterima!");
            }
        } else {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Tolak permintaan " + userName + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                
                DatabasePenyetor.assignUserToBank(userId, "null"); 
                
                
                DatabaseRequestJoin.updateStatus(userId, TransaksiJoin.Status.DITOLAK, currentBank.getFileRequestJoin());

                JOptionPane.showMessageDialog(this, "Permintaan Ditolak.");
            }
        }
        
        refreshRequestTable();
        refreshTable();
    }

    private void prosesTambahMember(String userId) {
        boolean globalSuccess = DatabasePenyetor.assignUserToBank(userId, currentBank.getIdBank());
        
        if (globalSuccess) {
            Object userObj = ListMemberController.getService().getUserById(userId);
            if (userObj instanceof Penyetor) {
                Penyetor p = (Penyetor) userObj;
                p.setIdBankSampah(currentBank.getIdBank());
                
                DatabasePenyetor.addPenyetor(p, currentBank.getFilePenyetor());
                
                refreshTable();
                refreshRequestTable();
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        if (currentBank != null) {
            List<Penyetor> myMembers = DatabasePenyetor.loadData(currentBank.getFilePenyetor());
            for (Penyetor p : myMembers) {
                tableModel.addRow(new Object[]{ p.getIdPenyetor(), p.getNamaLengkap(), p.getNoHp() });
            }
        }
    }

    private void refreshRequestTable() {
        modelRequest.setRowCount(0);
        
        ArrayList<TransaksiJoin> daftarTransaksiJoins = DatabaseRequestJoin.loadData(currentBank.getFileRequestJoin());
        
        for(TransaksiJoin tj : daftarTransaksiJoins){
            Object userObj = ListMemberController.getService().getUserById(tj.getIdPenyetor());
            String namaPenyetor = "Tidak Dikenal";
            if (userObj instanceof Penyetor) {
                namaPenyetor = ((Penyetor) userObj).getNamaLengkap();
            }

            modelRequest.addRow(new Object[]{ 
                tj.getIdPenyetor(), 
                namaPenyetor,
                tj.getStatusRequest()
            });
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
        table.setSelectionForeground(Color.BLACK); 
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