package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.dao.LegalGuardianDao;
import es.uji.ei1027.SgOviProject.dto.OviUserUpdateDTO;
import es.uji.ei1027.SgOviProject.enums.Gender;
import es.uji.ei1027.SgOviProject.exception.SgOviException;
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
import java.util.List;
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

    @ModelAttribute("genderList")
    public List<Gender> genderList() {
        return Arrays.asList(Gender.values());
    }

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

        if (account == null) {
            return "redirect:/register";
        }

        accountSvc.addOviUser(account, oviUser);
        session.removeAttribute("pendingAccount");
        session.removeAttribute("chosenType");

        return "redirect:/register/done";
    }

    @GetMapping("/details")
    public String watchDetails(){
        //El interceptor ya verifica que el usuario está loggeado

        return "oviUser/details";
    }


    @GetMapping("/update")
    public String updateOviUser(Model model, HttpSession session) {

        //El interceptor ya verifica que el usuario está loggeado

        Account account = (Account) session.getAttribute("account");
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        if (currentUser.getDniLegalGuardian() != null) {
            throw new SgOviException("Els permisos per modificar el perfil corresponen al teu tutor", "Error 403 - Sense permisos");
        }

        OviUserUpdateDTO updateForm = new OviUserUpdateDTO(account, currentUser);

        model.addAttribute("updateForm", updateForm);

        return "oviUser/update";
    }

    @PostMapping("/update")
    public String processUpdateOviUser(@ModelAttribute("updateForm") OviUserUpdateDTO form,
                                       BindingResult bindingResult,
                                       HttpSession session) {

        Account account = (Account) session.getAttribute("account");
        OviUser oviUser = (OviUser) session.getAttribute("specificAccount");

        if (oviUser.getDniLegalGuardian() != null) {
            throw new SgOviException("Els permisos per modificar el perfil corresponen al teu tutor", "Error 403 - Sense permisos");
        }

        // Ponemos datos que no aparecen en el formulario previo a pasarlo por el validador
        form.getAccount().setDni(account.getDni());
        form.getAccount().setStatus(account.getStatus());
        form.getOviUser().setDni(oviUser.getDni());

        // Le ponemos una contraseña temporal corta para "engañar" al AccountValidator
        // porque la contraseña encriptada de la BD es demasiado larga y no pasa validador
        form.getAccount().setPassword("ValidPass123");

        OviUserUpdateDTOValidator validator = new OviUserUpdateDTOValidator();
        validator.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            form.getAccount().setPassword(null); // La limpiamos por si acaso antes de volver a la vista
            return "oviUser/update";
        }

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
                form.getAccount().setPassword(null);
                return "oviUser/update";
            }
        } else {
            // Si está vacío, lo convertimos a null para evitar error
            form.getOviUser().setDniLegalGuardian(null);
        }

        accountSvc.updateOviUser(form.getAccount(), form.getOviUser());

        form.getAccount().setPassword(null);

        // Actualizamos sesion
        session.setAttribute("account", form.getAccount());
        session.setAttribute("specificAccount", form.getOviUser());

        return "redirect:/oviUser/details";
    }


}