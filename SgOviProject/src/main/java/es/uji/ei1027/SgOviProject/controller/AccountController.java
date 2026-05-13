package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.dao.LegalGuardianDao;
import es.uji.ei1027.SgOviProject.dao.OviUserDao;
import es.uji.ei1027.SgOviProject.dao.PapPatiDao;
import es.uji.ei1027.SgOviProject.enums.Status;
import es.uji.ei1027.SgOviProject.model.*;
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
    private OviUserDao oviUserDao;

    @Autowired
    private PapPatiDao papPatiDao;

    @Autowired
    private LegalGuardianDao legalGuardianDao;

    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }




    @RequestMapping("/list")
    public String listAccounts(Model model) {
        model.addAttribute("accounts", accountDao.getAccounts());
        return "account/list";
    }

    @RequestMapping(value = "add")
    public String addAccount(Model model) {
        model.addAttribute("account", new Account());
        return "account/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("account") Account account, BindingResult result) {
        if (result.hasErrors()) {
            return "account/add";
        }
        accountDao.addAccount(account);
        return "redirect:/account/list";
    }

    @RequestMapping(value = "/update/{dni}")
    public String editAccount(@PathVariable String dni, Model model) {
        model.addAttribute("account", accountDao.getAccount(dni));
        return "account/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("account") Account account, BindingResult result) {
        if (result.hasErrors()) {
            return "account/update";
        }
        accountDao.updateAccount(account);
        return "redirect:/account/list";
    }

    @RequestMapping(value = "/delete/{dni}")
    public String processDelete(@PathVariable String dni, Model model) {
        accountDao.deleteAccount(dni);
        return "redirect:/account/list";
    }

    @GetMapping(value = "/pendingAccounts")
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

    @GetMapping(value = "/details/{dni}")
    public String detailsAccount(@PathVariable String dni, Model model) {
        Account account = accountDao.getAccount(dni);
        model.addAttribute("account", account);

        // Detectar el rol del solicitante
        PapPati papPati = papPatiDao.getPapPati(dni);
        if (papPati != null) {
            model.addAttribute("papPati", papPati);
            model.addAttribute("role", "PAPPATI");
            return "account/details";
        }

        OviUser oviUser = oviUserDao.getOviUser(dni);
        if (oviUser != null) {
            model.addAttribute("oviUser", oviUser);
            model.addAttribute("role", "OVIUSER");
            // Si tiene tutor legal asociado, recuperar también sus datos
            if (oviUser.getDniLegalGuardian() != null) {
                Account guardianAccount = accountDao.getAccount(oviUser.getDniLegalGuardian());
                model.addAttribute("guardianAccount", guardianAccount);
            }
            return "account/details";
        }

        // Si no es PapPati ni OviUser, es tutor legal
        model.addAttribute("role", "LEGALGUARDIAN");
        return "account/details";
    }

    @GetMapping(value = "/approve/{dni}")
    public String approveAccount(@PathVariable String dni, Model model) {
        Account account = accountDao.getAccount(dni);
        account.setStatus(Status.ACCEPTED);
        accountDao.updateAccount(account);
        model.addAttribute("result", "approved");
        return "account/done";
    }

    @GetMapping(value = "/denyReason/{dni}")
    public String denyReasonForm(@PathVariable String dni, Model model) {
        Account account = accountDao.getAccount(dni);
        model.addAttribute("account", account);
        return "account/denyReason";
    }

    @PostMapping(value = "/denyReason")
    public String processDenyReason(@ModelAttribute("account") Account account, Model model) {
        Account existing = accountDao.getAccount(account.getDni());
        existing.setStatus(Status.REJECTED);
        existing.setDeniedReason(account.getDeniedReason());
        accountDao.updateAccount(existing);
        model.addAttribute("result", "rejected");
        return "account/done";
    }
}
