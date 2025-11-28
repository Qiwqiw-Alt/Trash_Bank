package Controller;

import Service.ListMemberService;

public class ListMemberController {
     private static ListMemberService service = new ListMemberService(); 

    public ListMemberController() {
        // Kosongkan atau hapus constructor ini jika tidak dipakai
    }

    public static ListMemberService getService() {
        return service;
    }
}
