package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/account")
public class AccountController {

    private int pageLength = 5;
    private AccountDao accountDao;

    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }


    @RequestMapping("/list")
    public String listAccounts(Model model) {
        model.addAttribute("accounts", accountDao.getAccounts());
        return "account/list";
    }


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

    @RequestMapping(value="/update/{dni}")
    public String editAccount(@PathVariable String dni, Model model) {
        model.addAttribute("account", accountDao.getAccount(dni));
        return "account/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("account") Account account, BindingResult result) {
        if (result.hasErrors()) {
            return "account/update";
        }
        accountDao.updateAccount(account);
        return "redirect:/account/list";
    }

    @RequestMapping(value="/delete/{dni}")
    public String processDelete(@PathVariable String dni, Model model) {
        accountDao.deleteAccount(dni);
        return "redirect:/account/list";
    }

    @GetMapping(value="/pendingAccounts")
    public String listPendingAccounts(Model model,
                                      @RequestParam("page") Optional<Integer> page) {

        List<Account> allPending = accountDao.getPendingAccounts();

        // Crear la lista paginada
        ArrayList<ArrayList<Account>> accountsPaged = new ArrayList<>();
        int ini = 0;
        int fin = pageLength;

        while (fin <= allPending.size()) {
            accountsPaged.add(new ArrayList<>(allPending.subList(ini, fin)));
            ini += pageLength;
            fin += pageLength;
        }
        if (ini < allPending.size()) {
            accountsPaged.add(new ArrayList<>(allPending.subList(ini, allPending.size())));
        }

        // Crear la barra de navegación de páginas
        int totalPages = accountsPaged.size();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        int currentPage = page.orElse(0);
        if (totalPages > 0) {
            if (currentPage < 0) currentPage = 0;
            if (currentPage >= totalPages) currentPage = totalPages - 1;
        } else {
            currentPage = 0;
        }

        model.addAttribute("accountsPaged", accountsPaged);
        model.addAttribute("selectedPage", currentPage);
        model.addAttribute("totalAccounts", allPending.size());
        model.addAttribute("pageLength", pageLength);

        return "account/pendingAccounts";
    }
}
