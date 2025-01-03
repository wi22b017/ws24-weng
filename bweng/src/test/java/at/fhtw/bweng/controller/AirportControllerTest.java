package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.AirportDto;
import at.fhtw.bweng.model.Airport;
import at.fhtw.bweng.service.AirportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportControllerTest {

    @Mock
    private AirportService airportService;

    @InjectMocks
    private AirportController airportController;

    private static final UUID AIRPORT_ID = UUID.randomUUID();
    private static final Airport AIRPORT = new Airport(AIRPORT_ID, "Example Airport", "EXA");
    private static final AirportDto AIRPORT_DTO = new AirportDto("EXA", "Example Airport");



    @Test
    void addAirport_returnsCreatedResponse() {
        // arrange
        when(airportService.addAirport(AIRPORT_DTO)).thenReturn(AIRPORT_ID);

        // act
        ResponseEntity<Map<String, String>> response = airportController.addAirport(AIRPORT_DTO);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(response.getBody()).containsEntry("message", "Airport created successfully");
        assertThat(response.getBody()).containsEntry("id", AIRPORT_ID.toString());
    }

    @Test
    void getAirports_withoutId_returnsAllAirports() {
        // arrange
        when(airportService.getAirports(null)).thenReturn(List.of(AIRPORT));

        // act
        ResponseEntity<?> response = airportController.getAirports(null);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(List.of(AIRPORT));
    }

    @Test
    void getAirports_withId_returnsSpecificAirport() {
        // arrange
        when(airportService.getAirports(AIRPORT_ID)).thenReturn(AIRPORT);

        // act
        ResponseEntity<?> response = airportController.getAirports(AIRPORT_ID);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(AIRPORT);
    }

    @Test
    void updateAirport_returnsOkResponse() {
        // arrange
        doNothing().when(airportService).updateAirport(AIRPORT_ID, AIRPORT_DTO);

        // act
        ResponseEntity<Map<String, String>> response = airportController.updateAirport(AIRPORT_ID, AIRPORT_DTO);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(response.getBody()).containsEntry("message", "Airport updated successfully");
        assertThat(response.getBody()).containsEntry("id", AIRPORT_ID.toString());
    }

    @Test
    void deleteAirport_returnsOkResponse() {
        // arrange
        doNothing().when(airportService).deleteAirport(AIRPORT_ID);

        // act
        ResponseEntity<Map<String, String>> response = airportController.deleteAirport(AIRPORT_ID);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(response.getBody()).containsEntry("message", "Airport deleted successfully");
        assertThat(response.getBody()).containsEntry("id", AIRPORT_ID.toString());
    }
}