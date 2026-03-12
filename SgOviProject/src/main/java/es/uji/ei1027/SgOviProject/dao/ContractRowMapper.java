package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.model.Contract;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class ContractRowMapper implements RowMapper<Contract> {
    @Override
    public Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contract contract = new Contract();
        contract.setIdContract(rs.getInt("id_contract"));
        contract.setStartDate(rs.getObject("start_date", LocalDate.class));
        contract.setEndDate(rs.getObject("end_date", LocalDate.class));
        contract.setSalary(rs.getInt("salary"));
        contract.setSchedule(rs.getString("schedule"));

        return contract;
    }
}