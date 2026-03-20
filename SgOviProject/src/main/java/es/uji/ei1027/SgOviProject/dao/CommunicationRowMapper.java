package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.model.Communication;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class CommunicationRowMapper implements RowMapper<Communication> {
    @Override
    public Communication mapRow(ResultSet rs, int rowNum) throws SQLException {
        Communication communication = new Communication();

        communication.setIdCommunication(rs.getInt("idCommunication"));
        communication.setIdCandidacy(rs.getInt("idCandidacy"));
        communication.setDateCommunication(rs.getObject("dateCommunication", LocalDate.class));
        communication.setInformation(rs.getString("information"));
        communication.setTransmitterName(rs.getString("transmitterName"));

        return communication;
    }
}