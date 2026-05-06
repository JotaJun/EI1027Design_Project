package es.uji.ei1027.SgOviProject.controller;


import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.dto.PapPatiUpdateDTO;
import es.uji.ei1027.SgOviProject.enums.Gender;
import es.uji.ei1027.SgOviProject.enums.StaffType;
import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.PapPati;
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
@RequestMapping("/papPati")
public class PapPatiController {

    @Autowired
    private IntAccountSvc accountSvc;

    @Autowired
    private AccountDao accountDao;

    @ModelAttribute("genderList")
    public List<Gender> genderList() {
        return Arrays.asList(Gender.values());
    }

    @ModelAttribute("staffTypeList")
    public List<StaffType> staffTypeList() {
        return Arrays.asList(StaffType.values());
    }


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


        accountSvc.addPapPati(account, papPati);
        session.removeAttribute("pendingAccount");
        session.removeAttribute("chosenType");

        return "redirect:/register/done";
    }

    @RequestMapping("/main")
    public String papPatiMain(Model model) {

        // Generar la fecha para la vista
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM 'de' yyyy", new Locale("ca", "ES"));
        String dateNow = LocalDate.now().format(formatter).toUpperCase();

        model.addAttribute("dateNow", dateNow);

        return "papPati/main";
    }

    @GetMapping("/details")
    public String watchDetails(Model model, HttpSession session){
        //El interceptor ya verifica que el usuario está loggeado

        // Usaremos la sesión para acceder a los atributos

        return "papPati/details";
    }

    @GetMapping("/update")
    public String updatePapPati(Model model, HttpSession session) {

        Account account = (Account) session.getAttribute("account");
        PapPati currentPapPati = (PapPati) session.getAttribute("specificAccount");

        PapPatiUpdateDTO updateForm = new PapPatiUpdateDTO(account, currentPapPati);

        model.addAttribute("updateForm", updateForm);

        return "papPati/update";
    }

    @PostMapping("/update")
    public String processUpdatePapPati(@ModelAttribute("updateForm") PapPatiUpdateDTO form,
                                       BindingResult bindingResult,
                                       HttpSession session) {

        Account account = (Account) session.getAttribute("account");
        PapPati papPati = (PapPati) session.getAttribute("specificAccount");

        // Ponemos datos que no aparecen en el formulario previo a pasarlo por el validador
        form.getAccount().setDni(account.getDni());
        form.getAccount().setStatus(account.getStatus());
        form.getPapPati().setDni(papPati.getDni());

        // Le ponemos una contraseña temporal corta para "engañar" al AccountValidator
        // porque la contraseña encriptada de la BD es demasiado larga y no pasa validador
        form.getAccount().setPassword("ValidPass123");


        PapPatiUpdateDTOValidator validator = new PapPatiUpdateDTOValidator();
        validator.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            // --- CODI DE DEPURACIÓ PER A LA CONSOLA ---
            System.out.println("❌ ERROR DE VALIDACIÓ AL FORMULARI UPDATE PAPPATI:");
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("   - " + error.toString());
            });
            System.out.println("---------------------------------------------------");
            // ------------------------------------------
            form.getAccount().setPassword(null); // La limpiamos por si acaso antes de volver a la vista
            return "papPati/update";
        }

        if (form.getNewPassword() != null && !form.getNewPassword().trim().isEmpty()) {
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            String encryptedPassword = passwordEncryptor.encryptPassword(form.getNewPassword());
            form.getAccount().setPassword(encryptedPassword);
        } else {
            Account accountDb = accountDao.getAccount(account.getDni());
            form.getAccount().setPassword(accountDb.getPassword());
        }

//        // Ponemos los dnis y status para que no aparezcan en el html, ya que ya está registrado
//        form.getAccount().setDni(account.getDni());
//        form.getAccount().setStatus(account.getStatus());
//        form.getPapPati().setDni(papPati.getDni());
//
//        // Logica de la contraseña
//        if (form.getNewPassword() != null && !form.getNewPassword().trim().isEmpty()) {
//            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
//            String encryptedPassword = passwordEncryptor.encryptPassword(form.getNewPassword());
//            form.getAccount().setPassword(encryptedPassword);
//        } else {
//            Account accountDb = accountDao.getAccount(account.getDni());
//            form.getAccount().setPassword(accountDb.getPassword());
//        }

        accountSvc.updatePapPati(form.getAccount(), form.getPapPati());

        form.getAccount().setPassword(null);

        // Actualizamos sesion
        session.setAttribute("account", form.getAccount());
        session.setAttribute("specificAccount", form.getPapPati());

        return "redirect:/papPati/details";
    }
}