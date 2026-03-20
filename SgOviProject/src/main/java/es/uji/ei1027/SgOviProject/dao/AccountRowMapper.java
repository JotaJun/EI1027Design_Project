package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.enums.Gender;
import es.uji.ei1027.SgOviProject.enums.Status;
import es.uji.ei1027.SgOviProject.model.Account;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class AccountRowMapper implements RowMapper<Account> {
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        Account account = new Account();
        account.setDni(rs.getString("dni"));
        account.setName(rs.getString("name"));
        account.setSurname(rs.getString("surname"));
        account.setBirthday(rs.getObject("birthday", LocalDate.class));
        account.setPassword(rs.getString("password"));
        account.setEmail(rs.getString("email"));
        account.setPhoneNumber(rs.getString("phoneNumber"));
        account.setCity(rs.getString("city"));
        account.setStreet(rs.getString("street"));
        account.setZipCode(rs.getString("zipCode"));

        // Mapeo del Status con conversión a mayúsculas
        String statusStr = rs.getString("status");
        if (statusStr != null && !statusStr.trim().isEmpty()) {
            account.setStatus(Status.valueOf(statusStr.toUpperCase()));
        } else {
            // Como en SQL tiene DEFAULT 'pending' y NOT NULL, esto es por precaución
            throw new SQLException("status ha fallado.");
        }

        account.setDeniedReason(rs.getString("deniedReason"));

        String genderStr = rs.getString("gender");
        if (genderStr != null && !genderStr.trim().isEmpty()) {
            account.setGender(Gender.valueOf(genderStr));
        } else {
            throw new SQLException("Gender cannot be null or empty");
        }

        return account;
    }
}