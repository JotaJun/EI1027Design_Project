package es.uji.ei1027.SgOviProject.filters;

import java.util.List;

public class StatusFilter {
    String statusSel;
    List<String> statusList; // La lista de opciones (Pendent, Acceptat, Rebutjat, Totes)

    public String getStatusSel() {
        return statusSel;
    }

    public void setStatusSel(String statusSel) {
        this.statusSel = statusSel;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    @Override
    public String toString() {
        return "StatusFilter{" +
                "statusSel='" + statusSel + '\'' +
                ", statusList=" + statusList +
                '}';
    }
}
