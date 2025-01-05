package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.AirlineDto;
import at.fhtw.bweng.model.Airline;
import at.fhtw.bweng.service.AirlineService;
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
class AirlineControllerTest {

    @Mock
    private AirlineService airlineService;

    @InjectMocks
    private AirlineController airlineController;

    private static final UUID AIRLINE_ID = UUID.randomUUID();
    private static final AirlineDto AIRLINE_DTO = new AirlineDto("Example Airline");
    private static final Airline AIRLINE = new Airline(AIRLINE_ID, "Example Airline");

    @Test
    void addAirline_returnsCreatedResponse() {
        // arrange
        when(airlineService.addAirline(AIRLINE_DTO)).thenReturn(AIRLINE_ID);

        // act
        ResponseEntity<?> response = airlineController.addAirline(AIRLINE_DTO);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        @SuppressWarnings("unchecked") // Suppress unchecked cast warning
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Airline added successfully");
        assertThat(responseBody).containsEntry("id", AIRLINE_ID.toString());
    }

    @Test
    void getAirlines_withoutId_returnsAllAirlines() {
        // arrange
        when(airlineService.getAirlines(null)).thenReturn(List.of(AIRLINE));

        // act
        ResponseEntity<?> response = airlineController.getAirlines(null);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(List.of(AIRLINE));
    }

    @Test
    void getAirlines_withId_returnsSpecificAirline() {
        // arrange
        when(airlineService.getAirlines(AIRLINE_ID)).thenReturn(AIRLINE);

        // act
        ResponseEntity<?> response = airlineController.getAirlines(AIRLINE_ID);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(AIRLINE);
    }

    @Test
    void updateAirline_returnsOkResponse() {
        // arrange
        doNothing().when(airlineService).updateAirline(AIRLINE_ID, AIRLINE_DTO);

        // act
        ResponseEntity<?> response = airlineController.updateAirline(AIRLINE_ID, AIRLINE_DTO);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        @SuppressWarnings("unchecked") // Suppress unchecked cast warning
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Airline updated successfully");
        assertThat(responseBody).containsEntry("id", AIRLINE_ID.toString());
    }

    @Test
    void deleteAirline_returnsOkResponse() {
        // arrange
        doNothing().when(airlineService).deleteAirline(AIRLINE_ID);

        // act
        ResponseEntity<?> response = airlineController.deleteAirline(AIRLINE_ID);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        @SuppressWarnings("unchecked") // Suppress unchecked cast warning
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Airline deleted successfully");
        assertThat(responseBody).containsEntry("id", AIRLINE_ID.toString());
    }



}
