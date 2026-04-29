package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.model.Communication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommunicationDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Añadir una comunicación */
    public void addCommunication(Communication communication) {
        jdbcTemplate.update("INSERT INTO Communication (idCandidacy, information, transmitterName) VALUES(?, ?, ?)",
                communication.getIdCandidacy(),
                communication.getInformation(), communication.getTransmitterName());
    }

    /* Borrar una comunicación por su PK (idCommunication) */
    public void deleteCommunication(Communication communication) {
        jdbcTemplate.update("DELETE FROM Communication WHERE idCommunication=?", communication.getIdCommunication());
    }

    public void deleteCommunication(int idCommunication) {
        jdbcTemplate.update("DELETE FROM Communication WHERE idCommunication=?", idCommunication);
    }

    /* Actualizar la comunicación por su PK */
    public void updateCommunication(Communication communication) {
        jdbcTemplate.update("UPDATE Communication SET idCandidacy=?, information=?, transmitterName=? WHERE idCommunication=?",
                communication.getIdCandidacy(),
                communication.getInformation(), communication.getTransmitterName(),
                communication.getIdCommunication());
    }

    // Actualiza solo la información sabiendo el id de la comunicación
    public void updateCommunication(int idCommunication, String information) {
        jdbcTemplate.update("UPDATE Communication SET information=? WHERE idCommunication=?", information, idCommunication);
    }

    /* Listar una comunicación específica por su ID */
    public Communication getCommunication(int idCommunication) {
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM Communication WHERE idCommunication=?",
                    new CommunicationRowMapper(), idCommunication);
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    /* Listar TODAS las comunicaciones */
    public List<Communication> getCommunications() {
        try {
            return jdbcTemplate.query("SELECT * FROM Communication", new CommunicationRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Communication>();
        }
    }

    /* Listar el historial de chats/comunicaciones de una candidatura en concreto */
    public List<Communication> getCommunicationsByCandidacy(int idCandidacy) {
        try {
            return jdbcTemplate.query("SELECT * FROM Communication WHERE idCandidacy=?",
                    new CommunicationRowMapper(), idCandidacy);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Communication>();
        }
    }
}