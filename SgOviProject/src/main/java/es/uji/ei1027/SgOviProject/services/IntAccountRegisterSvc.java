package es.uji.ei1027.SgOviProject.services;

import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.PapPati;
import es.uji.ei1027.SgOviProject.model.OviUser;


public interface IntAccountRegisterSvc {
    void addPapPati(Account account, PapPati papPati);
    void addOviUser(Account account, OviUser oviUser);
}
