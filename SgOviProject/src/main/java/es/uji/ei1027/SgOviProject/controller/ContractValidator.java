package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.model.Contract;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class ContractValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return Contract.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Contract contract = (Contract) target;

        if (contract.getStartDate() == null) {
            errors.rejectValue("startDate", "required", "La data d'inici és obligatòria");
        }else {
            if (contract.getStartDate().isBefore(LocalDate.now())) {
                errors.rejectValue("startDate", "pastDate", "La data d'inici no pot ser anterior a la data actual");
            }
        }

        if (contract.getEndDate() == null) {
            errors.rejectValue("endDate", "required", "La data de fi és obligatòria");
        }

        if (contract.getStartDate() != null && contract.getEndDate() != null) {
            if (!contract.getStartDate().isBefore(contract.getEndDate())) {
                errors.rejectValue("endDate", "dateMismatch", "La data de fi ha de ser estrictament posterior a la data d'inici");
            }
        }

        if (contract.getHourlySalary() <= 0) {
            errors.rejectValue("hourlySalary", "negativeOrZero", "El salari per hora ha de ser major que zero");
        }

        if (contract.getSchedule() == null || contract.getSchedule().trim().isEmpty()) {
            errors.rejectValue("schedule", "required", "L'horari és obligatori");
        } else if (contract.getSchedule().length() > 500) {
            errors.rejectValue("schedule", "tooLong", "L'horari no pot superar els 500 caràcters");
        }

        // No validamos el contrato ya que no se lo pediremos al usuario
//        if (contract.getUrlDocument() == null || contract.getUrlDocument().trim().isEmpty()) {
//            errors.rejectValue("urlDocument", "required", "El document del contracte en PDF és obligatori");
//        } else if (contract.getUrlDocument().length() > 500) {
//            errors.rejectValue("urlDocument", "tooLong", "L'enllaç del document no pot superar els 500 caràcters");
//        }
    }
}
