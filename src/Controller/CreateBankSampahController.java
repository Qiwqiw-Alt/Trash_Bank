package Controller;

import Service.CreateBankSampahService;

public class CreateBankSampahController {
    private static CreateBankSampahService service = new CreateBankSampahService();
    
    public CreateBankSampahController(){

    }

    public static CreateBankSampahService getService(){
        return service;
    }
}
