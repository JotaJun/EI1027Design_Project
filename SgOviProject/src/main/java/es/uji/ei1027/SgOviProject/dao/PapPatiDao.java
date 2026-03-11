package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.model.PapPati;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PapPatiDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addPapPati(PapPati papPati) {
        jdbcTemplate.update("INSERT INTO PapPati(dni, stafftype, initialAvailableDate, lastAvailableDate, training, yearsExperience, urlCv) VALUES(?, ?, ?, ?, ?, ?, ?)",
                papPati.getDni(), papPati.getStaffType().name(), papPati.getInitialAvailableDate(),
                papPati.getLastAvailableDate(), papPati.getTraining(), papPati.getYearsExperience(),
                papPati.getUrlCv());
    }

    public void deletePapPati(String dni) {
        jdbcTemplate.update("DELETE FROM PapPati WHERE dni=?", dni);
    }

    public void deletePapPati(PapPati papPati) {
        jdbcTemplate.update("DELETE FROM PapPati WHERE dni=?", papPati.getDni());
    }

    // Actualiza menos clave primaria
    public void updatePapPati(PapPati papPati) {
        jdbcTemplate.update("UPDATE PapPati SET stafftype=?, initialAvailableDate=?, lastAvailableDate=?, training=?, yearsExperience=?, urlCv=? WHERE dni=?",
                papPati.getStaffType().name(), papPati.getInitialAvailableDate(), papPati.getLastAvailableDate(),
                papPati.getTraining(), papPati.getYearsExperience(), papPati.getUrlCv(),
                papPati.getDni());
    }

    public PapPati getPapPati(String dni) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM PapPati WHERE dni=?",
                    new PapPatiRowMapper(), dni);
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<PapPati> getPapPatis() {
        try {
            return jdbcTemplate.query("SELECT * FROM PapPati",
                    new PapPatiRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<PapPati>();
        }
    }

    public List<PapPati> getPapPatisByType(String staffType) {
        try {
            return jdbcTemplate.query("SELECT * FROM PapPati WHERE stafftype=?",
                    new PapPatiRowMapper(), staffType);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<PapPati>();
        }
    }
}
