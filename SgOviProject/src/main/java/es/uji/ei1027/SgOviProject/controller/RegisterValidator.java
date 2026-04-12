package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.model.Account;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class RegisterValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return Account.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Account account = (Account) target;

        //Validació del DNI
        if (account.getDni() == null || account.getDni().trim().isEmpty()) {
            errors.rejectValue("dni", "required", "El DNI és obligatori");
        }
        else if (account.getDni().length() != 9) {
                errors.rejectValue("dni", "invalidFormat", "El DNI ha de tindre 9 caràcters");
            }
        else if (!Character.isLetter(account.getDni().charAt(8))) {
                errors.rejectValue("dni", "invalidFormat", "L'últim caràcter ha de ser una lletra");
            }

        //Validació del email
        if (account.getEmail() == null || account.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "required", "L'email és obligatori");
        } else if (!account.getEmail().contains("@")) {
            errors.rejectValue("email", "invalidFormat", "L'email ha de ser una adreça vàlida");
        }

        //Validació del name
        if (account.getName() == null || account.getName().trim().isEmpty()) {
            errors.rejectValue("name", "required", "El nom és obligatori");
        }

        //Validació del surname
        if (account.getSurname() == null || account.getSurname().trim().isEmpty()) {
            errors.rejectValue("surname", "required", "Els cognoms són obligatoris");
        }

        //Validació del password
        if (account.getPassword() == null || account.getPassword().trim().isEmpty()) {
            errors.rejectValue("password", "required", "La contrasenya és obligatòria");
        } else if (account.getPassword().length() < 8) {
            errors.rejectValue("password", "tooShort", "La contrasenya ha de tindre almenys 8 caràcters");
        }

        //Validació de l'aniversari
        if (account.getBirthday() == null) {
            errors.rejectValue("birthday", "required", "La data de naixement és obligatòria");
        } else if (!account.getBirthday().isBefore(LocalDate.now())) {
            errors.rejectValue("birthday", "invalidDate", "La data de naixement ha de ser anterior a la data d'avui");
        }

        //Validació del numero de telefon
        if (account.getPhoneNumber() == null || account.getPhoneNumber().trim().isEmpty()) {
            errors.rejectValue("phoneNumber", "required", "El numero de telèfon és obligatori");
        }

        //Validació del gènere
        if (account.getGender() == null) {
            errors.rejectValue("gender", "required", "Has de seleccionar un gènere");
        }

        //Validació de la direcció
        if (account.getCity() == null || account.getCity().trim().isEmpty()) {
            errors.rejectValue("city", "required", "La ciutat és obligatòria");
        }

        if (account.getStreet() == null || account.getStreet().trim().isEmpty()) {
            errors.rejectValue("street", "required", "El carrer és obligatori");
        }

        if (account.getZipCode() == null || account.getZipCode().trim().isEmpty()) {
            errors.rejectValue("zipCode", "required", "El codi postal és obligatori");
        }
    }
}