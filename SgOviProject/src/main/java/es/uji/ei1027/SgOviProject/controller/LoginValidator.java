package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.model.LoginDetails;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class LoginValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return LoginDetails.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginDetails user = (LoginDetails) target;

        // Comprobar que están los campos rellenados

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()){
            errors.rejectValue("email", "required", "El email no pot estar buit");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()){
            errors.rejectValue("password", "required", "La contrasenya no pot estar buida");
        }

        // Comprobar que la contraseña tenga como mínimo 8 carácteres (requisito de la tabla)
        else if (user.getPassword().length() < 8) {
            errors.rejectValue("password", "tooShort", "La contrasenya ha de tindre almenys 8 caràcters");
        }
    }
}
