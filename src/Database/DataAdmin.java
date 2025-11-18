package Database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import Model.Admin;

public class DataAdmin {

    String delim = "\\|";

    public ArrayList<Admin> loadData(String filepath) {
        ArrayList<Admin> dataAdmin = new ArrayList<>();
        File file = new File(filepath);

        try {
            if (!file.exists()) {
                throw new IOException("File" + filepath + "tidak ada");
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()).trim() != null) {
                String[] parts = line.split(delim);
                if (parts.length >= 4) {
                    dataAdmin.add(new Admin(parts[0], parts[1], parts[2], parts[3]));
                }
            }
            return dataAdmin;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return dataAdmin;
        }
    }
}
