package es.uji.ei1027.SgOviProject.services;

import es.uji.ei1027.SgOviProject.dao.LegalGuardianDao;
import es.uji.ei1027.SgOviProject.dao.OviUserDao;
import es.uji.ei1027.SgOviProject.dao.PapPatiDao;
import es.uji.ei1027.SgOviProject.model.LoginDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImplLoginService implements  IntLoginService{

    @Autowired
    PapPatiDao papPatiDao;

    @Autowired
    OviUserDao oviUserDao;

    @Autowired
    LegalGuardianDao legalGuardianDao;

    @Override
    public Object authenticate(LoginDetails details, String dni) {

        return switch (details.getAccountType()) {
            case OVIUSER -> oviUserDao.getOviUser(dni);
            case PAPPATI -> papPatiDao.getPapPati(dni);
            case LEGALGUARDIAN -> legalGuardianDao.getLegalGuardian(dni);
            default -> null;
        };

    }
}
