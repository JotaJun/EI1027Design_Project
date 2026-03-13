package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.model.LegalGuardian;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class LegalGuardianRowMapper implements RowMapper<LegalGuardian> {

    public LegalGuardian mapRow(ResultSet rs, int rowNum) throws SQLException {
        LegalGuardian legalGuardian = new LegalGuardian();

        legalGuardian.setDni(rs.getString("dni"));
        legalGuardian.setSignatureCode(rs.getString("signatureCode"));

        return legalGuardian;
    }
}