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

        // Validació del DNI
        if (account.getDni() == null || account.getDni().trim().isEmpty()) {
            errors.rejectValue("dni", "required", "El DNI és obligatori");
        } else if (!account.getDni().matches("^[0-9]{8}[A-Z]$")) {
            errors.rejectValue("dni", "invalidFormat", "El DNI ha de tindre 8 números i una lletra majúscula (Ex: 12345678A)");
        }

        // Validació del email
        if (account.getEmail() == null || account.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "required", "L'email és obligatori");
        } else if (account.getEmail().length() > 30) {
            errors.rejectValue("email", "tooLong", "L'email no pot superar els 30 caràcters");
        } else if (!account.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.rejectValue("email", "invalidFormat", "L'email ha de ser una adreça vàlida");
        }

        // Validació del nom
        if (account.getName() == null || account.getName().trim().isEmpty()) {
            errors.rejectValue("name", "required", "El nom és obligatori");
        } else if (account.getName().length() > 30) {
            errors.rejectValue("name", "tooLong", "El nom no pot superar els 30 caràcters");
        }

        // Validació dels cognoms
        if (account.getSurname() == null || account.getSurname().trim().isEmpty()) {
            errors.rejectValue("surname", "required", "Els cognoms són obligatoris");
        } else if (account.getSurname().length() > 50) {
            errors.rejectValue("surname", "tooLong", "Els cognoms no poden superar els 50 caràcters");
        }

        // Validació de la contrasenya
        if (account.getPassword() == null || account.getPassword().trim().isEmpty()) {
            errors.rejectValue("password", "required", "La contrasenya és obligatòria");
        } else if (account.getPassword().length() < 8) {
            errors.rejectValue("password", "tooShort", "La contrasenya ha de tindre almenys 8 caràcters");
        } else if (account.getPassword().length() > 100) {
            errors.rejectValue("password", "tooLong", "La contrasenya és massa llarga");
        }

        // Validació de l'aniversari
        if (account.getBirthday() == null) {
            errors.rejectValue("birthday", "required", "La data de naixement és obligatòria");
        } else if (!account.getBirthday().isBefore(LocalDate.now())) {
            errors.rejectValue("birthday", "invalidDate", "La data de naixement ha de ser anterior a la data d'avui");
        }

        // Validació del telèfon
        if (account.getPhoneNumber() == null || account.getPhoneNumber().trim().isEmpty()) {
            errors.rejectValue("phoneNumber", "required", "El numero de telèfon és obligatori");
        } else if (!account.getPhoneNumber().matches("^[0-9]{9}$")) {
            errors.rejectValue("phoneNumber", "invalidFormat", "El telèfon ha de tindre exactament 9 números");
        }

        // Validació del gènere
        if (account.getGender() == null) {
            errors.rejectValue("gender", "required", "Has de seleccionar un gènere");
        }

        // Validació de la ciutat
        if (account.getCity() == null || account.getCity().trim().isEmpty()) {
            errors.rejectValue("city", "required", "La ciutat és obligatòria");
        } else if (account.getCity().length() > 30) {
            errors.rejectValue("city", "tooLong", "La ciutat no pot superar els 30 caràcters");
        }

        // Validació del carrer
        if (account.getStreet() == null || account.getStreet().trim().isEmpty()) {
            errors.rejectValue("street", "required", "El carrer és obligatori");
        } else if (account.getStreet().length() > 30) {
            errors.rejectValue("street", "tooLong", "El carrer no pot superar els 30 caràcters");
        }

        // Validació del codi postal
        if (account.getZipCode() == null || account.getZipCode().trim().isEmpty()) {
            errors.rejectValue("zipCode", "required", "El codi postal és obligatori");
        } else if (!account.getZipCode().matches("^[0-9]{5}$")) {
            errors.rejectValue("zipCode", "invalidFormat", "El codi postal ha de tindre exactament 5 números");
        }
    }
}
