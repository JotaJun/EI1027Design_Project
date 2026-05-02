package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.enums.AccountType;
import es.uji.ei1027.SgOviProject.model.*;
import es.uji.ei1027.SgOviProject.services.IntAccountSvc;
import jakarta.servlet.http.HttpSession;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.Period;

@Controller

public class RegisterController {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private IntAccountSvc registerService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("account", new Account());
        return "register";
    }

    @PostMapping("/register")
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

        // ENCRIPTACIÓN DE LA CONTRASEÑA
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(account.getPassword());
        account.setPassword(encryptedPassword);

        session.setAttribute("pendingAccount", account);
        session.setAttribute("chosenType", accountType);

        switch (accountType) {
            case OVIUSER:
                if (account.getBirthday() != null) {
                    LocalDate now = LocalDate.now();
                    Period period = Period.between(account.getBirthday(), now);
                    if (period.getYears() >= 18) {

                        registerService.addOviUser(account, new OviUser());
                        return "redirect:/register/done";
                    }
                }
                return "redirect:/oviuser/register";

            case PAPPATI:
                return "redirect:/pappati/register";

            case LEGALGUARDIAN:
                return "redirect:/guardian/register";

            default:
                return "register";
        }

    }

    @GetMapping("/register/done")
    public String done(HttpSession session) {
        session.invalidate();
        return "register_done";
    }
}
