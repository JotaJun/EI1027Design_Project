package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.dao.LegalGuardianDao;
import es.uji.ei1027.SgOviProject.dao.OviUserDao;
import es.uji.ei1027.SgOviProject.dao.PapPatiDao;
import es.uji.ei1027.SgOviProject.dto.AccountWithTypeDTO;
import es.uji.ei1027.SgOviProject.enums.AccountType;
import es.uji.ei1027.SgOviProject.enums.Status;
import es.uji.ei1027.SgOviProject.filters.AccountTypeFilter;
import es.uji.ei1027.SgOviProject.filters.StatusFilter;
import es.uji.ei1027.SgOviProject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
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

    @GetMapping(value = {"/pendingAccounts", "/pendingAccounts/{type}"})
    public String listPendingAccounts(Model model,
                                      @PathVariable(required = false) String type,
                                      @RequestParam("page") Optional<Integer> page,
                                      jakarta.servlet.http.HttpSession session) {

        if (type == null) type = "Tots";

        // 1. Obtener todas las cuentas pendientes y enriquecerlas con su tipo
        List<Account> allPending = accountDao.getPendingAccounts();
        List<AccountWithTypeDTO> allWithType = new ArrayList<>();

        for (Account account : allPending) {
            AccountType accountType;
            if (papPatiDao.getPapPati(account.getDni()) != null) {
                accountType = AccountType.PAPPATI;
            } else if (oviUserDao.getOviUser(account.getDni()) != null) {
                accountType = AccountType.OVIUSER;
            } else {
                accountType = AccountType.LEGALGUARDIAN;
            }
            allWithType.add(new AccountWithTypeDTO(account, accountType));
        }

        // 2. Aplicar filtro por tipo
        List<AccountWithTypeDTO> filtered;
        if (type.equals("Tots")) {
            filtered = allWithType;
        } else {
            final String typeFinal = type;
            filtered = allWithType.stream()
                    .filter(dto -> dto.getAccountType().name().equals(typeFinal))
                    .collect(Collectors.toList());
        }

        // 3. Paginar
        ArrayList<ArrayList<AccountWithTypeDTO>> accountsPaged = new ArrayList<>();
        int ini = 0;
        int fin = pageLength;

        while (fin <= filtered.size()) {
            accountsPaged.add(new ArrayList<>(filtered.subList(ini, fin)));
            ini += pageLength;
            fin += pageLength;
        }
        if (ini < filtered.size()) {
            accountsPaged.add(new ArrayList<>(filtered.subList(ini, filtered.size())));
        }

        // 4. Números de página
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

        // 5. Preparar filtro para la vista
        AccountTypeFilter filter = new AccountTypeFilter();
        filter.setTypeSel(type);

        model.addAttribute("accountTypeFilter", filter);
        model.addAttribute("accountTypes", Arrays.stream(AccountType.values())
                .filter(t -> t != AccountType.TECHNICIAN)
                .collect(Collectors.toList()));
        model.addAttribute("accountsPaged", accountsPaged);
        model.addAttribute("selectedPage", currentPage);
        model.addAttribute("totalAccounts", filtered.size());
        model.addAttribute("pageLength", pageLength);
        model.addAttribute("selectedType", type);

        session.setAttribute("lastAccountListUrl", "/account/pendingAccounts/" + type + "?page=" + currentPage);

        return "account/pendingAccounts";
    }

    @PostMapping(value = "/pendingAccounts")
    public String processTypeFilter(@ModelAttribute("accountTypeFilter") AccountTypeFilter filter) {
        return "redirect:/account/pendingAccounts/" + filter.getTypeSel();
    }

    @GetMapping(value = {"/allAccounts", "/allAccounts/{type}", "/allAccounts/{type}/{status}"})
    public String listAllAccounts(Model model,
                                  @PathVariable(required = false) String type,
                                  @PathVariable(required = false) String status,
                                  @RequestParam("page") Optional<Integer> page,
                                  jakarta.servlet.http.HttpSession session) {

        if (type == null) type = "Tots";
        if (status == null) status = "Tots";

        // 1. Todas las cuentas con su tipo detectado
        List<Account> all = accountDao.getAccounts();
        List<AccountWithTypeDTO> allWithType = new ArrayList<>();

        for (Account account : all) {
            AccountType accountType;
            if (papPatiDao.getPapPati(account.getDni()) != null) {
                accountType = AccountType.PAPPATI;
            } else if (oviUserDao.getOviUser(account.getDni()) != null) {
                accountType = AccountType.OVIUSER;
            } else {
                accountType = AccountType.LEGALGUARDIAN;
            }
            allWithType.add(new AccountWithTypeDTO(account, accountType));
        }

        // 2. Aplicar filtros
        final String typeFinal = type;
        final String statusFinal = status;
        List<AccountWithTypeDTO> filtered = allWithType.stream()
                .filter(dto -> typeFinal.equals("Tots") || dto.getAccountType().name().equals(typeFinal))
                .filter(dto -> statusFinal.equals("Tots") || dto.getAccount().getStatus().name().equals(statusFinal))
                .collect(Collectors.toList());

        // 3. Paginar
        ArrayList<ArrayList<AccountWithTypeDTO>> accountsPaged = new ArrayList<>();
        int ini = 0;
        int fin = pageLength;

        while (fin <= filtered.size()) {
            accountsPaged.add(new ArrayList<>(filtered.subList(ini, fin)));
            ini += pageLength;
            fin += pageLength;
        }
        if (ini < filtered.size()) {
            accountsPaged.add(new ArrayList<>(filtered.subList(ini, filtered.size())));
        }

        // 4. Números de página
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

        // 5. Preparar filtros para la vista
        AccountTypeFilter typeFilter = new AccountTypeFilter();
        typeFilter.setTypeSel(type);
        StatusFilter statusFilter = new StatusFilter();
        statusFilter.setStatusSel(status);

        model.addAttribute("accountTypeFilter", typeFilter);
        model.addAttribute("statusFilter", statusFilter);
        model.addAttribute("accountTypes", Arrays.stream(AccountType.values())
                .filter(t -> t != AccountType.TECHNICIAN)
                .collect(Collectors.toList()));
        model.addAttribute("accountStatuses", Arrays.asList(Status.values()));
        model.addAttribute("accountsPaged", accountsPaged);
        model.addAttribute("selectedPage", currentPage);
        model.addAttribute("totalAccounts", filtered.size());
        model.addAttribute("pageLength", pageLength);
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedStatus", status);

        session.setAttribute("lastAccountListUrl", "/account/allAccounts/" + type + "/" + status + "?page=" + currentPage);

        return "account/allAccounts";
    }

    @PostMapping(value = "/allAccounts")
    public String processAllAccountsFilter(@ModelAttribute("accountTypeFilter") AccountTypeFilter typeFilter,
                                           @ModelAttribute("statusFilter") StatusFilter statusFilter) {
        return "redirect:/account/allAccounts/" + typeFilter.getTypeSel() + "/" + statusFilter.getStatusSel();
    }

    @GetMapping(value = "/details/{dni}")
    public String detailsAccount(@PathVariable String dni,
                                 @RequestParam(required = false, defaultValue = "false") boolean readOnly,
                                 Model model) {
        Account account = accountDao.getAccount(dni);
        model.addAttribute("account", account);
        model.addAttribute("readOnly", readOnly);

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
