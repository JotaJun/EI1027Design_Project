package es.uji.ei1027.SgOviProject.filters;

import java.util.List;

public class CandidacyStatusFilter {
    String statusSel;

    public String getStatusSel() {
        return statusSel;
    }

    public void setStatusSel(String statusSel) {
        this.statusSel = statusSel;
    }

    @Override
    public String toString() {
        return "CandidacyStatusFilter{" +
                "statusSel='" + statusSel + '\'' +
                '}';
    }
}
