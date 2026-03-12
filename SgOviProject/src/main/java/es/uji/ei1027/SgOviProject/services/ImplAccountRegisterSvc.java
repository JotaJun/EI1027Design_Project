package es.uji.ei1027.SgOviProject.services;

import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.dao.OviUserDao;
import es.uji.ei1027.SgOviProject.dao.PapPatiDao;
import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.OviUser;
import es.uji.ei1027.SgOviProject.model.PapPati;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional  // Mirar documentación Spring lo que significa
public class ImplAccountRegisterSvc implements  IntAccountRegisterSvc{

    @Autowired
    AccountDao accountDao;

    @Autowired
    PapPatiDao papPatiDao;

    @Autowired
    OviUserDao oviUserDao;


    @Override
    public void addPapPati(Account account, PapPati papPati) {
        accountDao.addAccount(account);

        // Si papPati no tiene el dni, se lo asignamos
        papPati.setDni(account.getDni());

        papPatiDao.addPapPati(papPati);
    }



    @Override
    public void addOviUser(Account account, OviUser oviUser) {
        accountDao.addAccount(account);

        // Si oviUser no tiene el dni, se lo asignamos
        oviUser.setDni(account.getDni());

        oviUserDao.addOviUser(oviUser);
    }
}
