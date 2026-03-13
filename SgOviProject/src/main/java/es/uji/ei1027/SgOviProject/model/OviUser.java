package es.uji.ei1027.SgOviProject.model;

public class OviUser {

    private String dni;
    private String dniLegalGuardian;

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDniLegalGuardian() {
        return dniLegalGuardian;
    }

    public void setDniLegalGuardian(String dniLegalGuardian) {
        this.dniLegalGuardian = dniLegalGuardian;
    }

    @Override
    public String toString() {
        return "OviUser{" +
                "dni='" + dni + '\'' +
                ", dniLegalGuardian='" + dniLegalGuardian + '\'' +
                '}';
    }
}