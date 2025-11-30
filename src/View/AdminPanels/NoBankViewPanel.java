package View.AdminPanels;

import View.DashboardAdminView;
import javax.swing.*;
import java.awt.*;

public class NoBankViewPanel extends JPanel {

    private DashboardAdminView parentFrame;

    public NoBankViewPanel(DashboardAdminView parentFrame) {
        this.parentFrame = parentFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

 
        JLabel iconLabel = new JLabel("", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Fredoka", Font.PLAIN, 60));
        add(iconLabel, gbc);

        // Pesan Utama
        gbc.gridy = 1;
        JLabel lblMessage = new JLabel("Anda belum membuat akun Bank Sampah.", SwingConstants.CENTER);
        lblMessage.setFont(new Font("Fredoka", Font.BOLD, 20));
        lblMessage.setForeground(new Color(80, 80, 80));
        add(lblMessage, gbc);

        // Pesan Sub-judul
        gbc.gridy = 2;
        JLabel lblSub = new JLabel("Silakan buat terlebih dahulu untuk mengakses fitur lainnya.", SwingConstants.CENTER);
        lblSub.setFont(new Font("Fredoka", Font.PLAIN, 14));
        lblSub.setForeground(Color.GRAY);
        add(lblSub, gbc);


        gbc.gridy = 3;
        gbc.insets = new Insets(30, 10, 10, 10); 
        JButton btnCreate = new JButton("Buat Bank Sampah Sekarang");
        btnCreate.setBackground(new Color(0, 128, 0)); 
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setFont(new Font("Fredoka", Font.BOLD, 14));
        btnCreate.setFocusPainted(false);
        btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCreate.setPreferredSize(new Dimension(250, 40));
        
        btnCreate.addActionListener(e -> {

            parentFrame.switchPanel("CreateBank");
        });
        
        add(btnCreate, gbc);
    }
}