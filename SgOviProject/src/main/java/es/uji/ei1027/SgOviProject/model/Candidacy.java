package es.uji.ei1027.SgOviProject.model;

import es.uji.ei1027.SgOviProject.enums.CandidacyStatus;
import java.time.LocalDate;

public class Candidacy {
    private int idCandidacy;
    private CandidacyStatus candidacyStatus = CandidacyStatus.TALKSNOTSTARTED;
    private String rejectedReason;
    private LocalDate dateLastModified;
    private int idApRequest;
    private String dniPapPati;

    public int getIdCandidacy() {
        return idCandidacy;
    }

    public void setIdCandidacy(int idCandidacy) {
        this.idCandidacy = idCandidacy;
    }

    public CandidacyStatus getCandidacyStatus() {
        return candidacyStatus;
    }

    public void setCandidacyStatus(CandidacyStatus candidacyStatus) {
        this.candidacyStatus = candidacyStatus;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public LocalDate getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(LocalDate dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public int getIdApRequest() {
        return idApRequest;
    }

    public void setIdApRequest(int idApRequest) {
        this.idApRequest = idApRequest;
    }

    public String getDniPapPati() {
        return dniPapPati;
    }

    public void setDniPapPati(String dniPapPati) {
        this.dniPapPati = dniPapPati;
    }

    @Override
    public String toString() {
        return "Candidacy{" +
                "idCandidacy=" + idCandidacy +
                ", candidacyStatus=" + candidacyStatus +
                ", rejectedReason='" + rejectedReason + '\'' +
                ", dateLastModified=" + dateLastModified +
                ", idApRequest=" + idApRequest +
                ", dniPapPati='" + dniPapPati + '\'' +
                '}';
    }
}