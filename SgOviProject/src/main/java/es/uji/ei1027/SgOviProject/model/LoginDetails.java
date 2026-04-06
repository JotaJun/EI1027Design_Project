package es.uji.ei1027.SgOviProject.model;

import es.uji.ei1027.SgOviProject.enums.AccountType;

public class LoginDetails {
    AccountType accountType;
    String email;
    String password;

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
