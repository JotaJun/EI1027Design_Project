package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.enums.StaffType;
import es.uji.ei1027.SgOviProject.model.AssistanceRequest;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class AssistanceRequestRowMapper implements RowMapper<AssistanceRequest> {

    public AssistanceRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
        AssistanceRequest request = new AssistanceRequest();

        request.setIdApRequest(rs.getInt("idApRequest"));
        request.setDateRequest(rs.getObject("dateRequest", LocalDate.class));

        String typeStr = rs.getString("assistantType");
        if (typeStr != null) {
            request.setAssistantType(StaffType.valueOf(typeStr));
        }

        request.setGender(rs.getString("gender"));
        request.setCity(rs.getString("city"));
        request.setYearsExperience(rs.getObject("yearsExperience", Integer.class));
        request.setSpecifiedTrainings(rs.getString("specifiedTrainings"));
        request.setInitialDateRequired(rs.getObject("initialDateRequired", LocalDate.class));
        request.setMonthsRequired(rs.getInt("monthsRequired"));

        request.setDniOviUser(rs.getString("dniOviUser"));

        return request;
    }
}