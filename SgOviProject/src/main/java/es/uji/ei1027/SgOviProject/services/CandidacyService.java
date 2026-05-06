package es.uji.ei1027.SgOviProject.services;

import es.uji.ei1027.SgOviProject.dao.*;
import es.uji.ei1027.SgOviProject.dto.CandidacyDTO;
import es.uji.ei1027.SgOviProject.dto.PapPatiCandidacyDTO;
import es.uji.ei1027.SgOviProject.enums.CandidacyStatus;
import es.uji.ei1027.SgOviProject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CandidacyService {

    @Autowired
    private CandidacyDao candidacyDao;

    @Autowired
    private PapPatiDao papPatiDao;

    @Autowired
    private OviUserDao oviUserDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AssistanceRequestDao assistanceRequestDao;

    public List<CandidacyDTO> getCandidaciesWithDetailsByIdApRequest(int idApRequest) {
        List<Candidacy> basicCandidacies = candidacyDao.getCandidaciesByIdApRequest(idApRequest);
        List<CandidacyDTO> candidaciesInfo = new ArrayList<>();

        for (Candidacy candidacy : basicCandidacies) {
            String dni = candidacy.getDniPapPati();

            PapPati papPati = papPatiDao.getPapPati(dni);
            Account account = accountDao.getAccount(dni);

            candidaciesInfo.add(new CandidacyDTO(candidacy, papPati, account));
        }

        return candidaciesInfo;
    }

    public List<CandidacyDTO> getCandidaciesWithDetailsByIdApRequestAndStatus(int idApRequest, String status) {
        List<Candidacy> candidacies;

        if (status == null || status.equals("Totes")) {
            candidacies = candidacyDao.getCandidaciesByIdApRequest(idApRequest);
        } else {
            candidacies = candidacyDao.getCandidaciesByIdApRequestAndStatus(idApRequest, status);
        }

        List<CandidacyDTO> candidaciesInfo = new ArrayList<>();

        for (Candidacy candidacy : candidacies) {
            String dni = candidacy.getDniPapPati();

            PapPati papPati = papPatiDao.getPapPati(dni);
            Account account = accountDao.getAccount(dni);

            candidaciesInfo.add(new CandidacyDTO(candidacy, papPati, account));
        }

        return candidaciesInfo;
    }

    public CandidacyDTO getCandidacyDetailByIdCandidacy(int idCandidacy) {
        Candidacy candidacy = candidacyDao.getCandidacyById(idCandidacy);

        String dni = candidacy.getDniPapPati();

        PapPati papPati = papPatiDao.getPapPati(dni);
        Account account = accountDao.getAccount(dni);

        return new CandidacyDTO(candidacy, papPati, account);
    }

    public boolean isCandidacyFromOviUser(int idCandidacy, OviUser oviUser){
        List<AssistanceRequest> assistanceRequests = assistanceRequestDao.getAssistanceRequestsByDni(oviUser.getDni());

        if (assistanceRequests.isEmpty()) return false;

        for (AssistanceRequest req : assistanceRequests){
            if (req.getIdApRequest() == idCandidacy)
                return true;
        }
        return false;
    }

    public void contractDone(Contract contract){
        Candidacy candidacy = candidacyDao.getCandidacyById(contract.getIdCandidacy());

        candidacy.setCandidacyStatus(CandidacyStatus.CONTRACTED);

        candidacyDao.updateCandidacy(candidacy);
    }

    public List<PapPatiCandidacyDTO> getPapPatiCandidaciesWithDetails(String dniPapPati, String status) {
        List<Candidacy> candidacies;

        // Si el estado es nulo o "Totes", traemos todas las de ese PapPati
        if (status == null || status.equals("Totes")) {
            candidacies = candidacyDao.getCandidaciesByStaffDni(dniPapPati);
        } else {
            candidacies = candidacyDao.getCandidaciesByStaffDniAndStatus(dniPapPati, status);
        }

        List<PapPatiCandidacyDTO> candidaciesInfo = new ArrayList<>();

        for (Candidacy candidacy : candidacies) {
            // A partir de la candidatura, sacamos la petición original
            AssistanceRequest request = assistanceRequestDao.getAssistanceRequest(candidacy.getIdApRequest());

            String dniSolicitante = request.getDniOviUser();

            // 3. Recuperamos ambas partes de la identidad del usuario
            OviUser oviUser = oviUserDao.getOviUser(dniSolicitante);
            Account account = accountDao.getAccount(dniSolicitante);

            // Montamos el DTO y lo añadimos a la lista
            candidaciesInfo.add(new PapPatiCandidacyDTO(candidacy, request, oviUser, account));
        }

        return candidaciesInfo;
    }

    public PapPatiCandidacyDTO getPapPatiCandidacyDetail(int idCandidacy) {
        Candidacy candidacy = candidacyDao.getCandidacyById(idCandidacy);

        if (candidacy == null) return null;

        AssistanceRequest request = assistanceRequestDao.getAssistanceRequest(candidacy.getIdApRequest());
        String dniSolicitante = request.getDniOviUser();

        OviUser oviUser = oviUserDao.getOviUser(dniSolicitante);
        Account account = accountDao.getAccount(dniSolicitante);

        return new PapPatiCandidacyDTO(candidacy, request, oviUser, account);
    }
}
