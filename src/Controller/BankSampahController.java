package Controller;

import Model.BankSampah;
import Service.BankSampahService;


public class BankSampahController {
    private static BankSampahService bankSampahService = new BankSampahService();

    public BankSampahController() {
    }

    public static BankSampah getBankSampah(String IdBankSampah) {
        return bankSampahService.getObjBankSampah(IdBankSampah);
    }
}
