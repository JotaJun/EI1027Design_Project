package es.uji.ei1027.SgOviProject.dto;

import es.uji.ei1027.SgOviProject.enums.AccountType;
import es.uji.ei1027.SgOviProject.model.Account;

public class AccountWithTypeDTO {
    private Account account;
    private AccountType accountType;

    public AccountWithTypeDTO(Account account, AccountType accountType) {
        this.account = account;
        this.accountType = accountType;
    }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
}
