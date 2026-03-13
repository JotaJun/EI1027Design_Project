package es.uji.ei1027.SgOviProject.model;

import es.uji.ei1027.SgOviProject.enums.Status;
import java.time.LocalDate;

public class Candidacy {
    private int idCandidacy;
    private Status status;
    private LocalDate dateLastModified;
    private int idApRequest;
    private String dniPapPati;

    public int getIdCandidacy() {
        return idCandidacy;
    }

    public void setIdCandidacy(int idCandidacy) {
        this.idCandidacy = idCandidacy;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
                ", status=" + status +
                ", dateLastModified=" + dateLastModified +
                ", idApRequest=" + idApRequest +
                ", dniPapPati='" + dniPapPati + '\'' +
                '}';
    }
}