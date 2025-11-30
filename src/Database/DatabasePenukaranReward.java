package Database;

import Model.PenukaranReward;
import Model.Penyetor;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabasePenukaranReward {
    private static final String DATA_PENUKARAN_REWARD_GLOBAL = "src\\Database\\DataPenukaranReward\\dataPenukaran.txt";
    private static final String DELIM = "\\|";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static String getFinalPath() {
        return DATA_PENUKARAN_REWARD_GLOBAL;
    }


    public static String generatePenukaranId() {
        ArrayList<PenukaranReward> list = loadData();

        int max = 0;
        for (PenukaranReward pr : list) {
            try {
                String angka = pr.getIdPenukaran().substring(2);
                int num = Integer.parseInt(angka);
                if (num > max)
                    max = num;
            } catch (Exception e) {
                continue;
            }
        }
        return String.format("PR%03d", max + 1);
    }

    public static ArrayList<PenukaranReward> loadData() {
        return loadData(DATA_PENUKARAN_REWARD_GLOBAL);
    }


    public static ArrayList<PenukaranReward> loadData(String filePath) {
        ArrayList<PenukaranReward> listHasil = new ArrayList<>();
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

                String[] data = line.split(DELIM);
                if (data.length >= 5) {
                    PenukaranReward baru = new PenukaranReward(
                            data[0], // id penukaran
                            data[1], // id reward
                            data[2] // id penyetor
                    );

                    try {
                        baru.setTanggalPenukaran(LocalDate.parse(data[3], FORMATTER));
                    } catch (Exception e) {
                        baru.setTanggalPenukaran(LocalDate.now());
                    }
                    try {
                        double poin = Double.parseDouble(data[4]);
                        baru.setPoint(poin);
                    } catch (Exception e) {
                        System.out.println("Error Membaca file Reward : " + e.getMessage() + ", format salah");
                    }

                    listHasil.add(baru);
                }
            }
        } catch (IOException e) {
            System.out.println("Error Load Penukaran: " + e.getMessage());
        }
        return listHasil;
    }

    public static void writeData(List<PenukaranReward> listPenukaran) {
        writeData(listPenukaran, DATA_PENUKARAN_REWARD_GLOBAL);
    }

    public static void writeData(List<PenukaranReward> listPenukaran, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {

            for (PenukaranReward pr : listPenukaran) {
                String tglStr = pr.getFormattedTime();

                bw.write(
                        pr.getIdPenukaran() + "|" +
                                pr.getIdReward() + "|" +
                                pr.getIdPenyetor() + "|" +
                                tglStr);
                bw.newLine();
            }
            System.out.println("Data Penukaran tersimpan di: " + filePath);

        } catch (IOException e) {
            System.out.println("Error Write Penukaran: " + e.getMessage());
        }
    }

    public static void addPenukaran(PenukaranReward transaksiBaru) {
        addPenukaran(transaksiBaru, DATA_PENUKARAN_REWARD_GLOBAL);
    }

    public static void addPenukaran(PenukaranReward transaksiBaru, String filePath) {
        ArrayList<PenukaranReward> list = loadData(filePath);
        list.add(transaksiBaru);
        writeData(list, filePath);
    }

    public static double hitungPoinPenyetor(Penyetor user) {
        ArrayList<PenukaranReward> list = loadData(); 
        double total = 0;

        for (PenukaranReward t : list) {
            if (t.getIdPenyetor().equals(user.getIdPenyetor()) ) {
                total += t.getPoin();
            }
        }

        return total;
    }
}