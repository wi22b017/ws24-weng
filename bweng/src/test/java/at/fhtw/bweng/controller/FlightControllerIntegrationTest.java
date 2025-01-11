package at.fhtw.bweng.controller;

import at.fhtw.bweng.model.Aircraft;
import at.fhtw.bweng.model.Flight;
import at.fhtw.bweng.repository.AircraftRepository;
import at.fhtw.bweng.repository.FlightRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlightControllerIntegrationTest {

    @Container
    private static final MySQLContainer<?> mariadbContainer = new MySQLContainer<>(
            org.testcontainers.utility.DockerImageName.parse("mariadb:10.6")
                    .asCompatibleSubstituteFor("mysql")
    )
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpw");

    @LocalServerPort
    private int port;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AircraftRepository aircraftRepository; // Inject AircraftRepository

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        System.setProperty("spring.datasource.url", mariadbContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mariadbContainer.getUsername());
        System.setProperty("spring.datasource.password", mariadbContainer.getPassword());

        // Create and save an Aircraft entity
        Aircraft aircraft = new Aircraft(
                UUID.randomUUID(),
                "SN12345",          // Serial Number
                "Boeing",           // Manufacturer
                "747",              // Model
                300,                // Capacity
                null                // No Airline for this test
        );
        aircraft = aircraftRepository.save(aircraft); // Save the Aircraft to the database

        // Create and save a Flight entity
        Flight flight = new Flight();
        flight.setFlightNumber("TEST123");
        flight.setDepartureTime(OffsetDateTime.now());
        flight.setArrivalTime(OffsetDateTime.now().plusHours(2));
        flight.setPrice(BigDecimal.valueOf(150.50));
        flight.setAircraft(aircraft); // Set the saved Aircraft

        flightRepository.save(flight); // Save the Flight to the database
    }

    @AfterEach
    public void tearDown() {
        flightRepository.deleteAll();
        aircraftRepository.deleteAll();
    }

    @Test
    public void testGetFlights() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/flights", String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("TEST123");
    }



}
