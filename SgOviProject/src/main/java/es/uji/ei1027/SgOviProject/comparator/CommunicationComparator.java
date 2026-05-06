package es.uji.ei1027.SgOviProject.comparator;

import es.uji.ei1027.SgOviProject.model.Communication;

import java.util.Comparator;

public class CommunicationComparator implements Comparator<Communication> {
    @Override
    public int compare(Communication a, Communication b) {
        // Ordena de más antigua a más reciente
        // Comparamos por fecha
        int dateComparison = a.getDateCommunication().compareTo(b.getDateCommunication());

        // Si las fechas son iguales comparamos por ID
        if (dateComparison == 0) {
            return Integer.compare(a.getIdCommunication(), b.getIdCommunication());
        }

        return dateComparison;
    }
}
