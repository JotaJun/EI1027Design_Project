package es.uji.ei1027.SgOviProject.controller;


import es.uji.ei1027.SgOviProject.dao.*;
import es.uji.ei1027.SgOviProject.enums.AccountType;
import es.uji.ei1027.SgOviProject.model.*;
import es.uji.ei1027.SgOviProject.services.IntLoginService;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TechnicianDao technicianDao;

    @Autowired
    private IntLoginService loginService;

    @ModelAttribute("accountTypes")
    public List<AccountType> accountTypes() {
        return Arrays.asList(AccountType.values());
    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("details", new LoginDetails());
        return "login";
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String checkLogin(@ModelAttribute("details") LoginDetails details,
                             BindingResult bindingResult, HttpSession session) {
        LoginValidator loginValidator = new LoginValidator();
        loginValidator.validate(details, bindingResult);
        if (bindingResult.hasErrors()) {
            return "login";
        }

        String targetUrl; // URL por defecto

        // Comprobar tipo login (tecnico distinto)
        if (details.getAccountType() == AccountType.TECHNICIAN) {

            Technician technician = technicianDao.getTechnicianByLoginDetails(details);
            if (technician == null) {
                bindingResult.reject("badCredentials", "El correu o la contrasenya són incorrectes");
                return "login";
            }
            // Autenticados como técnico
            session.setAttribute("technician", technician);

            targetUrl = "redirect:/technician/main";

        } else {

            Account account = accountDao.getAccountByLoginDetails(details); // devuelve cuenta sin contraseña
            if (account == null) {
                bindingResult.reject("badCredentials", "El correu o la contrasenya són incorrectes");
                return "login";
            }

            // Servicio comprueba que el tipo + cuenta coinciden
            Object specificAccount = loginService.authenticate(details, account.getDni());
            if (specificAccount == null){
                bindingResult.rejectValue("accountType", "wrongAccountType", "Tipus de compte incorrecte");
                return "login";
            }

            session.setAttribute("account", account);

            // Configuramos la ruta de destino según el rol y agregamos la cuenta
            switch (details.getAccountType()){
                case OVIUSER :
                    targetUrl = "redirect:/oviUser/main";
                    OviUser oviUser = (OviUser) specificAccount;
                    session.setAttribute("specificAccount", oviUser);
                    break;
                case PAPPATI:
                    targetUrl = "redirect:/papPati/main";
                    PapPati papPati = (PapPati) specificAccount;
                    session.setAttribute("specificAccount", papPati);
                    break;
                case LEGALGUARDIAN:
                    targetUrl = "redirect:/legalGuardian/main";
                    LegalGuardian legalGuardian = (LegalGuardian) specificAccount;
                    session.setAttribute("specificAccount", legalGuardian);
                    break;
                default:
                    return "login";
            }
        }

        session.setAttribute("userRole", details.getAccountType().name());  // Guardar rol para identificar en html más fácil

        // GESTIÓN DEL NEXT-URL (Tiene prioridad sobre el targetUrl configurado arriba)
        String nextUrl = (String) session.getAttribute("nextUrl");
        if (nextUrl != null) {
            session.removeAttribute("nextUrl");
            return "redirect:" + nextUrl;
        }

        return targetUrl;
    }


    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
