package es.uji.ei1027.SgOviProject.model;

import es.uji.ei1027.SgOviProject.enums.StaffType;
import es.uji.ei1027.SgOviProject.enums.Status;
import java.time.LocalDate;

public class AssistanceRequest {
    private int idApRequest;
    private LocalDate creationDate = LocalDate.now();
    private String description;

    // Parámetros de búsqueda
    private StaffType assistantType;
    private String gender;
    private String city;
    private Boolean drivingLicense;
    private Integer yearsOfExperience;
    private LocalDate initialDateRequired;
    private int monthsRequired;
    // Fin parámetros búsqueda

    private Status status = Status.PENDING;
    private String deniedReason;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(Boolean drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDeniedReason() {
        return deniedReason;
    }

    public void setDeniedReason(String deniedReason) {
        this.deniedReason = deniedReason;
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
                ", description='" + description + '\'' +
                ", assistantType=" + assistantType +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", drivingLicense=" + drivingLicense +
                ", yearsOfExperience=" + yearsOfExperience +
                ", initialDateRequired=" + initialDateRequired +
                ", monthsRequired=" + monthsRequired +
                ", status=" + status +
                ", deniedReason='" + deniedReason + '\'' +
                ", dniOviUser='" + dniOviUser + '\'' +
                ", approvedByGuardian=" + approvedByGuardian +
                ", dniLegalGuardian='" + dniLegalGuardian + '\'' +
                '}';
    }
}