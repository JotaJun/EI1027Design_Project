package es.uji.ei1027.SgOviProject.controller;


import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String listAccounts(Model model) {
        model.addAttribute("accounts", accountDao.getAccounts());
        return "account/list";
    }

    /* Operaciones para añadir */

    @RequestMapping(value="add")
    public String addAccount(Model model) {
        model.addAttribute("account", new Account());
        return "account/add";
    }

    /* Operaciones para actualizar */

    @RequestMapping(value="/update/{name}")
    public String editAccount(@PathVariable String name, Model model) {
        model.addAttribute("account", accountDao.getAccount(name));
        return "account/update";
    }

    /* Operaciones para borrar */

    @RequestMapping(value="/delete/{name}")
    public String deleteAccount(@PathVariable String name,String dni, Model model) {
        accountDao.deleteAccount(dni);
        return "redirect:/account/list";
    }
}
