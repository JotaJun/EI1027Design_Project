package es.uji.ei1027.SgOviProject.controller;

import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import es.uji.ei1027.SgOviProject.dto.OviUserUpdateDTO;
import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.OviUser;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class OviUserUpdateDTOValidator implements Validator{

    private final RegisterValidator accountValidator = new RegisterValidator();

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
        // Delegamos la validación de la cuenta
        try {
            errors.pushNestedPath("account");
            ValidationUtils.invokeValidator(accountValidator, account, errors);
        } finally {
            errors.popNestedPath(); // Siempre hay que quitar el prefijo al terminar
        }

        // ==========================================
        // 2. VALIDACIONS DE L'OVIUSER (TUTOR LEGAL)
        // ==========================================

        // Aquí no delegamos, ya que solo hay un método y aquí funciona diferente
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
