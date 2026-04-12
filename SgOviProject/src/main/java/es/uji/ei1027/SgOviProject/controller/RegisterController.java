package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.enums.AccountType;
import es.uji.ei1027.SgOviProject.enums.Status;
import es.uji.ei1027.SgOviProject.model.*;
import es.uji.ei1027.SgOviProject.services.IntAccountRegisterSvc;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class RegisterController {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private IntAccountRegisterSvc registerService;

    @RequestMapping("/register")
    public String register(Model model) {
        model.addAttribute("account", new Account());
        return "register";
    }

    @RequestMapping(value="/register", method=RequestMethod.POST)
    public String doRegister(@ModelAttribute("account") Account account, BindingResult bindingResult, @RequestParam("type") AccountType accountType, HttpSession session) {
        RegisterValidator registerValidator = new RegisterValidator();
        registerValidator.validate(account, bindingResult);

        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (accountDao.getAccount(account.getDni()) != null) {
            bindingResult.rejectValue("dni", "cloned", "Aquest DNI ja està registrat en el sistema");
            return "register";
        }

        if (accountDao.getAccount(account.getEmail()) != null) {
            bindingResult.rejectValue("email", "cloned", "Aquest email ja està registrat en el sistema");
            return "register";
        }

        account.setStatus(Status.PENDING);
        session.setAttribute("pendingAccount", account);
        session.setAttribute("chosenType", accountType);

        switch (accountType) {
            case OVIUSER:
                registerService.addOviUser(account, new OviUser());
                session.removeAttribute("pendingAccount");
                session.removeAttribute("chosenType");
                return "redirect:/register/oviuser/done";

            case PAPPATI:
                return "redirect:/register/pappati";

            case LEGALGUARDIAN:
                return "redirect:/register/legalguardian";

            default:
                return "register";
        }

    }

    @RequestMapping("/register/oviuser/done")
    public String done() {
        return "register_done"; 
    }
}
