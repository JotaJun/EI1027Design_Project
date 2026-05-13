package es.uji.ei1027.SgOviProject.dto;

import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.LegalGuardian;

public class LegalGuardianUpdateDTO {
    private Account account;
    private LegalGuardian legalGuardian;
    private String newPassword;
    private String confirmPassword;
    private String newSignatureCode;
    private String confirmSignatureCode;

    public LegalGuardianUpdateDTO() {
        this.account = new Account();
        this.legalGuardian = new LegalGuardian();
    }

    public LegalGuardianUpdateDTO(Account account, LegalGuardian legalGuardian){
        this.account = account;
        this.legalGuardian = legalGuardian;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LegalGuardian getLegalGuardian() {
        return legalGuardian;
    }

    public void setLegalGuardian(LegalGuardian legalGuardian) {
        this.legalGuardian = legalGuardian;
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

    public String getNewSignatureCode() {
        return newSignatureCode;
    }

    public void setNewSignatureCode(String newSignatureCode) {
        this.newSignatureCode = newSignatureCode;
    }

    public String getConfirmSignatureCode() {
        return confirmSignatureCode;
    }

    public void setConfirmSignatureCode(String confirmSignatureCode) {
        this.confirmSignatureCode = confirmSignatureCode;
    }

    @Override
    public String toString() {
        return "LegalGuardianUpdateDTO{" +
                "account=" + account +
                ", legalGuardian=" + legalGuardian +
                ", newPassword='" + newPassword + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", newSignatureCode='" + newSignatureCode + '\'' +
                ", confirmSignatureCode='" + confirmSignatureCode + '\'' +
                '}';
    }
}
