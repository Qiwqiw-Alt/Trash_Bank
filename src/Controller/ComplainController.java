package Controller;

import Service.ComplainService;

public class ComplainController {
    private static ComplainService service = new ComplainService(); 

    public ComplainController() {
        
    }

    public static ComplainService getService() {
        return service;
    }
}
