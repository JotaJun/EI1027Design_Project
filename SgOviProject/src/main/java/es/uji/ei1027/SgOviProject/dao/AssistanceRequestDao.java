package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.model.AssistanceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AssistanceRequestDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // !!! SIN idApRequest porque es SERIAL !!!
    public void addAssistanceRequest(AssistanceRequest request) {
        jdbcTemplate.update("INSERT INTO AssistanceRequest(dateRequest, assistantType, gender, city, yearsExperience, specifiedTrainings, initialDateRequired, monthsRequired, dniOviUser) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)",
                request.getDateRequest(),
                request.getAssistantType().name(),
                request.getGender(),
                request.getCity(),
                request.getYearsExperience(),
                request.getSpecifiedTrainings(),
                request.getInitialDateRequired(),
                request.getMonthsRequired(),
                request.getDniOviUser());
    }

    public void deleteAssistanceRequest(int idApRequest) {
        jdbcTemplate.update("DELETE FROM AssistanceRequest WHERE idApRequest=?", idApRequest);
    }

    public void deleteAssistanceRequest(AssistanceRequest request) {
        jdbcTemplate.update("DELETE FROM AssistanceRequest WHERE idApRequest=?", request.getIdApRequest());
    }

    public void updateAssistanceRequest(AssistanceRequest request) {
        jdbcTemplate.update("UPDATE AssistanceRequest SET dateRequest=?, assistantType=?, gender=?, city=?, yearsExperience=?, specifiedTrainings=?, initialDateRequired=?, monthsRequired=?, dniOviUser=? WHERE idApRequest=?",
                request.getDateRequest(),
                request.getAssistantType().name(),
                request.getGender(),
                request.getCity(),
                request.getYearsExperience(),
                request.getSpecifiedTrainings(),
                request.getInitialDateRequired(),
                request.getMonthsRequired(),
                request.getDniOviUser(),
                request.getIdApRequest()); // El ID va al final para el WHERE
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