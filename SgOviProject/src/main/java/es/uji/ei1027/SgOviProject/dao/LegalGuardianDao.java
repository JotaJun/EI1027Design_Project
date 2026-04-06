package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.model.LegalGuardian;
import es.uji.ei1027.SgOviProject.model.OviUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LegalGuardianDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addLegalGuardian(LegalGuardian legalGuardian) {
        jdbcTemplate.update("INSERT INTO LegalGuardian(dni, signatureCode) VALUES(?, ?)",
                legalGuardian.getDni(), legalGuardian.getSignatureCode());
    }

    public void deleteLegalGuardian(String dni) {
        jdbcTemplate.update("DELETE FROM LegalGuardian WHERE dni=?", dni);
    }

    public void deleteLegalGuardian(LegalGuardian legalGuardian) {
        jdbcTemplate.update("DELETE FROM LegalGuardian WHERE dni=?", legalGuardian.getDni());
    }

    // Actualiza to.do MENOS dni (clave primaria)
    public void updateLegalGuardian(LegalGuardian legalGuardian) {
        jdbcTemplate.update("UPDATE LegalGuardian SET signatureCode=? WHERE dni=?",
                legalGuardian.getSignatureCode(),
                legalGuardian.getDni());
    }

    public LegalGuardian getLegalGuardian(String dni) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM LegalGuardian WHERE dni=?",
                    new LegalGuardianRowMapper(), dni);
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<LegalGuardian> getLegalGuardians() {
        try {
            return jdbcTemplate.query("SELECT * FROM LegalGuardian",
                    new LegalGuardianRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<LegalGuardian>();
        }
    }
}