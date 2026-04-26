package es.uji.ei1027.SgOviProject.enums;

public enum Gender {
    M("Masculí"),
    F("Femení"),
    X("No binari / Altre");

    private final String description;

    Gender(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}