package es.uji.ei1027.SgOviProject.services;

import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.LegalGuardian;
import es.uji.ei1027.SgOviProject.model.PapPati;
import es.uji.ei1027.SgOviProject.model.OviUser;


public interface IntAccountSvc {
    void addPapPati(Account account, PapPati papPati);
    void addOviUser(Account account, OviUser oviUser);
    void updateOviUser(Account account, OviUser oviUser);
    void addLegalGuardian(Account account, LegalGuardian legalGuardian);
}
