package es.uji.ei1027.SgOviProject.controller;


import es.uji.ei1027.SgOviProject.dao.PapPatiDao;
import es.uji.ei1027.SgOviProject.model.Account;
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

@Controller
@RequestMapping("/papPati")
public class PapPatiController {

    @Autowired
    private PapPatiDao papPatiDao;
    @Autowired
    private IntAccountRegisterSvc registerService;


    @GetMapping("/register") //seria ("/register/pappati")
    public String mostrarFormulariPap(Model model, HttpSession session) {

        Account account = (Account) session.getAttribute("pendingAccount"); //seria creo que: session.getAttribute("pendingAccount").getDni()

        if(account == null) {
            return "redirect:/register";
        }
        
        model.addAttribute("papData", new PapPati());

        // Preparamos el objeto y le inyectamos el DNI
        //esto ya lo hace la interfaz e implentacion de serviceRegister mira mi codigo o el de juan tb te recomiendo que lo hagas abajo
        //porque puedes acceder a pendingAccount en el httpsession que viene de mi pagina de registro


        return "pappati/register";
    }

    @PostMapping("/register")
    public String processarRegistrePap(@ModelAttribute("papData") PapPati papPati,
                                       BindingResult bindingResult,
                                       HttpSession session) {

        PapPatiValidator papValidator = new PapPatiValidator();
        papValidator.validate(papPati, bindingResult);

        if (bindingResult.hasErrors()) {
            return "pappati/register";
        }

        Account account = (Account) session.getAttribute("pendingAccount"); //seria creo que: session.getAttribute("pendingAccount").getDni()


        registerService.addPapPati(account, papPati);
        session.removeAttribute("pendingAccount");
        session.removeAttribute("chosenType");

        return "redirect:/register/done"; //aqui: "/register/pappati/done"
    }
}