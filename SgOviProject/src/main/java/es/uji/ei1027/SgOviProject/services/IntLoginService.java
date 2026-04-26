package es.uji.ei1027.SgOviProject.services;

import es.uji.ei1027.SgOviProject.model.LoginDetails;

public interface IntLoginService {
    public Object authenticate(LoginDetails details, String dni);
}
