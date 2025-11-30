package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class LandingView extends JFrame {

    private JButton loginButton;
    private JButton startButton;
    private JScrollPane scrollPane;
    private JPanel scrollableContent;
    private JPanel welcomeWrapper;
    private JPanel aboutWrapper;

    private ImageIcon iconImage = new ImageIcon("src\\Asset\\Image\\recycle-bin.png");

    public LandingView() {
        setTitle("Selamat Datang - Bank Sampah");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setIconImage(iconImage.getImage());

        initComponents();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustContentWidth();
            }
        });

        adjustContentWidth();
    }

    private void adjustContentWidth() {
        int frameWidth = getContentPane().getWidth();
        int minContentWidth = 800;
        int contentWidth = Math.max(minContentWidth, (int) (frameWidth * 0.8));

        if (welcomeWrapper != null) {
            welcomeWrapper.setPreferredSize(new Dimension(contentWidth, 500));
            welcomeWrapper.revalidate();
        }

        if (aboutWrapper != null) {
            aboutWrapper.setPreferredSize(new Dimension(contentWidth, aboutWrapper.getPreferredSize().height));
            aboutWrapper.revalidate();
        }

        if (scrollPane != null) {
            scrollableContent.revalidate();
            scrollableContent.repaint();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0x356A69));
        headerPanel.setPreferredSize(new Dimension(0, 60));

        JLabel headerLabel = new JLabel("BANK SAMPAH");
        headerLabel.setFont(new Font("Fredoka", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        headerPanel.add(headerLabel, BorderLayout.WEST);

        JPanel loginButtonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 10));
        loginButtonWrapper.setOpaque(false);

        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Fredoka", Font.BOLD, 14));
        loginButton.setBackground(new Color(0x67AE6E));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        loginButtonWrapper.add(loginButton);
        headerPanel.add(loginButtonWrapper, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        scrollableContent = new JPanel();
        scrollableContent.setLayout(new BoxLayout(scrollableContent, BoxLayout.Y_AXIS));
        scrollableContent.setBackground(Color.WHITE);
        scrollableContent.setAlignmentX(Component.CENTER_ALIGNMENT);

        scrollPane = new JScrollPane(scrollableContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        welcomeWrapper = new JPanel(new GridLayout(1, 2));
        welcomeWrapper.setBackground(Color.WHITE);
        welcomeWrapper.setPreferredSize(new Dimension(1000, 500));
        welcomeWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        welcomeWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        scrollableContent.add(welcomeWrapper);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(80, 50, 80, 50));
        leftPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeTitle = new JLabel("SELAMAT DATANG DI");
        welcomeTitle.setFont(new Font("Fredoka", Font.BOLD, 36));
        welcomeTitle.setForeground(new Color(0x356A69));
        welcomeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeTitle2 = new JLabel("BANK SAMPAH!");
        welcomeTitle2.setFont(new Font("Fredoka", Font.BOLD, 36));
        welcomeTitle2.setForeground(new Color(0x356A69));
        welcomeTitle2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subTitle = new JLabel("Ubah barang tidak terpakai menjadi lebih bernilai");
        subTitle.setFont(new Font("Fredoka", Font.PLAIN, 16));
        subTitle.setForeground(Color.GRAY);
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        startButton = new JButton("START");
        startButton.setFont(new Font("Fredoka", Font.BOLD, 16));
        startButton.setBackground(new Color(0x356A69));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setPreferredSize(new Dimension(150, 50));
        startButton.setMaximumSize(new Dimension(150, 50));
        startButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(welcomeTitle);
        leftPanel.add(welcomeTitle2);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(subTitle);
        leftPanel.add(Box.createVerticalStrut(40));
        leftPanel.add(startButton);

        welcomeWrapper.add(leftPanel);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        ImageIcon img = new ImageIcon("src\\Asset\\Image\\e1xf_1j1j_220225.jpg");
        Image scaled = img.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
        img = new ImageIcon(scaled);

        JLabel imagePlaceholder = new JLabel(img);
        rightPanel.add(imagePlaceholder);

        welcomeWrapper.add(rightPanel);

        scrollableContent.add(Box.createVerticalStrut(40));

        JLabel aboutTitle = new JLabel("ABOUT");
        aboutTitle.setFont(new Font("Fredoka", Font.BOLD, 24));
        aboutTitle.setForeground(new Color(0x356A69));
        aboutTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollableContent.add(aboutTitle);

        scrollableContent.add(Box.createVerticalStrut(20));

        aboutWrapper = new JPanel();
        aboutWrapper.setLayout(new BoxLayout(aboutWrapper, BoxLayout.X_AXIS));
        aboutWrapper.setBackground(Color.WHITE);
        aboutWrapper.setBorder(BorderFactory.createEmptyBorder(20, 60, 60, 60));
        aboutWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        aboutWrapper.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));

        JPanel aboutContentPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        aboutContentPanel.setBackground(Color.WHITE);
        aboutContentPanel.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));

        JPanel about1Panel = new JPanel(new BorderLayout());
        about1Panel.setBackground(Color.WHITE);
        JTextArea about1 = createWrapTextArea(
                "Data SIPSN 2023 menunjukkan bahwa dari 31,9 juta ton timbunan sampah nasional, sebanyak 11,3 juta ton (35,67%) masih belum terkelola dan berisiko menjadi sumber penyakit. Rendahnya partisipasi masyarakat dalam memilah sampah menjadi kendala utama yang perlu diatasi melalui kombinasi edukasi dan insentif. Solusi strategisnya adalah pengadaan fasilitas Bank Sampah, di mana masyarakat dapat menukarkan sampah daur ulang menjadi uang atau barang. Pendekatan ini diharapkan mampu mengubah persepsi masyarakat dan meningkatkan partisipasi aktif mereka dalam pengelolaan sampah secara berkelanjutan.");
        about1Panel.add(about1, BorderLayout.CENTER);

        JPanel about2Panel = new JPanel(new BorderLayout());
        about2Panel.setBackground(Color.WHITE);
        JTextArea about2 = createWrapTextArea(
                "Guna mendukung optimalisasi fasilitas bank sampah, kami merancang sebuah aplikasi terintegrasi yang menghubungkan masyarakat dan pengelola. Solusi digital ini hadir untuk mengubah proses manajemen sampah yang sebelumnya rumit menjadi lebih sederhana, transparan, dan terorganisir.Kunci utama aplikasi ini adalah sistem insentif, di mana masyarakat mendapatkan reward berupa uang atau barang dari hasil penyetoran sampah. Dengan adanya keuntungan nyata dan kemudahan teknologi ini, diharapkan partisipasi masyarakat meningkat dan tercipta perubahan perilaku jangka panjang dalam menjaga kebersihan lingkungan.");
        about2Panel.add(about2, BorderLayout.CENTER);

        aboutContentPanel.add(about1Panel);
        aboutContentPanel.add(about2Panel);

        aboutWrapper.add(aboutContentPanel);
        scrollableContent.add(aboutWrapper);
        scrollableContent.add(Box.createVerticalStrut(50));

        loginButton.addActionListener(e -> navigateToLogin());
        startButton.addActionListener(e -> navigateToLogin());
    }

    private JTextArea createWrapTextArea(String txt) {
        JTextArea area = new JTextArea(txt);
        area.setFont(new Font("Fredoka", Font.PLAIN, 15));
        area.setForeground(Color.GRAY);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setEditable(false);
        area.setOpaque(false);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        area.setPreferredSize(new Dimension(300, 300));
        area.setMinimumSize(new Dimension(250, 200));

        return area;
    }

    private void navigateToLogin() {

        new LoginView().setVisible(true);
        dispose();
    }

}