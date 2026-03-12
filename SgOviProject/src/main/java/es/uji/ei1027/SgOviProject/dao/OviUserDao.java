package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.model.OviUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OviUserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Añadir un usuario */
    public void addOviUser(OviUser oviUser) {
        jdbcTemplate.update("INSERT into OviUser VALUES(?, ?)",  oviUser.getDni(), oviUser.getLegalGuardian());
    }

    /* Borrar un usuario */
    public void deleteOviUser(OviUser oviUser) {
        jdbcTemplate.update("DELETE from OviUser where dni=?", oviUser.getDni());
    }

    public void deleteOviUser(String dni) {
        jdbcTemplate.update("DELETE from OviUser where dni=?", dni);
    }

    /* Actualizar un usuario (puede ser que el legalGuardian cambie??) */

    public void updateOviUser(OviUser oviUser) {
        jdbcTemplate.update("UPDATE OviUser SET legalGuardian=? WHERE dni=?", oviUser.getLegalGuardian(), oviUser.getDni());
    }

    public void updateOviUser(String dni, String legalGuardian) {
        jdbcTemplate.update("UPDATE OviUser SET legalGuardian=? WHERE dni=?", legalGuardian, dni);
    }


    /* Listar uno o varios usuarios */
    public OviUser getOviUser(OviUser oviUser) {
        try {
            return jdbcTemplate.queryForObject("SELECT from OviUser WHERE dni=?", new OviUserRowMapper(), oviUser.getDni());

        }catch (EmptyResultDataAccessException e){
            return null;

        }
    }

    public List<OviUser> getOviUsers() {
        try{
            return jdbcTemplate.query("SELECT * FROM OviUser", new OviUserRowMapper());

        } catch (EmptyResultDataAccessException e){
            return new ArrayList<OviUser>();
        }
    }
}
