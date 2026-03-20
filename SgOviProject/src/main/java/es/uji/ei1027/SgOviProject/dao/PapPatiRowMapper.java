package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.enums.StaffType;
import es.uji.ei1027.SgOviProject.model.PapPati;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class PapPatiRowMapper implements RowMapper<PapPati> {

    public PapPati mapRow(ResultSet rs, int rowNum) throws SQLException {
        PapPati papPati = new PapPati();
        papPati.setDni(rs.getString("dni"));

        String typeStr = rs.getString("stafftype");
        if (typeStr != null) {
            papPati.setStaffType(StaffType.valueOf(typeStr));
        }else{
            throw new SQLException("staffType ha fallado.");
        }

        papPati.setDrivingLicense(rs.getBoolean("drivingLicense"));
        papPati.setInitialAvailableDate(rs.getObject("initialAvailableDate", LocalDate.class));
        papPati.setLastAvailableDate(rs.getObject("lastAvailableDate", LocalDate.class));
        papPati.setTraining(rs.getString("training"));
        papPati.setYearsOfExperience(rs.getInt("yearsOfExperience"));
        papPati.setUrlCv(rs.getString("urlCv"));

        return papPati;
    }
}