package Controller;

import Model.BankSampah;
import Service.BankSampahService;


public class BankSampahController {
    private static BankSampahService service = new BankSampahService();

    public BankSampahController() {
    }

    public static BankSampahService getService() {
        return service;
    }
}
