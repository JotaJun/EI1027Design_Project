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
        jdbcTemplate.update("INSERT INTO OviUser(dni, dniLegalGuardian) VALUES(?, ?)",
                oviUser.getDni(), oviUser.getDniLegalGuardian());
    }

    /* Borrar un usuario */
    public void deleteOviUser(OviUser oviUser) {
        jdbcTemplate.update("DELETE FROM OviUser WHERE dni=?", oviUser.getDni());
    }

    public void deleteOviUser(String dni) {
        jdbcTemplate.update("DELETE FROM OviUser WHERE dni=?", dni);
    }

    /* Actualizar un usuario */
    public void updateOviUser(OviUser oviUser) {
        jdbcTemplate.update("UPDATE OviUser SET dniLegalGuardian=? WHERE dni=?",
                oviUser.getDniLegalGuardian(), oviUser.getDni());
    }

    public void updateOviUser(String dni, String dniLegalGuardian) {
        jdbcTemplate.update("UPDATE OviUser SET dniLegalGuardian=? WHERE dni=?", dniLegalGuardian, dni);
    }

    /* Listar un usuario pasándole un objeto OviUser */
    public OviUser getOviUser(OviUser oviUser) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM OviUser WHERE dni=?",
                    new OviUserRowMapper(), oviUser.getDni());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /* Añadido: Listar un usuario pasándole el DNI*/
    public OviUser getOviUser(String dni) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM OviUser WHERE dni=?",
                    new OviUserRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<OviUser> getOviUsers() {
        try {
            return jdbcTemplate.query("SELECT * FROM OviUser", new OviUserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<OviUser>();
        }
    }
}