package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.model.Contract;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContractDao {

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Añadir un contrato */
    public void addContract(Contract contract) {
        jdbcTemplate.update("INSERT INTO Contract (id_contract, start_date, end_date, salary, schedule) VALUES(?, ?, ?, ?, ?)",
                contract.getIdContract(), contract.getStartDate(), contract.getEndDate(),
                contract.getSalary(), contract.getSchedule());
    }

    /* Borrar un contrato */
    public void deleteContract(Contract contract) {
        jdbcTemplate.update("DELETE FROM Contract WHERE id_contract=?", contract.getIdContract());
    }

    public void deleteContract(int idContract) {
        jdbcTemplate.update("DELETE FROM Contract WHERE id_contract=?", idContract);
    }

    /* Actualizar un contrato */
    public void updateContract(Contract contract) {
        jdbcTemplate.update("UPDATE Contract SET start_date=?, end_date=?, salary=?, schedule=? WHERE id_contract=?",
                contract.getStartDate(), contract.getEndDate(), contract.getSalary(),
                contract.getSchedule(), contract.getIdContract());
    }

    /*Listar un contrato*/
    public Contract getContract(int idContract) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Contract WHERE id_contract=?",
                    new ContractRowMapper(), idContract);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    /*Listar todos los contratos*/
    public List<Contract> getContracts() {
        try {
            return jdbcTemplate.query("SELECT * FROM Contract", new ContractRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Contract>();
        }
    }
}