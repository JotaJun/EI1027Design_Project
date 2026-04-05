package es.uji.ei1027.SgOviProject.controller;


import es.uji.ei1027.SgOviProject.model.Account;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;

@Controller
public class LoginController {


    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("account", new Account());
        return "login";
    }
/*
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String checkLogin(@ModelAttribute("user") UserDetails user,
                             BindingResult bindingResult, HttpSession session) {
        UserValidator userValidator = new UserValidator();
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "login";
        }

        // Comprova que el login siga correcte intentant carregar les dades
        user = userDao.loadUserByUsername(user.getUsername(),
                user.getPassword());
        if (user == null) {
            bindingResult.rejectValue("password", "badpw",
                    "Contrasenya incorrecta");
            return "login";
        }

        // Autenticats correctament.
        session.setAttribute("user", user);

        // Ejercicio 3: Comprobar si existe nextUrl en la sesión
        String nextUrl = (String) session.getAttribute("nextUrl");
        if (nextUrl != null) {
            session.removeAttribute("nextUrl"); // Eliminar el atributo para evitar confusiones
            return "redirect:" + nextUrl;   // NECESARIO usar redirect para hacer la petición correcta.
        }

        // Torna a la pàgina principal si no hay nextUrl
        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

 */
}
