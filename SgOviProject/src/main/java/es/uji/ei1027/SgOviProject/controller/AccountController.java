package es.uji.ei1027.SgOviProject.controller;


import es.uji.ei1027.SgOviProject.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {
    private AccountDao accountDao;

    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /* Operaciones para listar */

    @RequestMapping("/list")
    public String listAccount(Model model) {
        return "account/list";
    }
}
