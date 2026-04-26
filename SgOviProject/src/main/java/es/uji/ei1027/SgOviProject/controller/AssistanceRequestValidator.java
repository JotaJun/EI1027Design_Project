package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.model.AssistanceRequest;
import es.uji.ei1027.SgOviProject.model.LoginDetails;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AssistanceRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return AssistanceRequest.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AssistanceRequest request = (AssistanceRequest) target;

        if (request.getAssistantType() == null) {
            errors.rejectValue("assistantType", "required", "Has de seleccionar un tipus d'assistent");
        }

        if (request.getInitialDateRequired() == null) {
            errors.rejectValue("initialDateRequired", "required", "La data d'inici és obligatòria");
        }

        if (request.getMonthsRequired() < 1) {
            errors.rejectValue("monthsRequired", "tooShort", "La duració ha de ser d'almenys 1 mes");
        }

        if (request.getCity() != null && request.getCity().length() > 30) {
            errors.rejectValue("city", "tooLong", "La població no pot superar els 30 caràcters");
        }

        if (request.getYearsOfExperience() != null && request.getYearsOfExperience() < 0) {
            errors.rejectValue("yearsOfExperience", "negative", "Els anys d'experiència no poden ser negatius");
        }

        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            errors.rejectValue("description", "required", "La descripció del motiu és obligatòria");
        } else if (request.getDescription().length() > 255) {
            errors.rejectValue("description", "tooLong", "La descripció no pot superar els 255 caràcters");
        }
    }

}
