package View.PenyetorPanels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import Database.DatabaseComplain;
import Model.BankSampah;
import Model.Complain;
import Model.Penyetor;
import Service.BankSampahService;
import Service.ComplainService;

public class KeluhanPanel extends JPanel {
   private Penyetor user;
   private BankSampah bank;
   private ComplainService cs;
   private BankSampahService bss;
   private ArrayList<Complain> daftarComplain;

   // Komponen UI
   private JTextField judulField;
   private JTextArea isiArea;
   private JButton submitButton;
   private JTable riwayatTable;
   private DefaultTableModel tableModel;

   public KeluhanPanel(Penyetor user) {
      this.user = user;
      this.bss = new BankSampahService();
      this.cs = new ComplainService();
      this.bank = bss.getObjBankSampah(user.getIdBankSampah());
      daftarComplain = cs.daftarComplain(bank);

      initUI();
      loadRiwayatKeluhan();
   }

   private void initUI() {
      setLayout(new BorderLayout(20, 20));
      setBackground(Color.WHITE);
      setBorder(new EmptyBorder(20, 20, 20, 20));

      // Header
      JLabel header = new JLabel("Kirim Keluhan");
      header.setFont(new Font("Segoe UI", Font.BOLD, 22));
      header.setHorizontalAlignment(SwingConstants.CENTER);
      add(header, BorderLayout.NORTH);

      // Panel utama
      JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
      mainPanel.setBackground(Color.WHITE);
      add(mainPanel, BorderLayout.CENTER);

      // Form keluhan
      JPanel formPanel = new JPanel();
      formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
      formPanel.setBackground(Color.WHITE);
      formPanel.setBorder(BorderFactory.createTitledBorder("Form Keluhan"));

      // Judul
      JLabel judulLabel = new JLabel("Judul Keluhan:");
      judulLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      formPanel.add(judulLabel);
      formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

      judulField = new JTextField();
      judulField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
      formPanel.add(judulField);
      formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

      // Isi
      JLabel isiLabel = new JLabel("Isi Keluhan:");
      isiLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      formPanel.add(isiLabel);
      formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

      isiArea = new JTextArea(6, 30);
      isiArea.setLineWrap(true);
      isiArea.setWrapStyleWord(true);
      JScrollPane scrollPane = new JScrollPane(isiArea);
      formPanel.add(scrollPane);
      formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

      // Tombol Submit
      submitButton = new JButton("Submit Keluhan");
      submitButton.setBackground(new Color(0, 128, 0));
      submitButton.setForeground(Color.WHITE);
      submitButton.setFocusPainted(false);
      submitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
      submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
      submitButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            tambahComplain();
         }
      });
      formPanel.add(submitButton);

      mainPanel.add(formPanel, BorderLayout.NORTH);

      // Panel riwayat
      JPanel riwayatPanel = new JPanel(new BorderLayout());
      riwayatPanel.setBackground(Color.WHITE);
      riwayatPanel.setBorder(BorderFactory.createTitledBorder("Riwayat Keluhan Anda"));

      tableModel = new DefaultTableModel(new Object[] { "ID Complain", "Judul", "Isi", "Status" , "Tanggapan"}, 0) {
         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // read-only
         }
      };

      riwayatTable = new JTable(tableModel);
      riwayatTable.setFillsViewportHeight(true);
      riwayatTable.setRowHeight(25);
      riwayatTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
      riwayatTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

      JScrollPane tableScroll = new JScrollPane(riwayatTable);
      riwayatPanel.add(tableScroll, BorderLayout.CENTER);

      mainPanel.add(riwayatPanel, BorderLayout.CENTER);
   }

   private void tambahComplain() {
      String judul = judulField.getText().trim();
      String isi = isiArea.getText().trim();

      if (judul.isEmpty() || isi.isEmpty()) {
         JOptionPane.showMessageDialog(this, "Judul dan isi keluhan tidak boleh kosong!", "Error",
               JOptionPane.ERROR_MESSAGE);
         return;
      }

      String idUser = user.getIdPenyetor();
      String idBank = user.getIdBankSampah();
      String idComplain = DatabaseComplain.generateComplainId();

      Complain complainBaru = new Complain(idComplain, idUser, idBank, judul, isi);
      daftarComplain.add(complainBaru);

      // Simpan ke database
      DatabaseComplain.writeData(daftarComplain, bank.getFileComplain());
      DatabaseComplain.writeData(daftarComplain);

      JOptionPane.showMessageDialog(this, "Keluhan berhasil dikirim!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

      // Reset form
      judulField.setText("");
      isiArea.setText("");

      // Refresh tabel
      loadRiwayatKeluhan();
   }

   private void loadRiwayatKeluhan() {
      tableModel.setRowCount(0); // hapus data lama
      for (Complain c : daftarComplain) {
         if (c.getIdPenyetor().equals(user.getIdPenyetor())) {
            tableModel.addRow(new Object[] { c.getIdComplain(), c.getJudul(), c.getIsi(), c.getStatus(), c.getTanggapanAdmin() });
         }
      }
   }
}
