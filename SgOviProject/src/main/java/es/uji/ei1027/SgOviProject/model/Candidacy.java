package es.uji.ei1027.SgOviProject.model;
import es.uji.ei1027.SgOviProject.enums.*;

import java.time.LocalDate;

public class Candidacy {
    private String idCandidacy;
    private CandidacyStatus status;
    private LocalDate dateLastModified;
    private int idApRequest;
    private String dniPapPati;

    @Override
    public String toString() {
        return "Candidacy{" +
                "idCandidacy='" + idCandidacy + '\'' +
                ", status=" + status +
                ", date=" + dateLastModified +
                ", idApRequest=" + idApRequest +
                ", dniPapPati='" + dniPapPati + '\'' +
                '}';
    }

    public String getIdCandidacy() {
        return idCandidacy;
    }

    public void setIdCandidacy(String idCandidacy) {
        this.idCandidacy = idCandidacy;
    }

    public CandidacyStatus getStatus() {
        return status;
    }

    public void setStatus(CandidacyStatus status) {
        this.status = status;
    }

    public LocalDate getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(LocalDate date) {
        this.dateLastModified = date;
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






}
