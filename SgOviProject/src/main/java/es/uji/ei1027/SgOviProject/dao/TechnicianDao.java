package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.model.LoginDetails;
import es.uji.ei1027.SgOviProject.model.Technician;

public interface TechnicianDao {
    Technician getTechnicianByLoginDetails(LoginDetails details);
}
