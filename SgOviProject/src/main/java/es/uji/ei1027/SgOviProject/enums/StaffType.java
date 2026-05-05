package es.uji.ei1027.SgOviProject.enums;

public enum StaffType {
    PAP("Professional d'Assistència Personal"),
    PATI("Professional d'Assistència Terapèutica Infantil");

    private final String description;

    StaffType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
