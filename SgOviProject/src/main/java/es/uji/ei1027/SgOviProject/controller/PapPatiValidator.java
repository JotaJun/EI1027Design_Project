package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.model.PapPati;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PapPatiValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return PapPati.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PapPati papPati = (PapPati) target;

        // Validació del DNI (camp intern, no ha d'estar buit mai)
        if (papPati.getDni() == null || papPati.getDni().trim().isEmpty()) {
            errors.rejectValue("dni", "required", "Error intern: S'ha perdut la referència del DNI");
        }

        // Validació del tipus de personal (enum)
        if (papPati.getStaffType() == null) {
            errors.rejectValue("staffType", "required", "El tipus de personal és obligatori");
        }

        // Validació de la data d'inici de disponibilitat
        if (papPati.getInitialAvailableDate() == null) {
            errors.rejectValue("initialAvailableDate", "required", "La data d'inici de disponibilitat és obligatòria");
        }

        // Validació de la data de fi de disponibilitat
        if (papPati.getLastAvailableDate() == null) {
            errors.rejectValue("lastAvailableDate", "required", "La data de fi de disponibilitat és obligatòria");
        }

        // Validació que la data de fi siga posterior a la data d'inici
        if (papPati.getInitialAvailableDate() != null && papPati.getLastAvailableDate() != null) {
            if (!papPati.getLastAvailableDate().isAfter(papPati.getInitialAvailableDate())) {
                errors.rejectValue("lastAvailableDate", "invalidRange",
                        "La data de fi ha de ser posterior a la data d'inici");
            }
        }

        // Validació de la formació/training
        if (papPati.getTraining() == null || papPati.getTraining().trim().isEmpty()) {
            errors.rejectValue("training", "required", "La formació no pot estar buida");
        }

        // Validació dels anys d'experiència (ha de ser un valor no negatiu)
        if (papPati.getYearsOfExperience() < 0) {
            errors.rejectValue("yearsOfExperience", "invalidValue",
                    "Els anys d'experiència no poden ser negatius");
        }

        // Validació de l'URL del CV
        if (papPati.getUrlCv() == null || papPati.getUrlCv().trim().isEmpty()) {
            errors.rejectValue("urlCv", "required", "L'URL del currículum no pot estar buit");
        }
    }
}
