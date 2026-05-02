package es.uji.ei1027.SgOviProject.controller;


import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.PapPati;
import es.uji.ei1027.SgOviProject.services.IntAccountSvc;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/papPati")
public class PapPatiController {
    @Autowired
    private IntAccountSvc registerService;


    @GetMapping("/register")
    public String mostrarFormulariPap(Model model, HttpSession session) {

        Account account = (Account) session.getAttribute("pendingAccount");
        if(account == null) {
            return "redirect:/register";
        }
        model.addAttribute("papData", new PapPati());
        return "papPati/register";
    }

    @PostMapping("/register")
    public String processarRegistrePap(@ModelAttribute("papData") PapPati papPati,
                                       BindingResult bindingResult,
                                       HttpSession session) {

        PapPatiValidator papValidator = new PapPatiValidator();
        papValidator.validate(papPati, bindingResult);

        if (bindingResult.hasErrors()) {
            return "papPati/register";
        }

        Account account = (Account) session.getAttribute("pendingAccount");


        registerService.addPapPati(account, papPati);
        session.removeAttribute("pendingAccount");
        session.removeAttribute("chosenType");

        return "redirect:/register/done";
    }
}