package es.uji.ei1027.SgOviProject.model;

import java.time.LocalDate;

public class Contract {

    private int idContract;
    private LocalDate startDate;
    private LocalDate endDate;
    private int salary;
    private String schedule;

    @Override
    public String toString() {
        return "Contract{" +
                "idContract=" + idContract +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", salary=" + salary +
                ", schedule='" + schedule + '\'' +
                '}';
    }

    public int getIdContract() { return idContract; }

    public void setIdContract(int idContract) { this.idContract = idContract; }

    public LocalDate getStartDate() { return startDate; }

    public void setStartDate(LocalDate startDate) { this.startDate = startDate;}

    public LocalDate getEndDate() { return endDate; }

    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public int getSalary() { return salary; }

    public void setSalary(int salary) { this.salary = salary; }

    public String getSchedule() { return schedule; }

    public void setSchedule(String schedule) { this.schedule = schedule; }
}
