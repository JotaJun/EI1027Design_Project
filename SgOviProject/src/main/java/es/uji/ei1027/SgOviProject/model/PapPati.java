package es.uji.ei1027.SgOviProject.model;

import es.uji.ei1027.SgOviProject.enums.StaffType;
import es.uji.ei1027.SgOviProject.enums.Status;

import java.time.LocalDate;

public class PapPati {
    private String dni;
    private Status status;
    private StaffType staffType;
    private boolean drivingLicense;
    private LocalDate initialAvailableDate;
    private LocalDate lastAvailableDate;
    private String training;
    private int yearsOfExperience;
    private String urlCv;
    private String deniedReason;

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public StaffType getStaffType() {
        return staffType;
    }

    public void setStaffType(StaffType staffType) {
        this.staffType = staffType;
    }

    public boolean isDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(boolean drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public LocalDate getInitialAvailableDate() {
        return initialAvailableDate;
    }

    public void setInitialAvailableDate(LocalDate initialAvailableDate) {
        this.initialAvailableDate = initialAvailableDate;
    }

    public LocalDate getLastAvailableDate() {
        return lastAvailableDate;
    }

    public void setLastAvailableDate(LocalDate lastAvailableDate) {
        this.lastAvailableDate = lastAvailableDate;
    }

    public String getTraining() {
        return training;
    }

    public void setTraining(String training) {
        this.training = training;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getUrlCv() {
        return urlCv;
    }

    public void setUrlCv(String urlCv) {
        this.urlCv = urlCv;
    }

    public String getDeniedReason() {
        return deniedReason;
    }

    public void setDeniedReason(String deniedReason) {
        this.deniedReason = deniedReason;
    }

    @Override
    public String toString() {
        return "PapPati{" +
                "dni='" + dni + '\'' +
                ", status=" + status +
                ", staffType=" + staffType +
                ", drivingLicense=" + drivingLicense +
                ", initialAvailableDate=" + initialAvailableDate +
                ", lastAvailableDate=" + lastAvailableDate +
                ", training='" + training + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                ", urlCv='" + urlCv + '\'' +
                ", deniedReason='" + deniedReason + '\'' +
                '}';
    }
}