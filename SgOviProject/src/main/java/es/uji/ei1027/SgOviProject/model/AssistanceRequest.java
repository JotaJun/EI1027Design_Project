package es.uji.ei1027.SgOviProject.model;

import es.uji.ei1027.SgOviProject.enums.StaffType;

import java.time.LocalDate;

public class AssistanceRequest {
    private int idApRequest;
    private LocalDate dateRequest = LocalDate.now(); // Valor por defecto en Java

    // Parámetros de búsqueda
    private StaffType assistantType;
    private String gender;
    private String city;
    private Integer yearsExperience; // Integer en vez de int para permitir null
    private String specifiedTrainings;
    private LocalDate initialDateRequired;
    private int monthsRequired;

    // Clave ajena
    private String dniOviUser;

    public int getIdApRequest() {
        return idApRequest;
    }

    public void setIdApRequest(int idApRequest) {
        this.idApRequest = idApRequest;
    }

    public LocalDate getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(LocalDate dateRequest) {
        this.dateRequest = dateRequest;
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

    @Override
    public String toString() {
        return "AssistanceRequest{" +
                "idApRequest=" + idApRequest +
                ", dateRequest=" + dateRequest +
                ", assistantType=" + assistantType +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", yearsExperience=" + yearsExperience +
                ", specifiedTrainings='" + specifiedTrainings + '\'' +
                ", initialDateRequired=" + initialDateRequired +
                ", monthsRequired=" + monthsRequired +
                ", dniOviUser='" + dniOviUser + '\'' +
                '}';
    }
}
