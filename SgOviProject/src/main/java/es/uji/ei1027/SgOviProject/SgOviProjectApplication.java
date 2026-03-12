package es.uji.ei1027.SgOviProject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@SpringBootApplication
public class SgOviProjectApplication implements CommandLineRunner {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(SgOviProjectApplication.class).run(args);
	}

	// Función principal
	@Override
	public void run(String... args) throws Exception {
	}
}
