package es.uji.ei1027.SgOviProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
@RequestMapping("/technician")
public class TechnicianController {

    @GetMapping("/main")
    public String main(Model model) {

        // Generar la fecha para la vista
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM 'de' yyyy", new Locale("ca", "ES"));
        String dateNow = LocalDate.now().format(formatter).toUpperCase();

        model.addAttribute("dateNow", dateNow);

        return "technician/main";
    }
}
