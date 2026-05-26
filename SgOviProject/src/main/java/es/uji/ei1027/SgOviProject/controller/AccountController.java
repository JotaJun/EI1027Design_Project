package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.dao.AssistanceRequestDao;
import es.uji.ei1027.SgOviProject.dao.CandidacyDao;
import es.uji.ei1027.SgOviProject.dao.LegalGuardianDao;
import es.uji.ei1027.SgOviProject.dao.OviUserDao;
import es.uji.ei1027.SgOviProject.dao.PapPatiDao;
import es.uji.ei1027.SgOviProject.dto.AccountWithTypeDTO;
import es.uji.ei1027.SgOviProject.dto.CandidacyDTO;
import es.uji.ei1027.SgOviProject.enums.AccountType;
import es.uji.ei1027.SgOviProject.enums.Status;
import es.uji.ei1027.SgOviProject.filters.AccountTypeFilter;
import es.uji.ei1027.SgOviProject.filters.StatusFilter;
import es.uji.ei1027.SgOviProject.model.*;
import es.uji.ei1027.SgOviProject.services.CandidacyService;
import org.jasypt.util.password.BasicPasswordEncryptor;
import jakarta.servlet.http.HttpSession;
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
    private AssistanceRequestDao assistanceRequestDao;

    @Autowired
    private CandidacyService candidacyService;

    @Autowired
    private CandidacyDao candidacyDao;

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

    @GetMapping(value = "/approve/{dni}")
    public String approveAccount(@PathVariable String dni, Model model, HttpSession session) {
        String userRole = (String) session.getAttribute("userRole");
        if (!"TECHNICIAN".equals(userRole)) {
            throw new es.uji.ei1027.SgOviProject.exception.SgOviException(
                    "Només el personal tècnic pot aprovar comptes", "Error 403 - Sense permisos");
        }

        Account account = accountDao.getAccount(dni);
        account.setStatus(Status.ACCEPTED);
        accountDao.updateAccount(account);
        model.addAttribute("result", "approved");
        return "account/done";
    }

    @RequestMapping(value = "/update/{dni}")
    public String editAccount(@PathVariable String dni, Model model) {
        model.addAttribute("account", accountDao.getAccount(dni));
        return "account/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("account") Account account,
            @RequestParam(value = "signatureCode", required = false) String signatureCode,
            BindingResult result,
            HttpSession session,
            Model model) {
        if (result.hasErrors()) {
            return "account/update";
        }

        String userRole = (String) session.getAttribute("userRole");

        // Validación de la firma si es un Tutor
        if ("LEGALGUARDIAN".equals(userRole)) {
            LegalGuardian lgSession = (LegalGuardian) session.getAttribute("specificAccount");
            // Recuperamos el tutor de la BD porque el de la sesión suele tener la firma a null por seguridad
            LegalGuardian lg = legalGuardianDao.getLegalGuardian(lgSession.getDni());
            
            if (signatureCode == null || signatureCode.trim().isEmpty()) {
                model.addAttribute("signatureError", "Has d'introduir el codi de firma per a confirmar els canvis.");
                return "account/update";
            }

            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            boolean isValid = false;
            try {
                isValid = passwordEncryptor.checkPassword(signatureCode, lg.getSignatureCode());
            } catch (Exception e) {
                // Si la firma en la BD no está encriptada (datos de prueba), probamos comparación directa
                isValid = signatureCode.equals(lg.getSignatureCode());
            }

            if (!isValid) {
                model.addAttribute("signatureError", "El codi de firma no és correcte.");
                return "account/update";
            }
        }

        // Recuperem el compte existent per no perdre dades que no estan al formulari
        // (com la contrasenya o l'estat)
        Account existingAccount = accountDao.getAccount(account.getDni());
        account.setPassword(existingAccount.getPassword());

        if (account.getStatus() == null) {
            account.setStatus(existingAccount.getStatus());
        }

        if (account.getDeniedReason() == null) {
            account.setDeniedReason(existingAccount.getDeniedReason());
        }

        accountDao.updateAccount(account);

        if ("LEGALGUARDIAN".equals(userRole)) {
            return "redirect:/account/wardDetails/" + account.getDni();
        }

        return "redirect:/account/list";
    }

    @RequestMapping(value = "/delete/{dni}")
    public String processDelete(@PathVariable String dni, Model model) {
        accountDao.deleteAccount(dni);
        return "redirect:/account/list";
    }

    @GetMapping(value = { "/wardList", "/wardList/{status}" })
    public String listWardedAccounts(Model model,
            @PathVariable(required = false) String status,
            @RequestParam("page") Optional<Integer> page,
            HttpSession session) {

        Account currentUser = (Account) session.getAttribute("account");
        String dni = currentUser.getDni();

        if (status == null)
            status = "Tots";

        // Obtener los oviUsers
        List<OviUser> wardedUsers = oviUserDao.getWardedOviUsers(dni);
        List<AccountWithTypeDTO> allWithType = new ArrayList<>();

        // preguntar a lledo si usar esto o un dao en account que recupere lista a
        // paritr de uan lsita de ovis, los dos usan for asi que ns que es mas eficiente
        for (OviUser oviUser : wardedUsers) {
            Account account = accountDao.getAccount(oviUser.getDni());
            if (account != null) {
                allWithType.add(new AccountWithTypeDTO(account, AccountType.OVIUSER));
            }
        }

        // filtros
        final String statusFinal = status;
        List<AccountWithTypeDTO> filtered = allWithType.stream()
                .filter(dto -> statusFinal.equals("Tots") || dto.getAccount().getStatus().name().equals(statusFinal))
                .collect(Collectors.toList());

        // Paginar
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

        // Números de página
        int totalPages = accountsPaged.size();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        int currentPage = page.orElse(0);
        if (totalPages > 0) {
            if (currentPage < 0)
                currentPage = 0;
            if (currentPage >= totalPages)
                currentPage = totalPages - 1;
        } else {
            currentPage = 0;
        }

        // 5. Preparar filtros para la vista
        StatusFilter statusFilter = new StatusFilter();
        statusFilter.setStatusSel(status);

        model.addAttribute("statusFilter", statusFilter);
        model.addAttribute("accountStatuses", Arrays.asList(Status.values()));
        model.addAttribute("accountsPaged", accountsPaged);
        model.addAttribute("selectedPage", currentPage);
        model.addAttribute("totalAccounts", filtered.size());
        model.addAttribute("pageLength", pageLength);
        model.addAttribute("selectedStatus", status);

        session.setAttribute("lastAccountListUrl", "/account/wardList/" + status + "?page=" + currentPage);

        return "account/wardList";
    }

    @PostMapping(value = "/wardList")
    public String processWardListFilter(@ModelAttribute("statusFilter") StatusFilter statusFilter) {
        return "redirect:/account/wardList/" + statusFilter.getStatusSel();
    }

    @GetMapping(value = { "/pendingAccounts", "/pendingAccounts/{type}" })
    public String listPendingAccounts(Model model,
            @PathVariable(required = false) String type,
            @RequestParam("page") Optional<Integer> page,
            jakarta.servlet.http.HttpSession session) {

        if (type == null)
            type = "Tots";

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
            if (currentPage < 0)
                currentPage = 0;
            if (currentPage >= totalPages)
                currentPage = totalPages - 1;
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

    @GetMapping(value = { "/allAccounts", "/allAccounts/{type}", "/allAccounts/{type}/{status}" })
    public String listAllAccounts(Model model,
            @PathVariable(required = false) String type,
            @PathVariable(required = false) String status,
            @RequestParam("page") Optional<Integer> page,
            jakarta.servlet.http.HttpSession session) {

        if (type == null)
            type = "Tots";
        if (status == null)
            status = "Tots";

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
            if (currentPage < 0)
                currentPage = 0;
            if (currentPage >= totalPages)
                currentPage = totalPages - 1;
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

        session.setAttribute("lastAccountListUrl",
                "/account/allAccounts/" + type + "/" + status + "?page=" + currentPage);

        return "account/allAccounts";
    }

    @PostMapping(value = "/allAccounts")
    public String processAllAccountsFilter(@ModelAttribute("accountTypeFilter") AccountTypeFilter typeFilter,
            @ModelAttribute("statusFilter") StatusFilter statusFilter) {
        return "redirect:/account/allAccounts/" + typeFilter.getTypeSel() + "/" + statusFilter.getStatusSel();
    }

    @GetMapping(value = { "/details/{dni}", "/wardDetails/{dni}" })
    public String detailsAccount(@PathVariable String dni,
            @RequestParam(required = false, defaultValue = "false") boolean readOnly,
            Model model,
            HttpSession session) {

        String userRole = (String) session.getAttribute("userRole");
        Account currentUser = (Account) session.getAttribute("account");

        String viewName = "account/details";

        // Seguridad: Si es un tutor legal, validar que el DNI pertenece a su tutelado
        if ("LEGALGUARDIAN".equals(userRole)) {
            viewName = "account/wardDetails";
            OviUser oviUser = oviUserDao.getOviUser(dni);
            if (oviUser == null || !currentUser.getDni().equals(oviUser.getDniLegalGuardian())) {
                throw new es.uji.ei1027.SgOviProject.exception.SgOviException(
                        "No tens permisos per veure els detalls d'aquest compte", "Error 403 - Sense permisos");
            }
            session.setAttribute("fromAccount", true);
            session.setAttribute("fromAccountDni", dni);
        }

        Account account = accountDao.getAccount(dni);
        model.addAttribute("account", account);
        model.addAttribute("readOnly", readOnly);

        // Detectar el rol del solicitante
        PapPati papPati = papPatiDao.getPapPati(dni);
        if (papPati != null) {
            model.addAttribute("papPati", papPati);
            model.addAttribute("role", "PAPPATI");
            return viewName;
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
            return viewName;
        }

        // Si no es PapPati ni OviUser, es tutor legal
        return viewName;
    }

    @GetMapping(value = "/denyReason/{dni}")
    public String denyReasonForm(@PathVariable String dni, Model model, HttpSession session) {
        String userRole = (String) session.getAttribute("userRole");
        if (!"TECHNICIAN".equals(userRole)) {
            throw new es.uji.ei1027.SgOviProject.exception.SgOviException(
                    "Només el personal tècnic pot denegar comptes", "Error 403 - Sense permisos");
        }

        Account account = accountDao.getAccount(dni);
        model.addAttribute("account", account);
        return "account/denyReason";
    }

    @PostMapping(value = "/denyReason")
    public String processDenyReason(@ModelAttribute("account") Account account, Model model, HttpSession session) {
        String userRole = (String) session.getAttribute("userRole");
        if (!"TECHNICIAN".equals(userRole)) {
            throw new es.uji.ei1027.SgOviProject.exception.SgOviException(
                    "Només el personal tècnic pot denegar comptes", "Error 403 - Sense permisos");
        }

        Account existing = accountDao.getAccount(account.getDni());
        existing.setStatus(Status.REJECTED);
        existing.setDeniedReason(account.getDeniedReason());
        accountDao.updateAccount(existing);
        model.addAttribute("result", "rejected");
        return "account/done";
    }

    // ===== BAIXA DE COMPTE (tutor legal) =====

    @GetMapping(value = "/deactivateWard/{dni}")
    public String deactivateWardForm(@PathVariable String dni, Model model, HttpSession session) {
        String userRole = (String) session.getAttribute("userRole");
        Account currentUser = (Account) session.getAttribute("account");

        if (!"LEGALGUARDIAN".equals(userRole)) {
            throw new es.uji.ei1027.SgOviProject.exception.SgOviException(
                    "Només els tutors legals poden donar de baixa un compte d'usuari OVI",
                    "Error 403 - Sense permisos");
        }

        // Verificar que el usuario OVI le pertenece
        OviUser oviUser = oviUserDao.getOviUser(dni);
        if (oviUser == null || !currentUser.getDni().equals(oviUser.getDniLegalGuardian())) {
            throw new es.uji.ei1027.SgOviProject.exception.SgOviException(
                    "No tens permisos per donar de baixa aquest compte", "Error 403 - Sense permisos");
        }

        Account account = accountDao.getAccount(dni);
        model.addAttribute("account", account);
        return "account/deactivateWard";
    }

    @PostMapping(value = "/deactivateWard")
    public String processDeactivateWard(@ModelAttribute("account") Account account, Model model, HttpSession session) {
        String userRole = (String) session.getAttribute("userRole");
        Account currentUser = (Account) session.getAttribute("account");

        if (!"LEGALGUARDIAN".equals(userRole)) {
            throw new es.uji.ei1027.SgOviProject.exception.SgOviException(
                    "Només els tutors legals poden donar de baixa un compte", "Error 403 - Sense permisos");
        }

        // Verificar pertenencia
        OviUser oviUser = oviUserDao.getOviUser(account.getDni());
        if (oviUser == null || !currentUser.getDni().equals(oviUser.getDniLegalGuardian())) {
            throw new es.uji.ei1027.SgOviProject.exception.SgOviException(
                    "No tens permisos per donar de baixa aquest compte", "Error 403 - Sense permisos");
        }

        Account existing = accountDao.getAccount(account.getDni());

        // Validación backend del motivo
        if (account.getDeniedReason() == null || account.getDeniedReason().trim().isEmpty()) {
            model.addAttribute("account", existing);
            model.addAttribute("error", "Has d'indicar un motiu per a la baixa");
            return "account/deactivateWard";
        }

        existing.setStatus(Status.REJECTED);
        existing.setDeniedReason(account.getDeniedReason());
        accountDao.updateAccount(existing);

        model.addAttribute("result", "rejected"); // Podríamos crear un 'deactivated' si hubiera un done específico,
                                                  // pero reutilizamos el done de rejected
        return "account/done";
    }

    // ===== ELIMINACIÓ FÍSICA D'UN COMPTE (tècnic) =====

    @GetMapping(value = "/deleteReason/{dni}")
    public String deleteReasonForm(@PathVariable String dni, Model model, HttpSession session) {
        String userRole = (String) session.getAttribute("userRole");
        if (!"TECHNICIAN".equals(userRole)) {
            throw new es.uji.ei1027.SgOviProject.exception.SgOviException(
                    "Només el personal tècnic pot eliminar comptes", "Error 403 - Sense permisos");
        }

        Account account = accountDao.getAccount(dni);
        model.addAttribute("account", account);
        return "account/deleteReason";
    }

    @PostMapping(value = "/deleteReason")
    public String processDeleteReason(@ModelAttribute("account") Account account, HttpSession session) {
        String userRole = (String) session.getAttribute("userRole");
        if (!"TECHNICIAN".equals(userRole)) {
            throw new es.uji.ei1027.SgOviProject.exception.SgOviException(
                    "Només el personal tècnic pot eliminar comptes", "Error 403 - Sense permisos");
        }

        accountDao.deleteAccount(account.getDni());
        return "redirect:/account/allAccounts";
    }

    // ===== HISTORIAL D'AR PER OVIUSER (tècnic) =====

    @GetMapping(value = "/apHistory/{dni}")
    public String apHistory(@PathVariable String dni, Model model,
            jakarta.servlet.http.HttpSession session) {
        Account account = accountDao.getAccount(dni);
        model.addAttribute("account", account);

        List<AssistanceRequest> requests = assistanceRequestDao.getAssistanceRequestsByDni(dni);
        model.addAttribute("requests", requests);

        session.setAttribute("lastApHistoryDni", dni);
        return "account/apHistory";
    }

    // ===== HISTORIAL DE CANDIDATURES PER PAPPATI (tècnic) =====

    @GetMapping(value = "/candidacyHistory/{dni}")
    public String candidacyHistory(@PathVariable String dni, Model model,
            jakarta.servlet.http.HttpSession session) {
        Account account = accountDao.getAccount(dni);
        model.addAttribute("account", account);

        List<Candidacy> rawCandidacies = candidacyDao.getCandidaciesByDniPapPati(dni);
        List<CandidacyDTO> candidacyDTOs = new ArrayList<>();
        for (Candidacy c : rawCandidacies) {
            candidacyDTOs.add(candidacyService.getCandidacyDetailByIdCandidacy(c.getIdCandidacy()));
        }
        model.addAttribute("candidacies", candidacyDTOs);

        session.setAttribute("lastCandidacyHistoryDni", dni);
        return "account/candidacyHistory";
    }

}
