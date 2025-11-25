package View.PenyetorPanels;

import java.awt.BorderLayout;
// import java.awt.Color;

import javax.swing.BorderFactory;
// import javax.swing.JButton;
// import javax.swing.JOptionPane;
import javax.swing.JPanel;
// import javax.swing.JTextArea;

// import Model.BankSampah;
import Model.Penyetor;
import View.DashboardPenyetorView;

public class JoinBankPanel  extends JPanel {
    private Penyetor currentUser;
    private DashboardPenyetorView mainFrame;
    
    public JoinBankPanel(Penyetor user, DashboardPenyetorView mainFrame){
        this.currentUser = user;
        this.mainFrame = mainFrame; // Simpan referensi frame agar bisa di-refresh nanti

        // Setup Layout Dasar Panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panggil fungsi untuk mengisi komponen UI
        initUI();
    }

    private void initUI(){

    }

    // private JPanel createBankCard(BankSampah bank) {
    //     JPanel card = new JPanel(new BorderLayout());
    //     card.setBorder(BorderFactory.createTitledBorder(bank.getNamaBank()));
    //     card.setBackground(new Color(245, 255, 245));

    //     // Info Bank
    //     // JTextArea info = new JTextArea("Alamat: " + bank.getAlamat());
    //     // info.setEditable(false);
    //     // info.setLineWrap(true);
    //     // info.setWrapStyleWord(true);
    //     // info.setOpaque(false);
    //     // info.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    //     // card.add(info, BorderLayout.CENTER);

    //     // Tombol Gabung
    //     JButton btnJoin = new JButton("Gabung Sini");
    //     btnJoin.setBackground(new Color(0, 128, 0));
    //     btnJoin.setForeground(Color.WHITE);
        
    //     // Aksi saat tombol ditekan
    //     btnJoin.addActionListener(e -> aksiGabung(bank));
        
    //     card.add(btnJoin, BorderLayout.SOUTH);
    //     return card;
    // }

    // private void aksiGabung(BankSampah bank) {
    //     int confirm = JOptionPane.showConfirmDialog(this, 
    //         "Apakah Anda yakin ingin bergabung dengan " + bank.getNamaBank() + "?", 
    //         "Konfirmasi Gabung", 
    //         JOptionPane.YES_NO_OPTION);

    //     if (confirm == JOptionPane.YES_OPTION) {
    //         // 1. Update data User
    //         currentUser.setIdBankSampah(bank.getIdBank());
            
    //         // 2. Tampilkan pesan sukses
    //         JOptionPane.showMessageDialog(this, 
    //             "Selamat! Anda berhasil bergabung dengan " + bank.getNamaBank());

    //         // 3. REFRESH DASHBOARD (Penting!)
    //         // Tutup frame lama, buka frame baru dengan data user yang sudah diupdate & bank yang dipilih
    //         new DashboardPenyetorView(currentUser, bank).setVisible(true);
    //         mainFrame.dispose(); // Hancurkan frame lama
    //     }
    // }
}
