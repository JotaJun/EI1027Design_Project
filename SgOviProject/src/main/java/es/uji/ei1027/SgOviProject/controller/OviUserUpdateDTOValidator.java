package es.uji.ei1027.SgOviProject.controller;

import org.springframework.validation.Validator;
import es.uji.ei1027.SgOviProject.dto.OviUserUpdateDTO;
import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.OviUser;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class OviUserUpdateDTOValidator implements Validator{
    @Override
    public boolean supports(Class<?> cls) {
        return OviUserUpdateDTO.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OviUserUpdateDTO dto = (OviUserUpdateDTO) target;
        Account account = dto.getAccount();
        OviUser oviUser = dto.getOviUser();

        // ==========================================
        // 1. VALIDACIONS DE L'ACCOUNT
        // ==========================================

        // Validació del nom
        if (account.getName() == null || account.getName().trim().isEmpty()) {
            errors.rejectValue("account.name", "required", "El nom és obligatori");
        } else if (account.getName().length() > 30) {
            errors.rejectValue("account.name", "tooLong", "El nom no pot superar els 30 caràcters");
        }

        // Validació dels cognoms
        if (account.getSurname() == null || account.getSurname().trim().isEmpty()) {
            errors.rejectValue("account.surname", "required", "Els cognoms són obligatoris");
        } else if (account.getSurname().length() > 50) {
            errors.rejectValue("account.surname", "tooLong", "Els cognoms no poden superar els 50 caràcters");
        }

        // Validació del email
        if (account.getEmail() == null || account.getEmail().trim().isEmpty()) {
            errors.rejectValue("account.email", "required", "L'email és obligatori");
        } else if (account.getEmail().length() > 30) {
            errors.rejectValue("account.email", "tooLong", "L'email no pot superar els 30 caràcters");
        } else if (!account.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.rejectValue("account.email", "invalidFormat", "L'email ha de ser una adreça vàlida");
        }

        // Validació de cumpleaños
        if (account.getBirthday() == null) {
            errors.rejectValue("account.birthday", "required", "La data de naixement és obligatòria");
        } else if (!account.getBirthday().isBefore(LocalDate.now())) {
            errors.rejectValue("account.birthday", "invalidDate", "La data de naixement ha de ser anterior a la data d'avui");
        }

        // Validació del telèfon
        if (account.getPhoneNumber() == null || account.getPhoneNumber().trim().isEmpty()) {
            errors.rejectValue("account.phoneNumber", "required", "El numero de telèfon és obligatori");
        } else if (!account.getPhoneNumber().matches("^[0-9]{9}$")) {
            errors.rejectValue("account.phoneNumber", "invalidFormat", "El telèfon ha de tindre exactament 9 números");
        }

        // Validació del gènere
        if (account.getGender() == null) {
            errors.rejectValue("account.gender", "required", "Has de seleccionar un gènere");
        }

        // Validació de la ciutat, carrer i codi postal
        if (account.getCity() == null || account.getCity().trim().isEmpty()) {
            errors.rejectValue("account.city", "required", "La ciutat és obligatòria");
        } else if (account.getCity().length() > 30) {
            errors.rejectValue("account.city", "tooLong", "La ciutat no pot superar els 30 caràcters");
        }

        if (account.getStreet() == null || account.getStreet().trim().isEmpty()) {
            errors.rejectValue("account.street", "required", "El carrer és obligatori");
        } else if (account.getStreet().length() > 30) {
            errors.rejectValue("account.street", "tooLong", "El carrer no pot superar els 30 caràcters");
        }

        if (account.getZipCode() == null || account.getZipCode().trim().isEmpty()) {
            errors.rejectValue("account.zipCode", "required", "El codi postal és obligatori");
        } else if (!account.getZipCode().matches("^[0-9]{5}$")) {
            errors.rejectValue("account.zipCode", "invalidFormat", "El codi postal ha de tindre exactament 5 números");
        }

        // ==========================================
        // 2. VALIDACIONS DE L'OVIUSER (TUTOR LEGAL)
        // ==========================================

        String dniTutor = oviUser.getDniLegalGuardian();

        // Només validem el format SI ha escrit alguna cosa.
        if (dniTutor != null && !dniTutor.trim().isEmpty()) {
            if (!dniTutor.matches("^[0-9]{8}[A-Z]$")) {
                errors.rejectValue("oviUser.dniLegalGuardian", "invalidFormat", "El DNI ha de tindre 8 números i una lletra majúscula (Ex: 12345678A)");
            }
        }

        // ==========================================
        // 3. VALIDACIONS DEL CANVI DE CONTRASENYA
        // ==========================================

        String newPwd = dto.getNewPassword();
        String confirmPwd = dto.getConfirmPassword();

        // Només validem si l'usuari ha intentat canviar la contrasenya (no està buida)
        if (newPwd != null && !newPwd.trim().isEmpty()) {
            if (newPwd.length() < 8) {
                errors.rejectValue("newPassword", "error.password", "La contrasenya ha de tindre almenys 8 caràcters");
            } else if (!newPwd.equals(confirmPwd)) {
                errors.rejectValue("confirmPassword", "error.password", "Les contrasenyes no coincideixen");
            }
        }
    }
}
