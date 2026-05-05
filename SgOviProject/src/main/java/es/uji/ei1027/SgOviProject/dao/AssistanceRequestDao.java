package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.model.AssistanceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AssistanceRequestDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addAssistanceRequest(AssistanceRequest request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO AssistanceRequest(creationDate, description, assistantType, gender, city, drivingLicense, yearsOfExperience, initialDateRequired, monthsRequired, status, deniedReason, dniOviUser, approvedByGuardian, dniLegalGuardian) " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new String[] {"idaprequest"});

            ps.setObject(1, request.getCreationDate());
            ps.setString(2, request.getDescription());
            ps.setString(3, request.getAssistantType() != null ? request.getAssistantType().name() : null);
            ps.setString(4, (request.getGender() != null) ? request.getGender().name() : null);
            ps.setString(5, request.getCity());
            ps.setObject(6, request.getDrivingLicense());
            ps.setObject(7, request.getYearsOfExperience());
            ps.setObject(8, request.getInitialDateRequired());
            ps.setInt(9, request.getMonthsRequired());
            ps.setString(10, request.getStatus() != null ? request.getStatus().name().toLowerCase() : "pending");
            ps.setString(11, request.getDeniedReason());
            ps.setString(12, request.getDniOviUser());
            ps.setObject(13, request.getApprovedByGuardian());

            ps.setString(14, request.getDniLegalGuardian());
            return ps;
        }, keyHolder);

        // Recuperamos el ID generado y lo seteamos en el objeto original
        if (keyHolder.getKey() != null) {
            request.setIdApRequest(keyHolder.getKey().intValue());
        }
    }

    public void deleteAssistanceRequest(int idApRequest) {
        jdbcTemplate.update("DELETE FROM AssistanceRequest WHERE idApRequest=?", idApRequest);
    }

    public void deleteAssistanceRequest(AssistanceRequest request) {
        jdbcTemplate.update("DELETE FROM AssistanceRequest WHERE idApRequest=?", request.getIdApRequest());
    }

    public void updateAssistanceRequest(AssistanceRequest request) {
        jdbcTemplate.update("UPDATE AssistanceRequest SET creationDate=?, description=?, assistantType=?, gender=?, city=?, drivingLicense=?, yearsOfExperience=?, initialDateRequired=?, monthsRequired=?, status=?, deniedReason=?, dniOviUser=?, approvedByGuardian=?, dniLegalGuardian=? WHERE idApRequest=?",
                request.getCreationDate(),
                request.getDescription(),
                request.getAssistantType().name(),
                (request.getGender() != null) ? request.getGender().name() : null,
                request.getCity(),
                request.getDrivingLicense(),
                request.getYearsOfExperience(),
                request.getInitialDateRequired(),
                request.getMonthsRequired(),
                request.getStatus().name().toLowerCase(),
                request.getDeniedReason(),
                request.getDniOviUser(),
                request.getApprovedByGuardian(),
                request.getDniLegalGuardian(),
                request.getIdApRequest());
    }

    public AssistanceRequest getAssistanceRequest(int idApRequest) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM AssistanceRequest WHERE idApRequest=?",
                    new AssistanceRequestRowMapper(), idApRequest);
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<AssistanceRequest> getAssistanceRequests() {
        try {
            return jdbcTemplate.query("SELECT * FROM AssistanceRequest",
                    new AssistanceRequestRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<AssistanceRequest>();
        }
    }

    public List<AssistanceRequest> getAssistanceRequestsByDni(String dniOviUser) {
        try {
            return jdbcTemplate.query("SELECT * FROM AssistanceRequest WHERE dniOviUser=?",
                    new AssistanceRequestRowMapper(), dniOviUser);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<AssistanceRequest>();
        }
    }

    public List<AssistanceRequest> getAssistanceRequestsByDniAndStatus(String dniOviUser, String status) {
        try {
            if (status.equals("Totes")) {
                return getAssistanceRequestsByDni(dniOviUser);
            }
            return jdbcTemplate.query("SELECT * FROM AssistanceRequest WHERE dniOviUser=? AND status=LOWER(?)",
                    new AssistanceRequestRowMapper(), dniOviUser, status);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<AssistanceRequest>();
        }
    }

    public List<AssistanceRequest> getPendingRequests() {
        try {
            return jdbcTemplate.query("SELECT * FROM AssistanceRequest WHERE status=?",
                    new AssistanceRequestRowMapper(), "pending");
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<AssistanceRequest>();
        }
    }
}