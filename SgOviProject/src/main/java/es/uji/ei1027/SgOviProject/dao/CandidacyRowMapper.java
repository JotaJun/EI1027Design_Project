package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.enums.Status;
import es.uji.ei1027.SgOviProject.model.Candidacy;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class CandidacyRowMapper implements RowMapper<Candidacy> {
    @Override
    public Candidacy mapRow(ResultSet rs, int rowNum) throws SQLException {
        Candidacy candidacy = new Candidacy();

        candidacy.setIdCandidacy(rs.getInt("idCandidacy"));
        candidacy.setDateLastModified(rs.getObject("dateLastModified", LocalDate.class));

        String statusStr = rs.getString("candidacyStatus");
        if (statusStr != null) {
            candidacy.setStatus(Status.valueOf(statusStr.toUpperCase()));
        }

        candidacy.setIdApRequest(rs.getInt("idApRequest"));
        candidacy.setDniPapPati(rs.getString("dniPapPati"));

        return candidacy;
    }
}