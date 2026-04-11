package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dao.OviUserDao;
import es.uji.ei1027.SgOviProject.model.OviUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
@RequestMapping("/oviUser")
public class OviUserController {

    @Autowired
    private OviUserDao oviUserDao;

    @RequestMapping("/main")
    public String oviUserMain(HttpSession session, Model model) {
        if (session.getAttribute("account") == null || !"OVIUSER".equals(session.getAttribute("userRole"))) {
            return "redirect:/login"; // Si entra sin loguearse, lo echamos al login
        }

        // Generar la fecha para la vista
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM 'de' yyyy", new Locale("ca", "ES"));
        String dateNow = LocalDate.now().format(formatter).toUpperCase();

        model.addAttribute("dateNow", dateNow);

        return "oviUser/main";
    }


}