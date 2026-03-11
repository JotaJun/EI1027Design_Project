package es.uji.ei1027.SgOviProject.model;
import es.uji.ei1027.SgOviProject.enums.*;

import java.time.LocalDate;

public class Candidacy {
    private String idCandidacy;
    private CandidacyStatus status;
    private LocalDate date;

    @Override
    public String toString() {
        return "Candidacy{" +
                "idCandidacy='" + idCandidacy + '\'' +
                ", status=" + status +
                ", date=" + date +
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


}
