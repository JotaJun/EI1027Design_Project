package es.uji.ei1027.SgOviProject.model;

public class LegalGuardian {
    private String dni;
    private String signatureCode;

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getSignatureCode() {
        return signatureCode;
    }

    public void setSignatureCode(String signatureCode) {
        this.signatureCode = signatureCode;
    }

    @Override
    public String toString() {
        return "LegalGuardian{" +
                "dni='" + dni + '\'' +
                ", signatureCode='" + signatureCode + '\'' +
                '}';
    }
}
