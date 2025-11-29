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

    private ImageIcon iconImage = new ImageIcon("D:\\proyek-pbo\\Trash_Bank\\src\\Asset\\Image\\recycle-bin.png");

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

        // Penyesuaian WELCOME SECTION
        if (welcomeWrapper != null) {
            welcomeWrapper.setPreferredSize(new Dimension(contentWidth, 500));
            welcomeWrapper.revalidate();
        }

        // Penyesuaian ABOUT SECTION
        if (aboutWrapper != null) {
            // Gunakan contentWidth penuh untuk about wrapper
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

        // ======================= HEADER =======================
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

        // ======================= BODY SCROLL =======================
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

        // ======================= WELCOME SECTION =======================
        welcomeWrapper = new JPanel(new GridLayout(1, 2));
        welcomeWrapper.setBackground(Color.WHITE);
        welcomeWrapper.setPreferredSize(new Dimension(1000, 500));
        welcomeWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500)); 
        welcomeWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        scrollableContent.add(welcomeWrapper);

        // ========== LEFT PANEL ==========
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

        // ========== RIGHT PANEL (IMAGE) ==========
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        ImageIcon img = new ImageIcon("D:\\proyek-pbo\\Trash_Bank\\src\\Asset\\Image\\e1xf_1j1j_220225.jpg");
        Image scaled = img.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
        img = new ImageIcon(scaled);

        JLabel imagePlaceholder = new JLabel(img);
        rightPanel.add(imagePlaceholder);

        welcomeWrapper.add(rightPanel);

        // ======================= ABOUT SECTION =======================
        scrollableContent.add(Box.createVerticalStrut(40));

        JLabel aboutTitle = new JLabel("ABOUT");
        aboutTitle.setFont(new Font("Fredoka", Font.BOLD, 24));
        aboutTitle.setForeground(new Color(0x356A69));
        aboutTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollableContent.add(aboutTitle);

        scrollableContent.add(Box.createVerticalStrut(20));

        // PERBAIKAN: Gunakan panel dengan layout yang lebih fleksibel
        aboutWrapper = new JPanel();
        aboutWrapper.setLayout(new BoxLayout(aboutWrapper, BoxLayout.X_AXIS));
        aboutWrapper.setBackground(Color.WHITE);
        aboutWrapper.setBorder(BorderFactory.createEmptyBorder(20, 60, 60, 60));
        aboutWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Atur maksimum width yang lebih besar untuk mencegah pemotongan
        aboutWrapper.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));

        // Panel untuk konten about
        JPanel aboutContentPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        aboutContentPanel.setBackground(Color.WHITE);
        aboutContentPanel.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));

        // --- TEKS ABOUT 1 ---
        JPanel about1Panel = new JPanel(new BorderLayout());
        about1Panel.setBackground(Color.WHITE);
        JTextArea about1 = createWrapTextArea(
    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Sint magnam ab magni voluptates voluptatum nescunt, vel a qui earum explicabo possimus odit assumenda consectetur quia. Temporibus delectus sit nostrum. Ipsam. Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium optio, eaque rerum! Provident similique accusantium nemo autem. Veritatis obcaecati tenetur iure eius earum ut molestias architecto voluptate aliquam nihil, eveniet aliquid culpa officia aut! Impedit sit sunt quaerat, odit, tenetur error, harum nesciunt ipsum debitis quas aliquid. Reprehenderit, quia. Perspiciatis ad voluptatem qui illum."
        );
        about1Panel.add(about1, BorderLayout.CENTER);

        // --- TEKS ABOUT 2 ---
        JPanel about2Panel = new JPanel(new BorderLayout());
        about2Panel.setBackground(Color.WHITE);
        JTextArea about2 = createWrapTextArea(
    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Sint magnam ab magni voluptates voluptatum nescunt, vel a qui earum explicabo possimus odit assumenda consectetur quia. Temporibus delectus sit nostrum. Ipsam. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium."
        );
        about2Panel.add(about2, BorderLayout.CENTER);

        aboutContentPanel.add(about1Panel);
        aboutContentPanel.add(about2Panel);
        
        aboutWrapper.add(aboutContentPanel);
        scrollableContent.add(aboutWrapper);
        
        // Tambahkan padding di paling bawah
        scrollableContent.add(Box.createVerticalStrut(50));

        // ======================= ACTION =======================
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
        
        // PERBAIKAN: Atur preferred size yang sesuai
        area.setPreferredSize(new Dimension(300, 300)); // Lebar minimum, tinggi fleksibel
        area.setMinimumSize(new Dimension(250, 200));
        
        return area;
    }

    private void navigateToLogin() {
        // Asumsi LoginView ada
        new LoginView().setVisible(true); 
        dispose();
    }

    
}