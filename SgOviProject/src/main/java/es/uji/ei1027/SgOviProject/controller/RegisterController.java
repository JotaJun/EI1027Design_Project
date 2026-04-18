package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.enums.AccountType;
import es.uji.ei1027.SgOviProject.enums.Status;
import es.uji.ei1027.SgOviProject.model.*;
import es.uji.ei1027.SgOviProject.services.IntAccountRegisterSvc;
import jakarta.servlet.http.HttpSession;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller

public class RegisterController {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private IntAccountRegisterSvc registerService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("account", new Account());
        return "register";
    }

    @PostMapping("/register") //se puede hacer con requestmapping peor parece ser que en versiones nuevas de spring se usa esto
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
        String encriptedPassword = passwordEncryptor.encryptPassword(account.getPassword());
        account.setPassword(encriptedPassword);

        // COMENTARIO JUAN: no hace falta esto, ya que en la clase Account por defecto ya es PENDING, así nos quitamos líos de cabeza
        // account.setStatus(Status.PENDING);
        session.setAttribute("pendingAccount", account);
        session.setAttribute("chosenType", accountType);

        switch (accountType) {
            case OVIUSER:
                registerService.addOviUser(account, new OviUser());

                //el registro de oviuser lo gestionamos de momento ais para probar que va bien el register_done.html, pero
                //tiene registro especifico como los otros dos
                session.removeAttribute("pendingAccount");
                session.removeAttribute("chosenType");
                return "redirect:/register/done";


            case PAPPATI:
                return "redirect:/pappati/register";

            case LEGALGUARDIAN:
                return "redirect:/register/legalguardian";

            default:
                return "register";
        }

    }

    @GetMapping("/register/done")
    public String done() {
        return "register_done"; 
    }
}
