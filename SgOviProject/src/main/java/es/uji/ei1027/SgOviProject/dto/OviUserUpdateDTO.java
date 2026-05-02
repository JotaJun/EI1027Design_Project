package es.uji.ei1027.SgOviProject.dto;

import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.OviUser;

public class OviUserUpdateDTO {
    private Account account;
    private OviUser oviUser;
    private String newPassword;
    private String confirmPassword;

    public OviUserUpdateDTO(Account account, OviUser oviUser){
        this.account = account;
        this.oviUser = oviUser;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public OviUser getOviUser() {
        return oviUser;
    }

    public void setOviUser(OviUser oviUser) {
        this.oviUser = oviUser;
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
        return "OviUserUpdateDTO{" +
                "account=" + account +
                ", oviUser=" + oviUser +
                ", newPassword='" + newPassword + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
