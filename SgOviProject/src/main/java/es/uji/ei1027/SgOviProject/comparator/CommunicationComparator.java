package es.uji.ei1027.SgOviProject.comparator;

import es.uji.ei1027.SgOviProject.model.AssistanceRequest;
import es.uji.ei1027.SgOviProject.model.Communication;

import java.util.Comparator;

public class CommunicationComparator implements Comparator<Communication> {
    @Override
    public int compare(Communication a, Communication b) {
        // Ordena de más reciente a más antigua
        return b.getDateCommunication().compareTo(a.getDateCommunication());
    }
}
