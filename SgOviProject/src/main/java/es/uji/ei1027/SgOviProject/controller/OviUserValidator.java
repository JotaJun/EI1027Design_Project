package es.uji.ei1027.SgOviProject.controller;
import es.uji.ei1027.SgOviProject.model.OviUser;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class OviUserValidator implements Validator {


    @Override
    public boolean supports(Class<?> cls) {
        return OviUser.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OviUser oviUser = (OviUser) target;

        // Validació de DNI del Tutor Legal
        if (oviUser.getDniLegalGuardian() == null || oviUser.getDniLegalGuardian().trim().isEmpty()) {
            errors.rejectValue("dniLegalGuardian", "required", "El DNI del tutor és obligatori");
        } else if (!oviUser.getDniLegalGuardian().matches("^[0-9]{8}[A-Z]$")) {
            errors.rejectValue("dniLegalGuardian", "invalidFormat", "El DNI ha de tindre 8 números i una lletra majúscula (Ex: 12345678A)");
        }

    }
}
