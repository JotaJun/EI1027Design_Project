package es.uji.ei1027.SgOviProject.model;

import java.time.LocalDate;

public class Contract {

    private int idContract;
    private int idCandidacy;
    private LocalDate startDate;
    private LocalDate endDate;
    private double hourlySalary;
    private String schedule;
    private String urlDocument;

    public int getIdContract() {
        return idContract;
    }

    public void setIdContract(int idContract) {
        this.idContract = idContract;
    }

    public int getIdCandidacy() {
        return idCandidacy;
    }

    public void setIdCandidacy(int idCandidacy) {
        this.idCandidacy = idCandidacy;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getHourlySalary() {
        return hourlySalary;
    }

    public void setHourlySalary(double hourlySalary) {
        this.hourlySalary = hourlySalary;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getUrlDocument() {
        return urlDocument;
    }

    public void setUrlDocument(String urlDocument) {
        this.urlDocument = urlDocument;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "idContract=" + idContract +
                ", idCandidacy=" + idCandidacy +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", hourlySalary=" + hourlySalary +
                ", schedule='" + schedule + '\'' +
                ", urlDocument='" + urlDocument + '\'' +
                '}';
    }
}