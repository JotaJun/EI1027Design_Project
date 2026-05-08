package es.uji.ei1027.SgOviProject.services;

import es.uji.ei1027.SgOviProject.dao.CandidacyDao;
import es.uji.ei1027.SgOviProject.dao.PapPatiDao;
import es.uji.ei1027.SgOviProject.enums.CandidacyStatus;
import es.uji.ei1027.SgOviProject.model.AssistanceRequest;
import es.uji.ei1027.SgOviProject.model.Candidacy;
import es.uji.ei1027.SgOviProject.model.PapPati;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio que genera candidaturas automáticamente cuando una AssistanceRequest
 * es aprobada por el técnico. Filtra los PapPati de la BD según los criterios
 * de búsqueda de la solicitud y crea una Candidacy por cada uno que encaja.
 */
@Service
public class CandidacyGeneratorService {

    @Autowired
    private PapPatiDao papPatiDao;

    @Autowired
    private CandidacyDao candidacyDao;

    /**
     * Genera las candidaturas para una AssistanceRequest recién aprobada.
     *
     * @param request La solicitud de asistencia aceptada.
     * @return El número de nuevas candidaturas creadas.
     */
    public int generateCandidacies(AssistanceRequest request) {
        // 1. Obtener PapPatis que cumplen los criterios de búsqueda
        List<PapPati> candidates = papPatiDao.getCandidatePapPatis(request);

        // 2. Obtener los DNIs que ya tienen candidatura para esta AR (evitar duplicados)
        Set<String> existingDnis = candidacyDao.getCandidaciesByIdApRequest(request.getIdApRequest())
                .stream()
                .map(Candidacy::getDniPapPati)
                .collect(Collectors.toSet());

        // 3. Crear una candidatura por cada PapPati compatible que no tenga ya una
        int count = 0;
        for (PapPati papPati : candidates) {
            if (!existingDnis.contains(papPati.getDni())) {
                Candidacy candidacy = new Candidacy();
                candidacy.setIdApRequest(request.getIdApRequest());
                candidacy.setDniPapPati(papPati.getDni());
                candidacy.setCandidacyStatus(CandidacyStatus.TALKSNOTSTARTED);
                candidacy.setDateLastModified(LocalDate.now());
                candidacyDao.addCandidacy(candidacy);
                count++;
            }
        }
        return count;
    }
}
