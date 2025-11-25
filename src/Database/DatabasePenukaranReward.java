package Database;

import Model.PenukaranReward;
import Model.Reward;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DatabasePenukaranReward {
    private ArrayList<PenukaranReward> listTransaksiPenukaranReward;
    private static final String DATA_PENUKARAN_REWARD = "src/Database/DataPenukaranReward/dataPenukaran.txt";
    private final String delim = "\\|";

    public void addReward(PenukaranReward transaksiPenukaranBaru) {
        listTransaksiPenukaranReward.add(transaksiPenukaranBaru);
        saveData();
    }

    public ArrayList<PenukaranReward> loadData() {
        listTransaksiPenukaranReward.clear();
        File file = new File(DATA_PENUKARAN_REWARD);

        if (!file.exists()) {
            return listTransaksiPenukaranReward;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                String[] data = line.split(delim);
                if (data.length >= 4) {

                    PenukaranReward baru = new PenukaranReward(
                            data[0], //id penukaran
                            data[1], // id reward
                            data[2]); // id penyetor
                    baru.setTanggalPenukaran(LocalDate.parse(data[3], format)); // tanggal penukaran
                }
            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
        return getDaftarPenukaranReward();
    }

    public void saveData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_PENUKARAN_REWARD))) {
            for (PenukaranReward penukaranReward : listTransaksiPenukaranReward) {
                bw.write(
                    penukaranReward.getIdPenukaran() + "|" +
                    penukaranReward.getIdReward() + "|" + 
                    penukaranReward.getIdPenyetor() + "|" + 
                    penukaranReward.getFormattedTime() + "\n"
                );
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error : " + e.getMessage());
        }
    }

    public ArrayList<PenukaranReward> getDaftarPenukaranReward() {
        return listTransaksiPenukaranReward;
    }

}
