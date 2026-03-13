package es.uji.ei1027.SgOviProject.model;

import java.time.LocalDate;

public class Communication {
    private int idCommunication;
    private int idCandidacy;
    private LocalDate dateCommunication;
    private String information;

    public int getIdCommunication() {
        return idCommunication;
    }

    public void setIdCommunication(int idCommunication) {
        this.idCommunication = idCommunication;
    }

    public int getIdCandidacy() {
        return idCandidacy;
    }

    public void setIdCandidacy(int idCandidacy) {
        this.idCandidacy = idCandidacy;
    }

    public LocalDate getDateCommunication() {
        return dateCommunication;
    }

    public void setDateCommunication(LocalDate dateCommunication) {
        this.dateCommunication = dateCommunication;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Override
    public String toString() {
        return "Communication{" +
                "idCommunication=" + idCommunication +
                ", idCandidacy=" + idCandidacy +
                ", dateCommunication=" + dateCommunication +
                ", information='" + information + '\'' +
                '}';
    }
}