package es.uji.ei1027.SgOviProject.comparator;

import es.uji.ei1027.SgOviProject.model.AssistanceRequest;
import es.uji.ei1027.SgOviProject.model.Contract;

import java.util.Comparator;

public class ContractComparator implements Comparator<Contract> {

    @Override
    public int compare(Contract a, Contract b) {
        // Ordena de más reciente a más antigua
        return b.getCreationDate().compareTo(a.getCreationDate());
    }
}
