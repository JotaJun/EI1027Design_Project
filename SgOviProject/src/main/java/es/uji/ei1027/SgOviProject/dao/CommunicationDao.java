package es.uji.ei1027.SgOviProject.dao;


import es.uji.ei1027.SgOviProject.model.Communication;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class CommunicationDao {

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Añadir una comunicación */
    public void addCommunication(Communication communication) {
        jdbcTemplate.update("INSERT INTO Communcation VALUES(?, ?, ?)",
                communication.getDateCommunication(), communication.getInformation());
    }

    /* Borrar una comunicación con el objeto o con su id */
    public void deleteCommunication(Communication communication) {
        jdbcTemplate.update("DELETE from Communcation where idCandidacy=?", communication.getIdCandidacy());
    }

    public void deleteCommunication(int idCandidacy) {
        jdbcTemplate.update("DELETE from Communcation where idCandidacy=?", idCandidacy);

    }

    /* Actualizar la información de los chats */
    public void updateCommunication(Communication communication) {
        jdbcTemplate.update("UPDATE Communication SET information=? WHERE idCandidacy=?", communication.getInformation(), communication.getIdCandidacy());
    }

    public void updateCommunication(int idCandidacy, String information) {
        jdbcTemplate.update("UPDATE Communication SET information=? WHERE idCandidacy=?", information, idCandidacy);
    }

    /* Listar la comunicacion */

    public Communication getCommunication(int idCandidacy) {
        try{
            return jdbcTemplate.queryForObject("select * from Communication where idCandidacy=?", new CommunicationRowMapper(),idCandidacy);

        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }
}
