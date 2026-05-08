package es.uji.ei1027.SgOviProject.comparator;

import es.uji.ei1027.SgOviProject.model.AssistanceRequest;

import java.util.Comparator;

public class AssistanceRequestComparator implements Comparator<AssistanceRequest> {

    @Override
    public int compare(AssistanceRequest a, AssistanceRequest b) {
        // Ordena de más reciente a más antigua
        int dateComparison = b.getCreationDate().compareTo(a.getCreationDate());

        if (dateComparison != 0) {
            return dateComparison;
        }

        return Integer.compare(b.getIdApRequest(), a.getIdApRequest());
    }
}
