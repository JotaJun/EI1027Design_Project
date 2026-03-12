package es.uji.ei1027.SgOviProject.dao;


import es.uji.ei1027.SgOviProject.enums.CandidacyStatus;
import es.uji.ei1027.SgOviProject.model.Candidacy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;

@Repository
public class CandidacyDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Añadir candidatura */
    public void addCandidacy(Candidacy candidacy) {
        jdbcTemplate.update("INSERT INTO Candidacy VALUES(?, ?, ?, ? ,?)",
                candidacy.getIdCandidacy(), candidacy.getStatus(), candidacy.getDateLastModified(), candidacy.getDniPapPati(), candidacy.getDateLastModified());

    }

    /* Borrar una candidatura con el objeto o con su id */
    public void deleteCandidacy(Candidacy candidacy) {
        jdbcTemplate.update("DELETE from Candidacy where idCandidacy = ?", candidacy.getIdCandidacy());
    }

    public void deleteCandidacy(String idCandidacy) {
        jdbcTemplate.update("DELETE from Candidacy where idCandidacy = ?", idCandidacy);
    }

    /* Actualizar una candidatura con el objeto o con su id */
    public void updateCandidacy(Candidacy candidacy) {
        jdbcTemplate.update("UPDATE Candidacy SET status=?, dateLastModified=?", candidacy.getStatus(), candidacy.getDateLastModified());
    }

    public void updateCandidacy(String idCandidacy, CandidacyStatus status, LocalDate dateLastModified) {
        jdbcTemplate.update("UPDATE Candidacy SET status=?, dateLastModified=?", status, dateLastModified);
    }

    /* Listar una o todas las candidaturas */

    public void getCandidacyById(int idCandidacy) {
        try {
            jdbcTemplate.queryForObject("SELECT * from candidacy where idCandidacy = ?", new CandidacyRowMapper(), idCandidacy);

        }catch (EmptyResultDataAccessException e){
            e.printStackTrace();
        }
    }

    public void getCandidacyByDniPapPati(String dniPapPati) {
        try{
            jdbcTemplate.queryForObject("SELECT * from candidacy where dniPapPati = ?", new CandidacyRowMapper(), dniPapPati);

        } catch(EmptyResultDataAccessException e){
            e.printStackTrace();
        }
    }

    public void getCandidacies(){
        try{
            jdbcTemplate.query("SELECT * from candidacy", new CandidacyRowMapper());

        } catch(EmptyResultDataAccessException e){
            e.printStackTrace();
        }
    }
}
