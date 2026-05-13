package es.uji.ei1027.SgOviProject.filters;

public class AccountTypeFilter {
    private String typeSel;

    public String getTypeSel() { return typeSel; }
    public void setTypeSel(String typeSel) { this.typeSel = typeSel; }

    @Override
    public String toString() {
        return "AccountTypeFilter{typeSel='" + typeSel + "'}";
    }
}
