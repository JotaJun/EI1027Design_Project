package es.uji.ei1027.SgOviProject.controller;


import es.uji.ei1027.SgOviProject.dao.PapPatiDao;
import es.uji.ei1027.SgOviProject.model.PapPati;
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
public class PapPatiController {

    @Autowired
    private PapPatiDao papPatiDao;


    @GetMapping("/registre") //seria ("/register/pappati")
    public String mostrarFormulariPap(Model model, HttpSession session) {

        String dni = (String) session.getAttribute("dniEnRegistro"); //seria creo que: session.getAttribute("pendingAccount").getDni()

        if (dni == null) {
            return "redirect:/login"; //seria redirect:/register diria
        }

        // Preparamos el objeto y le inyectamos el DNI
        //esto ya lo hace la interfaz e implentacion de serviceRegister mira mi codigo o el de juan tb te recomiendo que lo hagas abajo
        //porque puedes acceder a pendingAccount en el httpsession que viene de mi pagina de registro
        PapPati papPati = new PapPati();
        papPati.setDni(dni);

        model.addAttribute("papData", papPati);
        return "registre_pappati";
    }

    @PostMapping("/registre")
    public String processarRegistrePap(@ModelAttribute("papData") PapPati papPati,
                                       BindingResult bindingResult,
                                       HttpSession session) {

        PapPatiValidator papValidator = new PapPatiValidator();
        papValidator.validate(papPati, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registre_pappati";
        }

        papPatiDao.addPapPati(papPati);

        return "redirect:/postregistro"; //aqui: "/register/pappati/done"
    }

    //
    @GetMapping("/register/pappati/done")
    public String done() {
        return "register_done";
    }
}