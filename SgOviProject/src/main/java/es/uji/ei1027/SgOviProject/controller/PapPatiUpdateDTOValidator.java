package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dto.PapPatiUpdateDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class PapPatiUpdateDTOValidator implements Validator{

    private final RegisterValidator accountValidator = new RegisterValidator();
    private final PapPatiValidator papPatiValidator = new PapPatiValidator();

    @Override
    public boolean supports(Class<?> cls) {
        return PapPatiUpdateDTO.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PapPatiUpdateDTO dto = (PapPatiUpdateDTO) target;

        // Delegamos la validación de la cuenta
        try {
            errors.pushNestedPath("account");
            ValidationUtils.invokeValidator(accountValidator, dto.getAccount(), errors);
        } finally {
            errors.popNestedPath(); // Siempre hay que quitar el prefijo al terminar
        }

        // Delegamos la validación del PapPati
        try {
            errors.pushNestedPath("papPati");
            ValidationUtils.invokeValidator(papPatiValidator, dto.getPapPati(), errors);
        } finally {
            errors.popNestedPath();
        }

        // Validación específica del DTO (Cambio de contraseña)
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
