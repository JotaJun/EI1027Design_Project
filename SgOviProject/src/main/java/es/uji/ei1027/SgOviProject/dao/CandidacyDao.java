package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.enums.CandidacyStatus;
import es.uji.ei1027.SgOviProject.model.Candidacy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CandidacyDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Añadir candidatura */
    public void addCandidacy(Candidacy candidacy) {
        jdbcTemplate.update("INSERT INTO Candidacy (dateLastModified, candidacyStatus, rejectedReason, idApRequest, dniPapPati) VALUES(?, ?, ?, ?, ?)",
                candidacy.getDateLastModified(),
                candidacy.getCandidacyStatus() != null ? candidacy.getCandidacyStatus().name().toLowerCase() : "talksnotstarted", // Manejo de nulos preventivo
                candidacy.getRejectedReason(),
                candidacy.getIdApRequest(),
                candidacy.getDniPapPati());
    }

    /* Borrar una candidatura con el objeto o con su id */
    public void deleteCandidacy(Candidacy candidacy) {
        jdbcTemplate.update("DELETE FROM Candidacy WHERE idCandidacy=?", candidacy.getIdCandidacy());
    }

    public void deleteCandidacy(int idCandidacy) {
        jdbcTemplate.update("DELETE FROM Candidacy WHERE idCandidacy=?", idCandidacy);
    }

    /* Actualizar una candidatura */
    public void updateCandidacy(Candidacy candidacy) {
        jdbcTemplate.update("UPDATE Candidacy SET dateLastModified=?, candidacyStatus=?, rejectedReason=?, idApRequest=?, dniPapPati=? WHERE idCandidacy=?",
                candidacy.getDateLastModified(),
                candidacy.getCandidacyStatus() != null ? candidacy.getCandidacyStatus().name().toLowerCase() : "talksnotstarted",
                candidacy.getRejectedReason(),
                candidacy.getIdApRequest(),
                candidacy.getDniPapPati(),
                candidacy.getIdCandidacy());
    }

    public void updateCandidacyStatus(int idCandidacy, CandidacyStatus candidacyStatus, LocalDate dateLastModified) {
        jdbcTemplate.update("UPDATE Candidacy SET candidacyStatus=?, dateLastModified=? WHERE idCandidacy=?",
                candidacyStatus.name().toLowerCase(),
                dateLastModified,
                idCandidacy);
    }

    /* Buscar una candidatura */
    public Candidacy getCandidacyById(int idCandidacy) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Candidacy WHERE idCandidacy=?",
                    new CandidacyRowMapper(), idCandidacy);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /* Listar candidaturas de un PapPati */
    public List<Candidacy> getCandidaciesByDniPapPati(String dniPapPati) {
        try {
            return jdbcTemplate.query("SELECT * FROM Candidacy WHERE dniPapPati=?",
                    new CandidacyRowMapper(), dniPapPati);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Candidacy>();
        }
    }

    public List<Candidacy> getCandidacies() {
        try {
            return jdbcTemplate.query("SELECT * FROM Candidacy", new CandidacyRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Candidacy>();
        }
    }
}