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

        contract.setIdContract(rs.getInt("idContract"));
        contract.setIdCandidacy(rs.getInt("idCandidacy"));
        contract.setStartDate(rs.getObject("startDate", LocalDate.class));
        contract.setEndDate(rs.getObject("endDate", LocalDate.class));
        contract.setHourlySalary(rs.getDouble("hourlySalary"));
        contract.setSchedule(rs.getString("schedule"));
        contract.setUrlDocument(rs.getString("urlDocument"));

        contract.setDeniedReason(rs.getString("deniedReason"));

        return contract;
    }
}