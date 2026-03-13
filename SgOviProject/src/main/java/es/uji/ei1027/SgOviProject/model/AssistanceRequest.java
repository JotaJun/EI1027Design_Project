package es.uji.ei1027.SgOviProject.model;

import es.uji.ei1027.SgOviProject.enums.StaffType;
import java.time.LocalDate;

public class AssistanceRequest {
    private int idApRequest;
    private LocalDate creationDate = LocalDate.now();

    // Parámetros de búsqueda
    private StaffType assistantType;
    private String gender;
    private String city;
    private Integer yearsExperience;
    private String specifiedTrainings;
    private LocalDate initialDateRequired;
    private int monthsRequired;
    // Fin parámetros búsqueda

    private String dniOviUser;
    private boolean approvedByGuardian;
    private String dniLegalGuardian;

    public int getIdApRequest() {
        return idApRequest;
    }

    public void setIdApRequest(int idApRequest) {
        this.idApRequest = idApRequest;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public StaffType getAssistantType() {
        return assistantType;
    }

    public void setAssistantType(StaffType assistantType) {
        this.assistantType = assistantType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getYearsExperience() {
        return yearsExperience;
    }

    public void setYearsExperience(Integer yearsExperience) {
        this.yearsExperience = yearsExperience;
    }

    public String getSpecifiedTrainings() {
        return specifiedTrainings;
    }

    public void setSpecifiedTrainings(String specifiedTrainings) {
        this.specifiedTrainings = specifiedTrainings;
    }

    public LocalDate getInitialDateRequired() {
        return initialDateRequired;
    }

    public void setInitialDateRequired(LocalDate initialDateRequired) {
        this.initialDateRequired = initialDateRequired;
    }

    public int getMonthsRequired() {
        return monthsRequired;
    }

    public void setMonthsRequired(int monthsRequired) {
        this.monthsRequired = monthsRequired;
    }

    public String getDniOviUser() {
        return dniOviUser;
    }

    public void setDniOviUser(String dniOviUser) {
        this.dniOviUser = dniOviUser;
    }

    public boolean isApprovedByGuardian() {
        return approvedByGuardian;
    }

    public void setApprovedByGuardian(boolean approvedByGuardian) {
        this.approvedByGuardian = approvedByGuardian;
    }

    public String getDniLegalGuardian() {
        return dniLegalGuardian;
    }

    public void setDniLegalGuardian(String dniLegalGuardian) {
        this.dniLegalGuardian = dniLegalGuardian;
    }

    @Override
    public String toString() {
        return "AssistanceRequest{" +
                "idApRequest=" + idApRequest +
                ", creationDate=" + creationDate +
                ", assistantType=" + assistantType +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", yearsExperience=" + yearsExperience +
                ", specifiedTrainings='" + specifiedTrainings + '\'' +
                ", initialDateRequired=" + initialDateRequired +
                ", monthsRequired=" + monthsRequired +
                ", dniOviUser='" + dniOviUser + '\'' +
                ", approvedByGuardian=" + approvedByGuardian +
                ", dniLegalGuardian='" + dniLegalGuardian + '\'' +
                '}';
    }
}