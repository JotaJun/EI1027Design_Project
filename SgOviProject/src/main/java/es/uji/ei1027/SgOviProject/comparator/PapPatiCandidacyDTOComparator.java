package es.uji.ei1027.SgOviProject.comparator;

import es.uji.ei1027.SgOviProject.dto.PapPatiCandidacyDTO;

import java.util.Comparator;

public class PapPatiCandidacyDTOComparator implements Comparator<PapPatiCandidacyDTO> {

    @Override
    public int compare(PapPatiCandidacyDTO a, PapPatiCandidacyDTO b) {
        // Ordena de más reciente a más antigua
        int dateComparison = b.getCandidacy().getDateLastModified().compareTo(a.getCandidacy().getDateLastModified());

        if (dateComparison != 0) {
            return dateComparison;
        }

        return Integer.compare(b.getCandidacy().getIdCandidacy(), a.getCandidacy().getIdCandidacy());
    }
}
