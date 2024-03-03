package com.example.project;

import com.example.project.model.*;
import com.example.project.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@SpringBootTest
class ProjectApplicationTests {
	@Autowired
	AdministratorService administratorService;
	@Autowired
	private CountriesService countriesService;
	@Autowired
	private UserRuleService userRuleService;
	@Autowired
	private AirlineCompanyService airlineCompanyService;
	@Autowired
	private FlightsService flightsService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private TicketsService ticketsService;
	@Autowired
	private JdbcTemplate jdbcTemplate;

    //It's necessary to change 'spring.datasource.url' to 'spring.datasource.url=jdbc:postgresql://localhost:5432/testdb'
	//Before any tests, it is necessary to create the tables in the database
	@BeforeEach
	public void create_tables_in_db() {
		String sqlQuery = "SELECT * FROM create_tables();";
		jdbcTemplate.execute(sqlQuery);
	}

	//After each test, it is necessary to delete the tables in the database
	@AfterEach
	public void drop_tables() {
		String sqlQuery = "SELECT * FROM drop_tables();";
		jdbcTemplate.execute(sqlQuery);
	}

	@Test
	void test_for_creating_airline_company() throws ClientFaultException {
		UserRole airlineUserRole = userRuleService.addUserRole(new UserRole(0, UserRoleName.AIRLINE_COMPANY));
		Country firstCountry = countriesService.addCountry(new Country(0, "israel"));

		User newUser = new User(0L, "airlineuser", "airlinepassword", "airlineuser@gmail.com", airlineUserRole.getId());
		AirlineCompany newAirlineCompany = new AirlineCompany(0L, "arkiaa", firstCountry.getId(), newUser.getId());
		UserAndAirlineCompanyDto userAndAirlineCompanyDto = new UserAndAirlineCompanyDto(newUser, newAirlineCompany);
		administratorService.addAirlineCompanyUser(userAndAirlineCompanyDto);

		System.out.println(airlineUserRole);
		System.out.println(firstCountry);
		System.out.println(newUser);
		System.out.println(newAirlineCompany);

		//לבדוק אם נוצר כראוי
		String expected = "arkiaa";
		String actual = airlineCompanyService.getAirlineCompanyById(newAirlineCompany.getId()).getAirlineCompany_name();

		// assert
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_for_creating_flight() throws ClientFaultException {
		UserRole airlineUserRole = userRuleService.addUserRole(new UserRole(0, UserRoleName.AIRLINE_COMPANY));
		UserRole customerUserRole = userRuleService.addUserRole(new UserRole(0, UserRoleName.CUSTOMER));
		Country firstCountry = countriesService.addCountry(new Country(0, "israel"));
		Country secondCountry = countriesService.addCountry(new Country(0, "france"));

		//add airline company user
		User user1 = new User(0L, "airlineuser", "airlinepassword", "airlineuser@gmail.com", airlineUserRole.getId());
		AirlineCompany newAirlineCompany = new AirlineCompany(0L, "arkiaa", firstCountry.getId(), user1.getId());
		UserAndAirlineCompanyDto userAndAirlineCompanyDto = new UserAndAirlineCompanyDto(user1, newAirlineCompany);
		administratorService.addAirlineCompanyUser(userAndAirlineCompanyDto);

		//add customer user
		User user2 = new User(0L, "customeruser", "customerpassword", "customeruser@gmail.com", customerUserRole.getId());
		Customer newCustomer = new Customer(0L, "noa", "kirel", "tel-aviv", "0524633576", "5326010243214159", user2.getId());
		UserAndCustomerDto userAndCustomerDto = new UserAndCustomerDto(user2, newCustomer);
		administratorService.addCustomerUser(userAndCustomerDto);

		//add flight
		Flight newFlight = flightsService.addFlight(new Flight(0L, newAirlineCompany.getId(), firstCountry.getId(),
				secondCountry.getId(), LocalDateTime.parse("2024-12-29 12:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				LocalDateTime.parse("2024-12-30 06:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), 5));

		//add ticket
		Ticket ticket = ticketsService.addTicket(new Ticket(0L, newCustomer.getId(), newFlight.getId()));

		Integer expected = 4;
		Integer actual = flightsService.getFlightById(newFlight.getId()).getRemaining_tickets();

		// assert
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_for_creating_flight_with_illegal_data() throws ClientFaultException {
		UserRole airlineUserRole = userRuleService.addUserRole(new UserRole(0, UserRoleName.AIRLINE_COMPANY));
		Country firstCountry = countriesService.addCountry(new Country(0, "israel"));
		Country secondCountry = countriesService.addCountry(new Country(0, "france"));

		//add airline company user
		User user1 = new User(0L, "airlineuser", "airlinepassword", "airlineuser@gmail.com", airlineUserRole.getId());
		AirlineCompany newAirlineCompany = new AirlineCompany(0L, "arkiaa", firstCountry.getId(), user1.getId());
		UserAndAirlineCompanyDto userAndAirlineCompanyDto = new UserAndAirlineCompanyDto(user1, newAirlineCompany);
		administratorService.addAirlineCompanyUser(userAndAirlineCompanyDto);

		//add flight
		//remaining_tickets < 0
		Flight newFlight = new Flight(0L, newAirlineCompany.getId(), firstCountry.getId(),
				secondCountry.getId(), LocalDateTime.parse("2024-12-29 12:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				LocalDateTime.parse("2024-12-30 06:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), -1);


		Exception exception = Assertions.assertThrows(IllegalDataException.class, () -> {
			flightsService.addFlight(newFlight);
		});

		String expectedMessage = "The data you supplied is not valid";
		String actualMessage = exception.getMessage();
		System.out.println(exception.getMessage());

		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void test2_for_creating_flight_with_illegal_data() throws ClientFaultException {
		UserRole airlineUserRole = userRuleService.addUserRole(new UserRole(0, UserRoleName.AIRLINE_COMPANY));
		Country firstCountry = countriesService.addCountry(new Country(0, "israel"));
		Country secondCountry = countriesService.addCountry(new Country(0, "france"));

		//add airline company user
		User user1 = new User(0L, "airlineuser", "airlinepassword", "airlineuser@gmail.com", airlineUserRole.getId());
		AirlineCompany newAirlineCompany = new AirlineCompany(0L, "arkiaa", firstCountry.getId(), user1.getId());
		UserAndAirlineCompanyDto userAndAirlineCompanyDto = new UserAndAirlineCompanyDto(user1, newAirlineCompany);
		administratorService.addAirlineCompanyUser(userAndAirlineCompanyDto);

		//add flight
		//originCountry_id = destinationCountry_id
		Flight newFlight = new Flight(0L, newAirlineCompany.getId(), firstCountry.getId(),
				firstCountry.getId(), LocalDateTime.parse("2024-12-29 12:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				LocalDateTime.parse("2024-12-30 06:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), 5);


		Exception exception = Assertions.assertThrows(IllegalDataException.class, () -> {
			flightsService.addFlight(newFlight);
		});

		String expectedMessage = "The data you supplied is not valid";
		String actualMessage = exception.getMessage();
		System.out.println(exception.getMessage());

		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

}





