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
        jdbcTemplate.update(
                "INSERT INTO AssistanceRequest(creationDate, description, assistantType, gender, city, drivingLicense, yearsOfExperience, initialDateRequired, monthsRequired, status, deniedReason, dniOviUser, approvedByGuardian, dniLegalGuardian) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                request.getCreationDate(),
                request.getDescription(),
                request.getAssistantType().name(),
                request.getGender(),
                request.getCity(),
                request.getDrivingLicense(),
                request.getYearsOfExperience(),
                request.getInitialDateRequired(),
                request.getMonthsRequired(),
                request.getStatus() != null ? request.getStatus().name().toLowerCase() : "pending",
                request.getDeniedReason(),
                request.getDniOviUser(),
                request.getApprovedByGuardian(),
                request.getDniLegalGuardian()
        );
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
                request.getGender(),
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
}