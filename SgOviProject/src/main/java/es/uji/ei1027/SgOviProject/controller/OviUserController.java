package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.OviUser;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
@RequestMapping("/oviuser")
public class OviUserController {

    @Autowired
    private IntAccountRegisterSvc registerService;

    @RequestMapping("/main")
    public String oviUserMain(Model model) {

        // Generar la fecha para la vista
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM 'de' yyyy", new Locale("ca", "ES"));
        String dateNow = LocalDate.now().format(formatter).toUpperCase();

        model.addAttribute("dateNow", dateNow);

        return "oviuser/main";
    }

    @GetMapping("/register")
    public String oviUserRegister(Model model, HttpSession session) {

        Account account = (Account) session.getAttribute("pendingAccount");
        if(account == null) {
            return "redirect:/register";
        }
        model.addAttribute("oviAccount", new OviUser());
        return "oviuser/register";
    }

    @PostMapping("/register")
    public String doOviUserRegister(@ModelAttribute("oviAccount") OviUser oviUser,
                                       BindingResult bindingResult,
                                       HttpSession session) {

        OviUserValidator oviValidator = new OviUserValidator();
        oviValidator.validate(oviUser, bindingResult);

        if (bindingResult.hasErrors()) {
            return "oviuser/register";
        }

        Account account = (Account) session.getAttribute("pendingAccount");


        registerService.addOviUser(account, oviUser);
        session.removeAttribute("pendingAccount");
        session.removeAttribute("chosenType");

        return "redirect:/register/done";
    }



}