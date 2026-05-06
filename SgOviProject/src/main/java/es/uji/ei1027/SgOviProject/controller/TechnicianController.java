package es.uji.ei1027.SgOviProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/technician")
public class TechnicianController {

    @GetMapping("/main")
    public String main() {
        return "technician/main";
    }
}
