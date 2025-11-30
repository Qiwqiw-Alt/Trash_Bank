package Controller;

import Service.ListMemberService;

public class ListMemberController {
     private static ListMemberService service = new ListMemberService(); 

    public ListMemberController() {
    }

    public static ListMemberService getService() {
        return service;
    }
}
