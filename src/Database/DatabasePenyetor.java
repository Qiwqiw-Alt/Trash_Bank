package Database;

import Model.Admin;
import Model.Penyetor;

import java.io.*;
import java.util.ArrayList;

public class DatabasePenyetor {
    private static String DELIM = "\\|";
    private static final String DATA_PENYETOR_GLOBAL = "src\\Database\\Penyetor\\dataSemuaPenyetor.txt";

    public static String generatePenyetorId() {
        ArrayList<Penyetor> allData = loadData(DATA_PENYETOR_GLOBAL);
        int max = 0;

        for (Penyetor p : allData) {
            try {
                String idStr = p.getIdPenyetor().substring(2); // Ambil angka setelah "UP"
                int num = Integer.parseInt(idStr);
                if (num > max)
                    max = num;
            } catch (NumberFormatException e) {
                continue;
            }
        }

        return String.format("UP%03d", max + 1); // UP001, UP002, dst
    }

    public static ArrayList<Penyetor> loadData() {
        return loadData(DATA_PENYETOR_GLOBAL);
    }

    public static ArrayList<Penyetor> loadData(String filePath) {
        ArrayList<Penyetor> listHasil = new ArrayList<>();
        File file = new File(filePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try {
            if (!file.exists()) {
                System.out.println("File tidak ditemukan, membuat file baru.");
                file.createNewFile();
                return listHasil;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                String[] parts = line.split(DELIM);
                if (parts.length >= 6) {
                    Penyetor penyetorBaru = new Penyetor(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);

                    if (!parts[6].equalsIgnoreCase("null")) {
                        penyetorBaru.setIdBankSampah(parts[6]);
                    }
                    listHasil.add(penyetorBaru);
                }
            }
            br.close();

        } catch (IOException e) {
            System.out.println("Error Load Data: " + e.getMessage());
        }
        return listHasil;
    }

    // Tulis data
    public static void writeData(ArrayList<Penyetor> listData, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Penyetor p : listData) {
                String idBank = (p.getIdBankSampah() == null) ? "null" : p.getIdBankSampah();

                String line = p.getIdPenyetor() + "|" +
                        p.getRole() + "|" +
                        p.getUsername() + "|" +
                        p.getPassword() + "|" +
                        p.getNamaLengkap() + "|" +
                        p.getNoHp() + "|" +
                        idBank;

                writer.write(line);
                writer.newLine();
            }
            System.out.println("Data berhasil disimpan ke: " + filePath);
        } catch (IOException e) {
            System.err.println("Error Write Data: " + e.getMessage());
        }
    }

    public static void writeData(ArrayList<Penyetor> listData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_PENYETOR_GLOBAL))) {
            for (Penyetor p : listData) {
                String idBank = (p.getIdBankSampah() == null) ? "null" : p.getIdBankSampah();

                String line = p.getIdPenyetor() + "|" +
                        p.getRole() + "|" +
                        p.getUsername() + "|" +
                        p.getPassword() + "|" +
                        p.getNamaLengkap() + "|" +
                        p.getNoHp() + "|" +
                        idBank;

                writer.write(line);
                writer.newLine();
            }
            System.out.println("Data berhasil disimpan ke: " + DATA_PENYETOR_GLOBAL);
        } catch (IOException e) {
            System.err.println("Error Write Data: " + e.getMessage());
        }
    }

    public static void addPenyetor(Penyetor penyetorBaru, String filePath) {
        ArrayList<Penyetor> currentList = loadData(filePath);
        currentList.add(penyetorBaru);
        writeData(currentList, filePath);
    }

    public static boolean assignUserToBank(String userId, String idBank) {
        ArrayList<Penyetor> globalList = loadData(DATA_PENYETOR_GLOBAL); // Load global data
        boolean found = false;

        for (Penyetor p : globalList) {
            if (p.getIdPenyetor().equals(userId)) {
                p.setIdBankSampah(idBank);
                found = true;
                break;
            }
        }

        if (found) {
            writeData(globalList, DATA_PENYETOR_GLOBAL);
            return true;
        }
        return false;
    }

    public static void updatePenyetor(Penyetor userUpdate, String filePath) {
        ArrayList<Penyetor> list = loadData(filePath);
        boolean found = false;

        for (int i = 0; i < list.size(); i++) {
            // Cari admin berdasarkan ID, update datanya
            if (list.get(i).getIdPenyetor().equals(userUpdate.getIdPenyetor())) {
                list.set(i, userUpdate);
                found = true;
                break;
            }
        }

        if (found) {
            writeData(list, filePath);
        }
    }

    public static void updatePenyetorGlobal(Penyetor user) {
        ArrayList<Penyetor> listData = loadData();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_PENYETOR_GLOBAL))) {
            for (Penyetor p : listData) {

                if (p.getIdPenyetor().equals(user.getIdPenyetor())) {
                    String idBank = (user.getIdBankSampah() == null) ? "null" : user.getIdBankSampah();

                    String line = user.getIdPenyetor() + "|" +
                            user.getRole() + "|" +
                            user.getUsername() + "|" +
                            user.getPassword() + "|" +
                            user.getNamaLengkap() + "|" +
                            user.getNoHp() + "|" +
                            idBank;

                    writer.write(line);
                    writer.newLine();
                } else {
                    String idBank = (p.getIdBankSampah() == null) ? "null" : p.getIdBankSampah();

                    String line = p.getIdPenyetor() + "|" +
                            p.getRole() + "|" +
                            p.getUsername() + "|" +
                            p.getPassword() + "|" +
                            p.getNamaLengkap() + "|" +
                            p.getNoHp() + "|" +
                            idBank;

                    writer.write(line);
                    writer.newLine();
                }
            }
            System.out.println("Data berhasil disimpan ke: " + DATA_PENYETOR_GLOBAL);
        } catch (IOException e) {
            System.err.println("Error Write Data: " + e.getMessage());
        }

    }

}
