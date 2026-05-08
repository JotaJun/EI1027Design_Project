package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.enums.StaffType;
import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.AssistanceRequest;
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
        jdbcTemplate.update("INSERT INTO PapPati(dni, stafftype, drivingLicense, initialAvailableDate, lastAvailableDate, training, yearsOfExperience, urlCv) VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                papPati.getDni(),
                papPati.getStaffType().name(),
                papPati.isDrivingLicense(),
                papPati.getInitialAvailableDate(),
                papPati.getLastAvailableDate(),
                papPati.getTraining(),
                papPati.getYearsOfExperience(),
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
        jdbcTemplate.update("UPDATE PapPati SET stafftype=?, drivingLicense=?, initialAvailableDate=?, lastAvailableDate=?, training=?, yearsOfExperience=?, urlCv=? WHERE dni=?",
                papPati.getStaffType().name(),
                papPati.isDrivingLicense(),
                papPati.getInitialAvailableDate(),
                papPati.getLastAvailableDate(),
                papPati.getTraining(),
                papPati.getYearsOfExperience(),
                papPati.getUrlCv(),
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

    /**
     * Devuelve los PapPati cuya cuenta está aceptada y que cumplen los criterios
     * de búsqueda de la AssistanceRequest dada.
     * Filtros aplicados:
     *  - staffType: siempre obligatorio.
     *  - gender: solo si la AR lo especifica (no es null).
     *  - drivingLicense: solo si la AR lo requiere (true); si es null o false, no filtra.
     *  - yearsOfExperience: solo si la AR lo especifica; el PapPati debe tener >= ese valor.
     */
    public List<PapPati> getCandidatePapPatis(AssistanceRequest request) {
        StringBuilder sql = new StringBuilder(
                "SELECT p.* FROM PapPati p " +
                "JOIN Account a ON p.dni = a.dni " +
                "WHERE a.status = 'accepted' " +
                "AND p.stafftype = ? "
        );
        List<Object> params = new ArrayList<>();
        params.add(request.getAssistantType().name());

        // Filtro de género (opcional: solo si la AR especifica preferencia)
        if (request.getGender() != null) {
            sql.append("AND a.gender = ? ");
            params.add(request.getGender().name());
        }

        // Filtro de carnet de conducir (solo si la AR lo exige)
        if (Boolean.TRUE.equals(request.getDrivingLicense())) {
            sql.append("AND p.drivingLicense = true ");
        }

        // Filtro de años de experiencia mínima (opcional)
        if (request.getYearsOfExperience() != null) {
            sql.append("AND p.yearsOfExperience >= ? ");
            params.add(request.getYearsOfExperience());
        }

        try {
            return jdbcTemplate.query(sql.toString(), new PapPatiRowMapper(), params.toArray());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }
}