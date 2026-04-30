package es.uji.ei1027.SgOviProject.services;

import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.dao.CandidacyDao;
import es.uji.ei1027.SgOviProject.dao.PapPatiDao;
import es.uji.ei1027.SgOviProject.dto.CandidacyDTO;
import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.Candidacy;
import es.uji.ei1027.SgOviProject.model.PapPati;
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
    private AccountDao accountDao;

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
        List<Candidacy> basicCandidacies = candidacyDao.getCandidaciesByIdApRequestAndStatus(idApRequest, status);
        List<CandidacyDTO> candidaciesInfo = new ArrayList<>();

        for (Candidacy candidacy : basicCandidacies) {
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
}
