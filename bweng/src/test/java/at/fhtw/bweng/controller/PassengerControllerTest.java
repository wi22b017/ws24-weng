package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.BaggageDto;
import at.fhtw.bweng.dto.PassengerDto;
import at.fhtw.bweng.model.Baggage;
import at.fhtw.bweng.model.Passenger;
import at.fhtw.bweng.service.PassengerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import at.fhtw.bweng.model.BaggageType;
import java.math.BigDecimal;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerControllerTest {

    @Mock
    private PassengerService passengerService;

    @InjectMocks
    private PassengerController passengerController;

    private static final UUID PASSENGER_ID = UUID.randomUUID();
    private static final UUID BAGGAGE_TYPE_ID = UUID.randomUUID();
    private static final BaggageDto BAGGAGE_DTO = new BaggageDto(BAGGAGE_TYPE_ID);
    private static final PassengerDto PASSENGER_DTO = new PassengerDto(
            null,
            "John",
            "Doe",
            LocalDate.of(1990, 1, 1),
            "12A",
            BAGGAGE_DTO
    );
    private static final Baggage BAGGAGE = new Baggage(UUID.randomUUID(),
            new BaggageType(BAGGAGE_TYPE_ID, "Standard Baggage", BigDecimal.valueOf(50.0)));
    private static final Passenger PASSENGER = new Passenger(
            PASSENGER_ID,
            "John",
            "Doe",
            LocalDate.of(1990, 1, 1),
            "12A",
            BAGGAGE
    );

    @Test
    void addPassenger_returnsCreatedResponse() {
        // arrange
        when(passengerService.addPassenger(PASSENGER_DTO)).thenReturn(PASSENGER_ID);

        // act
        ResponseEntity<?> response = passengerController.addPassenger(PASSENGER_DTO);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Passenger added successfully");
        assertThat(responseBody).containsEntry("uuid", PASSENGER_ID.toString());
    }

    @Test
    void getPassengers_withoutId_returnsAllPassengers() {
        // arrange
        when(passengerService.getPassengers(null)).thenReturn(List.of(PASSENGER));

        // act
        ResponseEntity<?> response = passengerController.getPassengers(null);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(List.of(PASSENGER));
    }

    @Test
    void getPassengers_withId_returnsSpecificPassenger() {
        // arrange
        when(passengerService.getPassengers(PASSENGER_ID)).thenReturn(PASSENGER);

        // act
        ResponseEntity<?> response = passengerController.getPassengers(PASSENGER_ID);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(PASSENGER);
    }

    @Test
    void updatePassenger_returnsOkResponse() {
        // arrange
        doNothing().when(passengerService).updatePassenger(PASSENGER_ID, PASSENGER_DTO);

        // act
        ResponseEntity<?> response = passengerController.updatePassenger(PASSENGER_ID, PASSENGER_DTO);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Passenger updated successfully");
        assertThat(responseBody).containsEntry("uuid", PASSENGER_ID.toString());
    }

    @Test
    void deletePassenger_returnsOkResponse() {
        // arrange
        doNothing().when(passengerService).deletePassenger(PASSENGER_ID);

        // act
        ResponseEntity<?> response = passengerController.deletePassenger(PASSENGER_ID);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Passenger deleted successfully");
        assertThat(responseBody).containsEntry("uuid", PASSENGER_ID.toString());
    }
}
