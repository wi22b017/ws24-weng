package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.AircraftDto;
import at.fhtw.bweng.dto.AirlineDto;
import at.fhtw.bweng.model.Aircraft;
import at.fhtw.bweng.model.Airline;
import at.fhtw.bweng.service.AircraftService;
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
class AircraftControllerTest {

    @Mock
    private AircraftService aircraftService;

    @InjectMocks
    private AircraftController aircraftController;

    private static final UUID AIRCRAFT_ID = UUID.randomUUID();
    private static final UUID AIRLINE_ID = UUID.randomUUID();
    private static final Airline AIRLINE = new Airline(AIRLINE_ID, "Example Airline");
    private static final AircraftDto AIRCRAFT_DTO = new AircraftDto("Boeing", "737", 180, new AirlineDto("Example Airline"), "SN123");
    private static final Aircraft AIRCRAFT = new Aircraft(AIRCRAFT_ID, "SN123", "Boeing", "737", 180, AIRLINE);

    @Test
    void addAircraft_returnsCreatedResponse() {
        // arrange
        when(aircraftService.addAircraft(AIRCRAFT_DTO)).thenReturn(AIRCRAFT_ID);

        // act
        ResponseEntity<Map<String, String>> response = aircraftController.addAircraft(AIRCRAFT_DTO);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(response.getBody()).containsEntry("message", "Aircraft created successfully");
        assertThat(response.getBody()).containsEntry("id", AIRCRAFT_ID.toString());
    }

    @Test
    void getAircrafts_withoutId_returnsAllAircrafts() {
        // arrange
        when(aircraftService.getAircrafts(null, null)).thenReturn(List.of(AIRCRAFT));

        // act
        ResponseEntity<?> response = aircraftController.getAircrafts(null, null);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(List.of(AIRCRAFT));
    }

    @Test
    void getAircrafts_withId_returnsSpecificAircraft() {
        // arrange
        when(aircraftService.getAircrafts(AIRCRAFT_ID, null)).thenReturn(AIRCRAFT);

        // act
        ResponseEntity<?> response = aircraftController.getAircrafts(AIRCRAFT_ID, null);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(AIRCRAFT);
    }

    @Test
    void getAircrafts_withSerialNumber_returnsSpecificAircraft() {
        // arrange
        String serialNumber = "SN123";
        when(aircraftService.getAircrafts(null, serialNumber)).thenReturn(AIRCRAFT);

        // act
        ResponseEntity<?> response = aircraftController.getAircrafts(null, serialNumber);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(AIRCRAFT);
    }

    @Test
    void updateAircraft_returnsOkResponse() {
        // arrange
        doNothing().when(aircraftService).updateAircraft(AIRCRAFT_ID, AIRCRAFT_DTO);

        // act
        ResponseEntity<Map<String, String>> response = aircraftController.updateAircraft(AIRCRAFT_ID, AIRCRAFT_DTO);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(response.getBody()).containsEntry("message", "Aircraft updated successfully");
        assertThat(response.getBody()).containsEntry("id", AIRCRAFT_ID.toString());
    }

    @Test
    void deleteAircraft_returnsOkResponse() {
        // arrange
        doNothing().when(aircraftService).deleteAircraft(AIRCRAFT_ID);

        // act
        ResponseEntity<Map<String, String>> response = aircraftController.deleteAircraft(AIRCRAFT_ID);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(response.getBody()).containsEntry("message", "Aircraft deleted successfully");
        assertThat(response.getBody()).containsEntry("id", AIRCRAFT_ID.toString());
    }
}
