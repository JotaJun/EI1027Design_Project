package es.uji.ei1027.SgOviProject.enums;

public enum AccountType {
    OVIUSER("Persona usuària de l'OVI"),
    PAPPATI("Professional d'assistència (PAP/PATI)"),
    LEGALGUARDIAN("Tutor legal o garant"),
    TECHNICIAN("Personal tècnic de l'OVI");

    private final String description;

    AccountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
