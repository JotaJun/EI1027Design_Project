package es.uji.ei1027.SgOviProject.comparator;

import es.uji.ei1027.SgOviProject.model.AssistanceRequest;

import java.util.Comparator;

public class AssistanceRequestComparator implements Comparator<AssistanceRequest> {

    @Override
    public int compare(AssistanceRequest a, AssistanceRequest b) {
        // Ordena de más reciente a más antigua
        return b.getCreationDate().compareTo(a.getCreationDate());
    }
}
