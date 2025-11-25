package Database;

import Model.Complain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DatabaseComplain {
    private ArrayList<Complain> listComplains;
    private static final String DATA_COMPLAIN = "src/Database/Complain/datacomplain.txt";
    private String delim = "|";

    public void addComplain(Complain complainBaru) {
        listComplains.add(complainBaru);
    }

    public ArrayList<Complain> getAllDaftarComplain() {
        return listComplains;
    }

    public ArrayList<Complain> loadData() {
        listComplains.clear();
        File file = new File(DATA_COMPLAIN);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (!file.exists()) {
                throw new IOException("File Complain Belum ada");
            }

            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                String[] data = line.split(delim);

                if (data.length >= 8) {
                    Complain baru = new Complain(
                                    data[0], //id complain
                                    data[1], // id penyetor
                                    data[2], // id bank
                                    data[3], // judul
                                    data[4]); // isi
                    baru.setTanggal(LocalDate.parse(data[5], format));
                    Complain.Status status = Complain.Status.valueOf(data[6]);
                    baru.setTanggapanAdmin(data[7]);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error : " + e.getMessage());
        }
        return getAllDaftarComplain();
    }

}
