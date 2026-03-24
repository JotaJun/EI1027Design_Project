package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.enums.StaffType;
import es.uji.ei1027.SgOviProject.enums.Status;
import es.uji.ei1027.SgOviProject.model.AssistanceRequest;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class AssistanceRequestRowMapper implements RowMapper<AssistanceRequest> {

    public AssistanceRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
        AssistanceRequest request = new AssistanceRequest();

        request.setIdApRequest(rs.getInt("idApRequest"));
        request.setCreationDate(rs.getObject("creationDate", LocalDate.class));

        String typeStr = rs.getString("assistantType");
        if (typeStr != null) {
            request.setAssistantType(StaffType.valueOf(typeStr));
        }

        request.setGender(rs.getString("gender"));
        request.setCity(rs.getString("city"));
        request.setDrivingLicense(rs.getObject("drivingLicense", Boolean.class));
        request.setYearsOfExperience(rs.getObject("yearsOfExperience", Integer.class));
        request.setInitialDateRequired(rs.getObject("initialDateRequired", LocalDate.class));
        request.setMonthsRequired(rs.getInt("monthsRequired"));

        String statusStr = rs.getString("status");
        if (statusStr != null) {
            request.setStatus(Status.valueOf(statusStr.toUpperCase()));
        }

        request.setDeniedReason(rs.getString("deniedReason"));
        request.setDniOviUser(rs.getString("dniOviUser"));
        request.setApprovedByGuardian(rs.getBoolean("approvedByGuardian"));
        request.setDniLegalGuardian(rs.getString("dniLegalGuardian"));

        return request;
    }
}