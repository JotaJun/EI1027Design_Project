package es.uji.ei1027.SgOviProject.comparator;

import es.uji.ei1027.SgOviProject.dto.CandidacyDTO;

import java.util.Comparator;

public class CandidacyDTOComparator implements Comparator<CandidacyDTO> {

    @Override
    public int compare(CandidacyDTO a, CandidacyDTO b) {
        // Ordena de más reciente a más antigua

        int dateComparison = b.getCandidacy().getDateLastModified().compareTo(a.getCandidacy().getDateLastModified());

        if (dateComparison != 0) {
            return dateComparison;
        }

        return Integer.compare(b.getCandidacy().getIdCandidacy(), a.getCandidacy().getIdCandidacy());
    }
}
