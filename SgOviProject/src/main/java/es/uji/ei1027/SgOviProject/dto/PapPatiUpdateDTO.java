package es.uji.ei1027.SgOviProject.dto;

import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.PapPati;

public class PapPatiUpdateDTO {
    private Account account;
    private PapPati papPati;
    private String newPassword;
    private String confirmPassword;

    public PapPatiUpdateDTO() {
        this.account = new Account();
        this.papPati = new PapPati();
    }

    public PapPatiUpdateDTO(Account account, PapPati papPati){
        this.account = account;
        this.papPati = papPati;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public PapPati getPapPati() {
        return papPati;
    }

    public void setPapPati(PapPati papPati) {
        this.papPati = papPati;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "PapPatiUpdateDTO{" +
                "account=" + account +
                ", papPati=" + papPati +
                ", newPassword='" + newPassword + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
