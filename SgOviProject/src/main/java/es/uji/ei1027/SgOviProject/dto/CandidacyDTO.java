package es.uji.ei1027.SgOviProject.dto;

import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.Candidacy;
import es.uji.ei1027.SgOviProject.model.PapPati;

// Clase envoltorio para Candidacy
public class CandidacyDTO {
    private Candidacy candidacy;
    private PapPati papPati;
    private Account account;

    public CandidacyDTO(Candidacy candidacy, PapPati papPati, Account account) {
        this.candidacy = candidacy;
        this.papPati = papPati;
        this.account = account;
    }

    public Candidacy getCandidacy() { return candidacy; }
    public void setCandidacy(Candidacy candidacy) { this.candidacy = candidacy; }

    public PapPati getPapPati() { return papPati; }
    public void setPapPati(PapPati papPati) { this.papPati = papPati; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

}
