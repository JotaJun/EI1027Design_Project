package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dto.LegalGuardianUpdateDTO;
import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.LegalGuardian;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class LegalGuardianUpdateDTOValidator implements Validator {

    private final RegisterValidator accountValidator = new RegisterValidator();
    private final LegalGuardianValidator legalGuardianValidator = new LegalGuardianValidator();

    @Override
    public boolean supports(Class<?> cls) {
        return LegalGuardianUpdateDTO.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LegalGuardianUpdateDTO dto = (LegalGuardianUpdateDTO) target;
        Account account = dto.getAccount();
        LegalGuardian legalGuardian = dto.getLegalGuardian();

        // 1. Validaciones de la cuenta (Account)
        try {
            errors.pushNestedPath("account");
            ValidationUtils.invokeValidator(accountValidator, account, errors);
        } finally {
            errors.popNestedPath();
        }

        // 2. Validaciones del Tutor Legal (LegalGuardian)
        try {
            errors.pushNestedPath("legalGuardian");
            ValidationUtils.invokeValidator(legalGuardianValidator, legalGuardian, errors);
        } finally {
            errors.popNestedPath();
        }

        // 3. Validaciones del cambio de contraseña
        String newPwd = dto.getNewPassword();
        String confirmPwd = dto.getConfirmPassword();

        if (newPwd != null && !newPwd.trim().isEmpty()) {
            if (newPwd.length() < 8) {
                errors.rejectValue("newPassword", "error.password", "La contrasenya ha de tindre almenys 8 caràcters");
            } else if (!newPwd.equals(confirmPwd)) {
                errors.rejectValue("confirmPassword", "error.password", "Les contrasenyes no coincideixen");
            }
        }
    }
}
