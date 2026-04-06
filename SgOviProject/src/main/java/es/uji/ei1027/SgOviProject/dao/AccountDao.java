package es.uji.ei1027.SgOviProject.dao;

import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.LoginDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addAccount(Account account) {
        jdbcTemplate.update("INSERT INTO Account(dni, name, surname, birthday, password, email, phoneNumber, city, street, zipCode, gender, status, deniedReason) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                account.getDni(), account.getName(), account.getSurname(), account.getBirthday(),
                account.getPassword(), account.getEmail(), account.getPhoneNumber(),
                account.getCity(), account.getStreet(), account.getZipCode(),
                account.getGender().name(),
                account.getStatus().name().toLowerCase(), // Conversión a minúscula para la BBDD
                account.getDeniedReason());
    }

    public void deleteAccount(String dni) {
        jdbcTemplate.update("DELETE FROM Account WHERE dni=?", dni);
    }

    public void deleteAccount(Account account) {
        jdbcTemplate.update("DELETE FROM Account WHERE dni=?", account.getDni());
    }

    // Actualiza todo MENOS dni
    public void updateAccount(Account account) {
        jdbcTemplate.update("UPDATE Account SET name=?, surname=?, birthday=?, password=?, email=?, phoneNumber=?, city=?, street=?, zipCode=?, gender=?, status=?, deniedReason=? WHERE dni=?",
                account.getName(), account.getSurname(), account.getBirthday(),
                account.getPassword(), account.getEmail(), account.getPhoneNumber(),
                account.getCity(), account.getStreet(), account.getZipCode(),
                account.getGender().name(),
                account.getStatus().name().toLowerCase(), // Conversión a minúscula para la BBDD
                account.getDeniedReason(),
                account.getDni());
    }

    public Account getAccount(String dni) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Account WHERE dni=?",
                    new AccountRowMapper(), dni);
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Account getAccountByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Account WHERE email=?",
                    new AccountRowMapper(), email);
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Account getAccountByLoginDetails(LoginDetails details){
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Account WHERE email=? and password=?",
                    new AccountRowMapper(), details.getEmail(), details.getPassword());
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Account> getAccounts() {
        try {
            return jdbcTemplate.query("SELECT * FROM Account",
                    new AccountRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Account>();
        }
    }
}