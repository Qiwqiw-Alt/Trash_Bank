package Database;

import Model.Complain;
import Model.Complain.Status; // Pastikan import enum Status

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseComplain {
    private static final String DATA_COMPLAIN_GLOBAL = "src\\Database\\Complain\\datacomplain.txt";
    private static final String DELIM = "\\|";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static String generateComplainId() {
        ArrayList<Complain> list = loadData();

        int max = 0;
        for (Complain c : list) {
            try {
                String angka = c.getIdComplain().substring(2);
                int num = Integer.parseInt(angka);
                if (num > max)
                    max = num;
            } catch (Exception e) {
                continue;
            }
        }
        return String.format("CP%03d", max + 1);
    }

    public static ArrayList<Complain> loadData() {
        return loadData(DATA_COMPLAIN_GLOBAL);
    }

    // --- LOAD DATA (Core - Stateless) ---
    public static ArrayList<Complain> loadData(String filePath) {
        ArrayList<Complain> listHasil = new ArrayList<>();
        File file = new File(filePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (!file.exists()) {
                file.createNewFile();
                return listHasil;
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                String[] data = line.split(DELIM); // Pakai \\|

                if (data.length >= 8) {
                    Complain c = new Complain(
                            data[0], // id complain
                            data[1], // id penyetor
                            data[2], // id bank
                            data[3], // judul
                            data[4] // isi
                    );

                    try {
                        c.setTanggal(LocalDate.parse(data[5], FORMATTER));
                    } catch (Exception e) {
                        c.setTanggal(LocalDate.now());
                    }
                    try {
                        c.setStatus(Status.valueOf(data[6]));
                    } catch (Exception e) {
                        c.setStatus(Status.PENDING);
                    }

                    if (data[7].equalsIgnoreCase("null")) {
                        c.setTanggapanAdmin("-");
                    } else {
                        c.setTanggapanAdmin(data[7]);
                    }

                    listHasil.add(c);
                }
            }
        } catch (IOException e) {
            System.out.println("Error Load Complain: " + e.getMessage());
        }
        return listHasil;
    }

    public static void writeData(List<Complain> listComplain) {
        writeData(listComplain, DATA_COMPLAIN_GLOBAL);
    }

    public static void writeData(List<Complain> listComplain, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {

            for (Complain c : listComplain) {
                String tglStr = c.getTanggal().format(FORMATTER);

                String tanggapan = (c.getTanggapanAdmin() == null || c.getTanggapanAdmin().isEmpty())
                        ? "null"
                        : c.getTanggapanAdmin();

                String line = c.getIdComplain() + "|" +
                        c.getIdPenyetor() + "|" +
                        c.getIdBank() + "|" +
                        c.getJudul() + "|" +
                        c.getIsi() + "|" +
                        tglStr + "|" +
                        c.getStatus().name() + "|" + 
                        tanggapan;

                bw.write(line);
                bw.newLine();
            }
            System.out.println("Data Complain tersimpan di: " + filePath);

        } catch (IOException e) {
            System.out.println("Error Write Complain: " + e.getMessage());
        }
    }

    public static void addComplain(Complain complainBaru, String filePath) {
        ArrayList<Complain> list = loadData(filePath);
        list.add(complainBaru);
        writeData(list, filePath);
    }
}