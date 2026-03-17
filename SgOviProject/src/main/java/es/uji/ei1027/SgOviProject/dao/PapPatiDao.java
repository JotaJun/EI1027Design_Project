package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.enums.StaffType;
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
        jdbcTemplate.update("INSERT INTO PapPati(dni, status, stafftype, drivingLicense, initialAvailableDate, lastAvailableDate, training, yearsOfExperience, urlCv, deniedReason) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                papPati.getDni(),
                papPati.getStatus().name().toLowerCase(), // Enum a minúscula para la BD
                papPati.getStaffType().name(),
                papPati.isDrivingLicense(),
                papPati.getInitialAvailableDate(),
                papPati.getLastAvailableDate(),
                papPati.getTraining(),
                papPati.getYearsOfExperience(),
                papPati.getUrlCv(),
                papPati.getDeniedReason());
    }

    public void deletePapPati(String dni) {
        jdbcTemplate.update("DELETE FROM PapPati WHERE dni=?", dni);
    }

    public void deletePapPati(PapPati papPati) {
        jdbcTemplate.update("DELETE FROM PapPati WHERE dni=?", papPati.getDni());
    }

    // Actualiza menos clave primaria
    public void updatePapPati(PapPati papPati) {
        jdbcTemplate.update("UPDATE PapPati SET status=?, stafftype=?, drivingLicense=?, initialAvailableDate=?, lastAvailableDate=?, training=?, yearsOfExperience=?, urlCv=?, deniedReason=? WHERE dni=?",
                papPati.getStatus().name().toLowerCase(), // Enum a minúscula para la BD
                papPati.getStaffType().name(),
                papPati.isDrivingLicense(),
                papPati.getInitialAvailableDate(),
                papPati.getLastAvailableDate(),
                papPati.getTraining(),
                papPati.getYearsOfExperience(),
                papPati.getUrlCv(),
                papPati.getDeniedReason(),
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

    public List<PapPati> getPapPatisByType(StaffType staffType) {
        try {
            return jdbcTemplate.query("SELECT * FROM PapPati WHERE stafftype=?",
                    new PapPatiRowMapper(), staffType.name());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<PapPati>();
        }
    }
}