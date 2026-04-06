package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.enums.AccountType;
import es.uji.ei1027.SgOviProject.model.LoginDetails;
import es.uji.ei1027.SgOviProject.model.Technician;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FakeTechnicianProvider implements TechnicianDao {
    final Map<String, Technician> knownTechnicians = new HashMap<>();

    public FakeTechnicianProvider(){
        BasicPasswordEncryptor passwordEncryptor =
                new BasicPasswordEncryptor();

        Technician tech1 = new Technician();
        tech1.setEmail("tecnic1@sgovi.com");
        tech1.setPassword(passwordEncryptor.encryptPassword("tecnicOvi1"));

        knownTechnicians.put(tech1.getEmail(), tech1);
    }

    public Technician getTechnicianByLoginDetails(LoginDetails details) {
        // Asegurarnos de que están intentando entrar como técnico
        if (details.getAccountType() != AccountType.TECHNICIAN) {
            return null;
        }

        Technician technician = knownTechnicians.get(details.getEmail().trim());
        if (technician == null) {
            return null; // El email no existe
        }

        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        if (passwordEncryptor.checkPassword(details.getPassword(), technician.getPassword())) {

            // Clon seguro sin devolver la contraseña a la sesión
            Technician safeTechnician = new Technician();
            safeTechnician.setEmail(technician.getEmail());
            return safeTechnician;
        }

        return null;
    }

}
