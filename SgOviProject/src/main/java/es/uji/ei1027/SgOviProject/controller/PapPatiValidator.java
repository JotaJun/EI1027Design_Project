package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.model.PapPati;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class PapPatiValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return PapPati.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PapPati papPati = (PapPati) target;

        // Validació del tipus de personal (enum)
        if (papPati.getStaffType() == null) {
            errors.rejectValue("staffType", "required", "El tipus de personal és obligatori");
        }

        LocalDate ini = papPati.getInitialAvailableDate();
        LocalDate fi = papPati.getLastAvailableDate();

        if (ini == null && fi != null) {
            errors.rejectValue("initialAvailableDate", "requiredWithEnd",
                    "Si indiques una data de fi, la d'inici també és obligatòria");
        } else if (ini != null && fi == null) {
            errors.rejectValue("lastAvailableDate", "requiredWithStart",
                    "Si indiques una data d'inici, la de fi també és obligatòria");
        } else if (ini != null && fi != null) {
            // Si ambas existen, validamos el rango
            if (!fi.isAfter(ini)) {
                errors.rejectValue("lastAvailableDate", "invalidRange",
                        "La data de fi ha de ser posterior a la data d'inici");
            }
        }
        // Si ambas son null, se considera válido (no disponible)

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
