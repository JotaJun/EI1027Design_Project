package es.uji.ei1027.SgOviProject.controller;


import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("account") Account account, BindingResult result) {
        if (result.hasErrors()) {
            return "account/add";
        }
        accountDao.addAccount(account);
        return "redirect:/account/list";

    }

    /* Operaciones para actualizar */

    @RequestMapping(value="/update/{dni}")
    public String editAccount(@PathVariable String dni, Model model) {
        model.addAttribute("account", accountDao.getAccount(dni));
        return "account/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.GET)
    public String processUpdateSubmit(@ModelAttribute("account") Account account, BindingResult result) {
        if (result.hasErrors()) {
            return "account/update";
        }
        accountDao.updateAccount(account);
        return "redirect:/account/list";
    }

    /* Operaciones para borrar */

    @RequestMapping(value="/delete/{dni}")
    public String processDelete(@PathVariable String dni, Model model) {
        accountDao.deleteAccount(dni);
        return "redirect:/account/list";
    }
}
