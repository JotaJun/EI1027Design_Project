package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.LegalGuardian;
import es.uji.ei1027.SgOviProject.model.PapPati;
import es.uji.ei1027.SgOviProject.services.IntAccountRegisterSvc;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.standard.expression.LessOrEqualToExpression;

@Controller
@RequestMapping("/legalGuardian")
public class LegalGuardianController {
    @Autowired
    private IntAccountRegisterSvc registerService;


    @GetMapping("/register")
    public String legalGuardianRegister(Model model, HttpSession session) {

        Account account = (Account) session.getAttribute("pendingAccount");
        if(account == null) {
            return "redirect:/register";
        }
        model.addAttribute("guardianAccount", new LegalGuardian());
        return "legalGuardian/register";
    }

    @PostMapping("/register")
    public String doLegalGuardianRegister(@ModelAttribute("guardianAccount") LegalGuardian guardian,
                                          BindingResult bindingResult,
                                          HttpSession session) {

        LegalGuardianValidator guardianValidator = new LegalGuardianValidator();
        guardianValidator.validate(guardian, bindingResult);

        if (bindingResult.hasErrors()) {
            return "legalGuardian/register";
        }

        Account account = (Account) session.getAttribute("pendingAccount");


        registerService.addLegalGuardian(account, guardian);
        session.removeAttribute("pendingAccount");
        session.removeAttribute("chosenType");

        return "redirect:/register/done";
    }
}
