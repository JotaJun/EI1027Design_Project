package es.uji.ei1027.SgOviProject.controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import es.uji.ei1027.SgOviProject.model.LegalGuardian;

public class LegalGuardianValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return LegalGuardian.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LegalGuardian guardian = (LegalGuardian) target;

        //Validació de DNI
        if (guardian.getDni() == null || guardian.getDni().trim().isEmpty()) {
            errors.rejectValue("dni", "required", "El DNI és obligatori");
        } else if (!guardian.getDni().matches("^[0-9]{8}[A-Z]$")) {
            errors.rejectValue("dni", "invalidFormat", "El DNI ha de tindre 8 números i una lletra majúscula (Ex: 12345678A)");
        }

        // Validació de la signatura
        if (guardian.getSignatureCode() == null || guardian.getSignatureCode().trim().isEmpty()) {
            errors.rejectValue("name", "required", "La signatura és obligatòria");
        } else if (guardian.getSignatureCode().length() < 4) {
            errors.rejectValue("name", "tooShort", "La signatura obligatòria no pot ser menor de 4 caràcters");
        } else if (guardian.getSignatureCode().length() > 15) {
        errors.rejectValue("name", "tooLong", "La signatura obligatòria no pot superar els 15 caràcters");
    }
    }
}
