package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContractDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Añadir un contrato */
    public void addContract(Contract contract) {
        jdbcTemplate.update("INSERT INTO Contract (idCandidacy, startDate, endDate, hourlySalary, schedule, urlDocument) VALUES(?, ?, ?, ?, ?, ?)",
                contract.getIdCandidacy(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getHourlySalary(),
                contract.getSchedule(),
                contract.getUrlDocument());
    }

    /* Borrar un contrato */
    public void deleteContract(Contract contract) {
        jdbcTemplate.update("DELETE FROM Contract WHERE idContract=?", contract.getIdContract());
    }

    public void deleteContract(int idContract) {
        jdbcTemplate.update("DELETE FROM Contract WHERE idContract=?", idContract);
    }

    /* Actualizar un contrato */
    public void updateContract(Contract contract) {
        jdbcTemplate.update("UPDATE Contract SET idCandidacy=?, startDate=?, endDate=?, hourlySalary=?, schedule=?, urlDocument=? WHERE idContract=?",
                contract.getIdCandidacy(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getHourlySalary(),
                contract.getSchedule(),
                contract.getUrlDocument(),
                contract.getIdContract());
    }

    /* Listar un contrato */
    public Contract getContract(int idContract) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Contract WHERE idContract=?",
                    new ContractRowMapper(), idContract);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /* Listar todos los contratos */
    public List<Contract> getContracts() {
        try {
            return jdbcTemplate.query("SELECT * FROM Contract", new ContractRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Contract>();
        }
    }

    public List<Contract> getContractsByIdCandidacy(int idCandidacy) {
        try {
            return jdbcTemplate.query("SELECT * FROM Contract WHERE idCandidacy=?",
                    new ContractRowMapper(), idCandidacy);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Contract>();
        }
    }
}