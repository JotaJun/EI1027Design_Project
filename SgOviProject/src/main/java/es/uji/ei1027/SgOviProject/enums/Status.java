package es.uji.ei1027.SgOviProject.enums;

public enum Status {
    PENDING("Pendent"),
    ACCEPTED("Acceptat"),
    REJECTED("Rebutjat");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
