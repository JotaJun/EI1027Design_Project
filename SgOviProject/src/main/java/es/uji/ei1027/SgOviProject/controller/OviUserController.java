package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.dao.LegalGuardianDao;
import es.uji.ei1027.SgOviProject.dto.OviUserUpdateDTO;
import es.uji.ei1027.SgOviProject.enums.Gender;
import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.OviUser;
import es.uji.ei1027.SgOviProject.services.IntAccountSvc;
import jakarta.servlet.http.HttpSession;
import org.jasypt.util.password.BasicPasswordEncryptor;
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
import java.util.Arrays;
import java.util.Locale;

@Controller
@RequestMapping("/oviUser")
public class OviUserController {

    @Autowired
    private IntAccountSvc accountSvc;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private LegalGuardianDao legalGuardianDao;

    @RequestMapping("/main")
    public String oviUserMain(Model model) {

        // Generar la fecha para la vista
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM 'de' yyyy", new Locale("ca", "ES"));
        String dateNow = LocalDate.now().format(formatter).toUpperCase();

        model.addAttribute("dateNow", dateNow);

        return "oviUser/main";
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
            return "oviUser/register";
        }

        Account account = (Account) session.getAttribute("pendingAccount");


        accountSvc.addOviUser(account, oviUser);
        session.removeAttribute("pendingAccount");
        session.removeAttribute("chosenType");

        return "redirect:/register/done";
    }

    @GetMapping("/details")
    public String watchDetails(Model model, HttpSession session){
        //El interceptor ya verifica que el usuario está loggeado

        Account account = (Account) session.getAttribute("account");
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        model.addAttribute("account", account);
        model.addAttribute("oviUser", currentUser);

        return "oviUser/details";
    }


    @GetMapping("/update")
    public String updateOviUser(Model model, HttpSession session) {

        //El interceptor ya verifica que el usuario está loggeado

        Account account = (Account) session.getAttribute("account");
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        OviUserUpdateDTO updateForm = new OviUserUpdateDTO(account, currentUser);

        model.addAttribute("updateForm", updateForm);
        model.addAttribute("genderList", Gender.values());

        return "oviUser/update";
    }

    @PostMapping("/update")
    public String processUpdateOviUser(Model model,
                                       @ModelAttribute("updateForm") OviUserUpdateDTO form,
                                       BindingResult bindingResult,
                                       HttpSession session) {

        Account account = (Account) session.getAttribute("account");
        OviUser oviUser = (OviUser) session.getAttribute("specificAccount");

        OviUserUpdateDTOValidator validator = new OviUserUpdateDTOValidator();
        validator.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("genderList", Gender.values());
            return "oviUser/update";
        }

        // Ponemos los dnis y status para que no aparezcan en el html, ya que ya está registrado
        form.getAccount().setDni(account.getDni());
        form.getAccount().setStatus(account.getStatus());
        form.getOviUser().setDni(oviUser.getDni());

        if (form.getNewPassword() != null && !form.getNewPassword().trim().isEmpty()) {
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            String encryptedPassword = passwordEncryptor.encryptPassword(form.getNewPassword());
            form.getAccount().setPassword(encryptedPassword);
        } else {
            account = accountDao.getAccount(account.getDni());  // cogemos la cuenta con contraseña de la BD
            form.getAccount().setPassword(account.getPassword());
        }

        // Comprobar que existe tutor en base de datos

        String dniTutor = form.getOviUser().getDniLegalGuardian();

        if (dniTutor != null && !dniTutor.trim().isEmpty()) {
            boolean tutorExists = legalGuardianDao.existsLegalGuardian(dniTutor);

            if (!tutorExists) {
                bindingResult.rejectValue("oviUser.dniLegalGuardian", "notFound",
                        "Aquest DNI no correspon a cap tutor legal registrat al sistema.");

                model.addAttribute("genderList", Gender.values());
                return "oviUser/update";
            }
        } else {
            // Si está vacío, lo convertimos a null para evitar error
            form.getOviUser().setDniLegalGuardian(null);
        }

        accountSvc.updateOviUser(form.getAccount(), form.getOviUser());

        form.getAccount().setPassword(null);
        session.setAttribute("account", form.getAccount());
        session.setAttribute("specificAccount", form.getOviUser());

        return "redirect:/oviUser/details";
    }


}