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

        // El DNI se valida en el primer paso del registro general.

        // Validació de la signatura
        if (guardian.getSignatureCode() == null || guardian.getSignatureCode().trim().isEmpty()) {
            errors.rejectValue("signatureCode", "required", "La signatura és obligatòria");
        } else if (guardian.getSignatureCode().length() < 4) {
            errors.rejectValue("signatureCode", "tooShort", "La signatura no pot ser menor de 4 caràcters");
        } else if (guardian.getSignatureCode().length() > 20) {
            errors.rejectValue("signatureCode", "tooLong", "La signatura no pot superar els 20 caràcters");
        }
    }
}
