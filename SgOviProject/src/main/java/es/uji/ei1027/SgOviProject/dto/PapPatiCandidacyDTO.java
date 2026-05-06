package es.uji.ei1027.SgOviProject.dto;

import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.AssistanceRequest;
import es.uji.ei1027.SgOviProject.model.Candidacy;
import es.uji.ei1027.SgOviProject.model.OviUser;

public class PapPatiCandidacyDTO {
    private Candidacy candidacy;
    private AssistanceRequest assistanceRequest;
    private OviUser oviUser;
    private Account oviUserAccount;

    public PapPatiCandidacyDTO(Candidacy candidacy, AssistanceRequest assistanceRequest, OviUser oviUser, Account account) {
        this.candidacy = candidacy;
        this.assistanceRequest = assistanceRequest;
        this.oviUser = oviUser;
        this.oviUserAccount = account;
    }

    public Candidacy getCandidacy() {
        return candidacy;
    }

    public void setCandidacy(Candidacy candidacy) {
        this.candidacy = candidacy;
    }

    public AssistanceRequest getAssistanceRequest() {
        return assistanceRequest;
    }

    public void setAssistanceRequest(AssistanceRequest assistanceRequest) {
        this.assistanceRequest = assistanceRequest;
    }

    public OviUser getOviUser() {
        return oviUser;
    }

    public void setOviUser(OviUser oviUser) {
        this.oviUser = oviUser;
    }

    public Account getOviUserAccount() {
        return oviUserAccount;
    }

    public void setOviUserAccount(Account oviUserAccount) {
        this.oviUserAccount = oviUserAccount;
    }

    @Override
    public String toString() {
        return "PapPatiCandidacyDTO{" +
                "candidacy=" + candidacy +
                ", assistanceRequest=" + assistanceRequest +
                ", oviUser=" + oviUser +
                ", oviUserAccount=" + oviUserAccount +
                '}';
    }
}
