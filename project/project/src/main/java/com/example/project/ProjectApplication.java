package com.example.project;

import com.example.project.service.RedisDetailsConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@EnableConfigurationProperties(value = {RedisDetailsConfig.class})
public class ProjectApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ProjectApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(JdbcTemplate jdbcTemplate) {
		return args -> {
/*
			jdbcTemplate.execute(
					"DROP TABLE IF EXISTS userRole cascade;\n" +
							"DROP TABLE IF EXISTS users cascade;\n" +
							"DROP TABLE IF EXISTS administrator cascade;\n" +
							"DROP TABLE IF EXISTS customer cascade;\n" +
							"DROP TABLE IF EXISTS country cascade;\n" +
							"DROP TABLE IF EXISTS airlineCompany cascade;\n" +
							"DROP TABLE IF EXISTS flight cascade;\n" +
							"DROP TABLE IF EXISTS ticket cascade;\n" +
							"CREATE TABLE userRole ("+
							"    id SERIAL PRIMARY KEY,\n" +
							"    role_name varchar(255) NOT NULL UNIQUE);\n" +
							"CREATE TABLE users ("+
							"    id BIGSERIAL PRIMARY KEY,\n" +
							"    user_name varchar(255) NOT NULL UNIQUE,\n" +
							"    password varchar(255) NOT NULL,\n" +
							"    email varchar(255) NOT NULL UNIQUE,\n" +
							"    user_role int NOT NULL,\n" +
							"    FOREIGN KEY (user_role) REFERENCES userRole(id));\n" +
							"CREATE TABLE administrator ("+
							"    id SERIAL PRIMARY KEY,\n" +
							"    administrator_firstName varchar(255) NOT NULL default '',\n" +
							"    administrator_lastName varchar(255) NOT NULL default '',\n" +
							"    user_id bigint NOT NULL UNIQUE,\n" +
							"    FOREIGN KEY (user_id) REFERENCES users(id));\n" +
							"CREATE TABLE customer (" +
							"    id BIGSERIAL PRIMARY KEY,\n" +
							"    customer_firstName varchar(255) NOT NULL default '',\n" +
							"    customer_lastName varchar(255) NOT NULL default '',\n" +
							"    address varchar(255) NOT NULL default '',\n" +
							"    phone_number varchar(255) NOT NULL UNIQUE,\n" +
							"    creditCard_number varchar(255) NOT NULL UNIQUE,\n" +
							"    user_id bigint NOT NULL UNIQUE,\n" +
							"    FOREIGN KEY (user_id) REFERENCES users(id));\n" +
							"CREATE TABLE country ("+
							"    id SERIAL PRIMARY KEY,\n" +
							"    country_name varchar(255) NOT NULL UNIQUE);\n" +
							"CREATE TABLE airlineCompany ("+
							"    id BIGSERIAL PRIMARY KEY,\n" +
							"    airlineCompany_name varchar(255) NOT NULL UNIQUE,\n" +
							"    country_id int NOT NULL,\n" +
							"    user_id bigint NOT NULL UNIQUE,\n" +
							"    FOREIGN KEY (country_id) REFERENCES country(id),\n" +
							"    FOREIGN KEY (user_id) REFERENCES users(id));\n" +
							"CREATE TABLE flight ("+
							"    id BIGSERIAL PRIMARY KEY,\n" +
							"    airlineCompany_id bigint NOT NULL,\n" +
							"    originCountry_id int NOT NULL,\n" +
							"    destinationCountry_id int NOT NULL,\n" +
							"    departure_time TIMESTAMP NOT NULL,\n" +
							"    landing_time TIMESTAMP NOT NULL,\n" +
							"    remaining_tickets int NOT NULL,\n" +
							"    FOREIGN KEY (airlineCompany_id) REFERENCES airlineCompany(id),\n" +
							"    FOREIGN KEY (originCountry_id) REFERENCES country(id),\n" +
							"    FOREIGN KEY (destinationCountry_id) REFERENCES country(id));\n" +
							"CREATE TABLE ticket ("+
							"    id BIGSERIAL PRIMARY KEY,\n" +
							"    flight_id bigint NOT NULL,\n" +
							"    customer_id bigint NOT NULL,\n" +
							"    UNIQUE (flight_id, customer_id),\n" +
							"    FOREIGN KEY (flight_id) REFERENCES flight(id),\n" +
							"    FOREIGN KEY (customer_id) REFERENCES customer(id));"
			);

 */
		};
	}
}
