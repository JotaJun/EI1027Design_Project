package es.uji.ei1027.SgOviProject.services;

import es.uji.ei1027.SgOviProject.dao.*;
import es.uji.ei1027.SgOviProject.dto.ContractListAllDTO;
import es.uji.ei1027.SgOviProject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContractService {

    @Autowired
    private AssistanceRequestDao assistanceRequestDao;

    @Autowired
    private CandidacyDao candidacyDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private PapPatiDao papPatiDao;

    @Autowired
    private AccountDao accountDao;

    public List<ContractListAllDTO> listAllContractsFromUser(OviUser oviUser){

        List<AssistanceRequest> assistanceRequests = assistanceRequestDao.getAcceptedAssistanceRequestsByDni(oviUser.getDni());

        if (assistanceRequests.isEmpty()) return new ArrayList<>();

        List<Candidacy> candidacies = new ArrayList<>();

        for (AssistanceRequest req : assistanceRequests){
            candidacies.addAll(candidacyDao.getContractedCandidaciesByIdApRequest(req.getIdApRequest()));
        }

        if ( candidacies.isEmpty() ) return new ArrayList<>();

        List<Contract> contracts = new ArrayList<>();

        for (Candidacy c : candidacies){
            contracts.addAll(contractDao.getContractsByIdCandidacy(c.getIdCandidacy()));
        }

        List<ContractListAllDTO> contractsDto = new ArrayList<>();

        for (Contract c : contracts){
            Candidacy candidacy = candidacyDao.getCandidacyById(c.getIdCandidacy());
            PapPati papPati = papPatiDao.getPapPati(candidacy.getDniPapPati());
            Account accountPapPati = accountDao.getAccount(papPati.getDni());

            String papPatiName = accountPapPati.getName() + ' ' + accountPapPati.getSurname();
            contractsDto.add(new ContractListAllDTO(c, papPati.getStaffType(), papPatiName));
        }

        return contractsDto; // debería de haber al menos un contrato si llega aqui, si falla algo devolverá una lista vacía

    }
}
