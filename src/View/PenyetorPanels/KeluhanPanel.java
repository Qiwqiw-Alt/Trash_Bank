package View.PenyetorPanels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
      header.setFont(new Font("Segoe UI", Font.BOLD, 26));
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
      formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Form Keluhan"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));

      // Judul
      JLabel judulLabel = new JLabel("Judul Keluhan:");
      judulLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      formPanel.add(judulLabel);
      formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

      judulField = new JTextField();
      judulField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      judulField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
      judulField.setBorder(BorderFactory.createCompoundBorder(
            judulField.getBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
      formPanel.add(judulField);
      formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

      // Isi
      JLabel isiLabel = new JLabel("Isi Keluhan:");
      isiLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      formPanel.add(isiLabel);
      formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

      isiArea = new JTextArea(6, 30);
      isiArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      isiArea.setLineWrap(true);
      isiArea.setWrapStyleWord(true);
      isiArea.setBorder(BorderFactory.createCompoundBorder(
            isiArea.getBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
      JScrollPane scrollPane = new JScrollPane(isiArea);
      scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
      formPanel.add(scrollPane);
      formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

      // Tombol Submit
      submitButton = new JButton("Submit Keluhan");
      submitButton.setBackground(new Color(0, 128, 0));
      submitButton.setForeground(Color.WHITE);
      submitButton.setFocusPainted(false);
      submitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
      submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
      submitButton.setPreferredSize(new Dimension(150, 35));
      submitButton.addActionListener(e -> tambahComplain());
      formPanel.add(submitButton);

      mainPanel.add(formPanel, BorderLayout.NORTH);

      // Panel riwayat
      JPanel riwayatPanel = new JPanel(new BorderLayout());
      riwayatPanel.setBackground(Color.WHITE);
      riwayatPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Riwayat Keluhan Anda"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));

      tableModel = new DefaultTableModel(new Object[] { "ID Complain", "Judul", "Isi", "Status", "Tanggapan" }, 0) {
         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // read-only
         }
      };

      riwayatTable = new JTable(tableModel);
      riwayatTable.setFillsViewportHeight(true);
      riwayatTable.setRowHeight(30);
      riwayatTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
      riwayatTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

      for (int i = 0; i < riwayatTable.getColumnCount(); i++) {
         riwayatTable.getColumnModel().getColumn(i).setCellRenderer(new TextAreaRenderer());
      }

      JScrollPane tableScroll = new JScrollPane(riwayatTable);
      tableScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
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
      daftarComplain = DatabaseComplain.loadData(bank.getFileComplain());
      for (Complain c : daftarComplain) {
         if (c.getIdPenyetor().equals(user.getIdPenyetor())) {
            tableModel.addRow(
                  new Object[] { c.getIdComplain(), c.getJudul(), c.getIsi(), c.getStatus(), c.getTanggapanAdmin() });
         }
      }
   }
}

class TextAreaRenderer extends JTextArea implements javax.swing.table.TableCellRenderer {
   public TextAreaRenderer() {
      setLineWrap(true);
      setWrapStyleWord(true);
      setOpaque(true);
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value,
         boolean isSelected, boolean hasFocus, int row, int column) {

      setText(value != null ? value.toString() : "");
      setFont(table.getFont());

      // Mengatur warna background & foreground ketika diseleksi
      if (isSelected) {
         setBackground(table.getSelectionBackground());
         setForeground(table.getSelectionForeground());
      } else {
         setBackground(table.getBackground());
         setForeground(table.getForeground());
      }

      // Menyesuaikan tinggi row
      int tableWidth = table.getColumnModel().getColumn(column).getWidth();
      setSize(new Dimension(tableWidth, Short.MAX_VALUE));
      int preferredHeight = getPreferredSize().height;
      if (table.getRowHeight(row) != preferredHeight) {
         table.setRowHeight(row, preferredHeight);
      }

      return this;
   }
}
