package es.uji.ei1027.SgOviProject.model;

public class OviUser {

    private String dni;
    private String legalGuardian;

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getLegalGuardian() {
        return legalGuardian;
    }

    public void setLegalGuardian(String legalGuardian) {
        this.legalGuardian = legalGuardian;
    }

    @Override
    public String toString() {
        return "OviUser{" +
                "dni='" + dni + '\'' +
                ", legalGuardian='" + legalGuardian + '\'' +
                '}';
    }
}
