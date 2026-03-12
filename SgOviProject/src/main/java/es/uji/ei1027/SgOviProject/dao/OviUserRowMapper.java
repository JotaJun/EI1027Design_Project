package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.model.OviUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class OviUserRowMapper implements RowMapper<OviUser> {
    @Override
    public OviUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        OviUser oviUser = new OviUser();
        oviUser.setDni(rs.getString("dni"));
        oviUser.setLegalGuardian(rs.getString("legalGuardian"));
        return oviUser;
    }
}
