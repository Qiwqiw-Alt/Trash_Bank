package Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PenukaranReward {

    private String idPenukaran;
    private String idReward;
    private String idPenyetor;
    private LocalDate tanggal;

    public PenukaranReward() {}

    public PenukaranReward(String idPenukaran, String idReward, String idPenyetor) {
        this.idPenukaran = idPenukaran;
        this.idReward = idReward;
        this.idPenyetor = idPenyetor;
        this.tanggal = LocalDate.now();
    }

    public String getIdPenukaran() { return idPenukaran; }
    public String getIdReward() { return idReward; }
    public String getIdPenyetor() { return idPenyetor; }
    public LocalDate getTanggal() { return tanggal; }
    public void setTanggalPenukaran(LocalDate time) {
        this.tanggal = time;
    }

    public String getFormattedTime() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return tanggal.format(format);
    }
}
