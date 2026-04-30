package es.uji.ei1027.SgOviProject.enums;

public enum CandidacyStatus {
    INTALKS("En converses"),
    TALKSENDED("Finalitzat"),
    TALKSNOTSTARTED("Pendent"),
    CONTRACTED("Contractat");

    private final String description;

    CandidacyStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
