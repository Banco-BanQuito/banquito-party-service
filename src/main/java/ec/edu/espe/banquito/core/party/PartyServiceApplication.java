package ec.edu.espe.banquito.core.party;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class PartyServiceApplication {

	private static final Logger log = LoggerFactory.getLogger(PartyServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PartyServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner insertAnahy(JdbcTemplate jdbcTemplate) {
		return args -> {
			try {
				String sql = "INSERT INTO customer (customer_subtype_id, customer_type, identification_type, identification, first_name, last_name, email, status) " +
							 "VALUES (1, 'NATURAL', 'CEDULA', '1700000001', 'Anahy Estefania', 'Herrera Quinte', 'anahyherrera09082002@gmail.com', 'ACTIVO')";
				jdbcTemplate.execute(sql);
				log.info("====== CUSTOMER 1700000001 INSERTED ======");
			} catch (Exception e) {
				log.warn("====== CUSTOMER 1700000001 ALREADY EXISTS OR ERROR: {} ======", e.getMessage());
			}
		};
	}

}