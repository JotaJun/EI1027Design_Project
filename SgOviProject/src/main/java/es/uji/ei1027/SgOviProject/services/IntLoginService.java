package es.uji.ei1027.SgOviProject.services;

import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.LoginDetails;

public interface IntLoginService {
    public boolean authenticate(LoginDetails details, String dni);
}
