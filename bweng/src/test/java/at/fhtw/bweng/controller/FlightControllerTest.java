package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.AircraftDto;
import at.fhtw.bweng.dto.AirlineDto;
import at.fhtw.bweng.dto.AirportDto;
import at.fhtw.bweng.dto.FlightDto;
import at.fhtw.bweng.model.Aircraft;
import at.fhtw.bweng.model.Airline;
import at.fhtw.bweng.model.Airport;
import at.fhtw.bweng.model.Flight;
import at.fhtw.bweng.service.FlightService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightControllerTest {
    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightController flightController;

    private FlightDto mockFlightDto() {
        FlightDto flightDto = new FlightDto(  "FL123",
                "2024-01-01T10:00:00+00:00",
                "2024-01-01T14:00:00+00:00",
                new AirportDto("ORIG", "Origin Airport"),
                new AirportDto("DEST", "Destination Airport"),
                new AircraftDto("Boeing", "737", 180, new AirlineDto("Test Airline"), "SN123"),
                new BigDecimal(150.0));
        return flightDto;
    }

    private Flight mockFlight() {
        Airport origin = new Airport(UUID.randomUUID(), "Origin Airport", "ORIG");
        Airport destination = new Airport(UUID.randomUUID(), "Destination Airport", "DEST");
        Airline airline = new Airline(UUID.randomUUID(), "Test Airline");
        Aircraft aircraft = new Aircraft(UUID.randomUUID(), "SN123", "Boeing", "737", 200, airline);
        Flight flight = new Flight(null, "FL123", OffsetDateTime.now(), OffsetDateTime.now(), origin, destination, aircraft, new BigDecimal(150.0));
        return flight;
    }

    @Test
    void addFlight_ReturnsCreatedResponse() {
        //Arrange
        FlightDto flightDto = mockFlightDto();
        UUID flightId = UUID.randomUUID();
        when(flightService.addFlight(flightDto)).thenReturn(flightId);

        //Act
        ResponseEntity<Map<String, String>> response = flightController.addFlight(flightDto);

        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Flight created successfully");
        assertThat(responseBody).containsEntry("id", flightId.toString());
    }

    @Test
    void getFlights_WithIdAndReturnsSpecificFlight(){
        //Arrange
        UUID flightId = UUID.randomUUID();
        Flight flight = mockFlight();
        flight.setId(flightId);
        when(flightService.getFlights(flightId,null)).thenReturn(flight);

        //Act
        ResponseEntity<?> response = flightController.getFlights(flightId,null);

        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(flight);
    }

    @Test
    void getFlights_WithFlightNumberAndReturnsSpecificFlight(){
        //Arrange
        String flightNumber = "FL123";
        Flight flight = mockFlight();
        when(flightService.getFlights(null,flightNumber)).thenReturn(flight);

        //Act
        ResponseEntity<?> response = flightController.getFlights(null,flightNumber);

        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(flight);
    }

    @Test
    void getFlights_WithoutIdOrFlightNumberAndReturnsAllFlights(){
        //Arrange
        Flight flight = mockFlight();
        when(flightService.getFlights(null,null)).thenReturn(List.of(flight));

        //Act
        ResponseEntity<?> response = flightController.getFlights(null,null);

        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(List.of(flight));
    }

    @Test
    void updateFlight_ReturnsOkResponse() {
        //Arrange
        UUID flightId = UUID.randomUUID();
        FlightDto flightDto = mockFlightDto();

        //Act
        ResponseEntity<Map<String, String>> response = flightController.updateFlight(flightId, flightDto);

        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Flight updated successfully");
        assertThat(responseBody).containsEntry("id", flightId.toString());
    }

    @Test
    void deleteFlight_ReturnsOkResponse() {
        //Arrange
        UUID flightId = UUID.randomUUID();

        //Act
        ResponseEntity<Map<String, String>> response = flightController.deleteFlight(flightId);

        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Flight deleted successfully");
        assertThat(responseBody).containsEntry("id", flightId.toString());
    }


}
