package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.dao.LegalGuardianDao;
import es.uji.ei1027.SgOviProject.dto.LegalGuardianUpdateDTO;
import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.LegalGuardian;
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
import java.util.Locale;

@Controller
@RequestMapping("/legalGuardian")
public class LegalGuardianController {
    @Autowired
    private IntAccountSvc registerService;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private LegalGuardianDao legalGuardianDao;


    @GetMapping("/register")
    public String legalGuardianRegister(Model model, HttpSession session) {

        Account account = (Account) session.getAttribute("pendingAccount");
        if(account == null) {
            return "redirect:/register";
        }
        model.addAttribute("guardianAccount", new LegalGuardian());
        return "legalGuardian/register";
    }

    @PostMapping("/register")
    public String doLegalGuardianRegister(@ModelAttribute("guardianAccount") LegalGuardian guardian,
                                          BindingResult bindingResult,
                                          HttpSession session) {

        LegalGuardianValidator guardianValidator = new LegalGuardianValidator();
        guardianValidator.validate(guardian, bindingResult);

        if (bindingResult.hasErrors()) {
            return "legalGuardian/register";
        }

        Account account = (Account) session.getAttribute("pendingAccount");

        if (account == null) {
            return "redirect:/register";
        }

        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String encryptedSignature = passwordEncryptor.encryptPassword(guardian.getSignatureCode());
        guardian.setSignatureCode(encryptedSignature);

        registerService.addLegalGuardian(account, guardian);
        session.removeAttribute("pendingAccount");
        session.removeAttribute("chosenType");

        return "redirect:/register/done";
    }

    @RequestMapping("/main")
    public String oviUserMain(Model model) {

        // Generar la fecha para la vista
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM 'de' yyyy", new Locale("ca", "ES"));
        String dateNow = LocalDate.now().format(formatter).toUpperCase();

        model.addAttribute("dateNow", dateNow);

        return "legalGuardian/main";
    }

    @GetMapping("/details")
    public String watchDetails(){
        return "legalGuardian/details";
    }

    @GetMapping("/update")
    public String updateOviUser(Model model, HttpSession session) {

        //El interceptor ya verifica que el usuario está loggeado

        Account account = (Account) session.getAttribute("account");
        LegalGuardian currentUser = (LegalGuardian) session.getAttribute("specificAccount");

        LegalGuardianUpdateDTO updateForm = new LegalGuardianUpdateDTO(account, currentUser);

        model.addAttribute("updateForm", updateForm);

        return "legalGuardian/update";
    }

    @PostMapping("/update")
    public String processUpdateLegalGuardian(@ModelAttribute("updateForm") LegalGuardianUpdateDTO form,
                                             BindingResult bindingResult,
                                             HttpSession session) {

        Account account = (Account) session.getAttribute("account");
        LegalGuardian legalGuardian = (LegalGuardian) session.getAttribute("specificAccount");

        // Ponemos datos que no aparecen en el formulario previo a pasarlo por el validador
        form.getAccount().setDni(account.getDni());
        form.getAccount().setStatus(account.getStatus());
        form.getLegalGuardian().setDni(legalGuardian.getDni());

        // Le ponemos una contraseña temporal corta para "engañar" al AccountValidator
        // porque la contraseña encriptada de la BD es demasiado larga y no pasa validador
        form.getAccount().setPassword("ValidPass123");
        // Lo mismo para el signatureCode
        form.getLegalGuardian().setSignatureCode("ValidSig123");

        LegalGuardianUpdateDTOValidator validator = new LegalGuardianUpdateDTOValidator();
        validator.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            form.getAccount().setPassword(null);
            form.getLegalGuardian().setSignatureCode(null);
            return "legalGuardian/update";
        }

        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();

        // Manejo de la contraseña de la cuenta
        if (form.getNewPassword() != null && !form.getNewPassword().trim().isEmpty()) {
            String encryptedPassword = passwordEncryptor.encryptPassword(form.getNewPassword());
            form.getAccount().setPassword(encryptedPassword);
        } else {
            account = accountDao.getAccount(account.getDni());  // cogemos la cuenta con contraseña de la BD
            form.getAccount().setPassword(account.getPassword());
        }

        // Manejo del código de firma (signatureCode)
        if (form.getNewSignatureCode() != null && !form.getNewSignatureCode().trim().isEmpty()) {
            String encryptedSignature = passwordEncryptor.encryptPassword(form.getNewSignatureCode());
            form.getLegalGuardian().setSignatureCode(encryptedSignature);
        } else {
            legalGuardian = legalGuardianDao.getLegalGuardian(legalGuardian.getDni());
            form.getLegalGuardian().setSignatureCode(legalGuardian.getSignatureCode());
        }

        registerService.updateLegalGuardian(form.getAccount(), form.getLegalGuardian());

        form.getAccount().setPassword(null);
        form.getLegalGuardian().setSignatureCode(null);

        // Actualizamos sesion
        session.setAttribute("account", form.getAccount());
        session.setAttribute("specificAccount", form.getLegalGuardian());

        return "redirect:/legalGuardian/details";
    }
}
