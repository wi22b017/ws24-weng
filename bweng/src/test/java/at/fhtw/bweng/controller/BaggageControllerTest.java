package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.BaggageDto;
import at.fhtw.bweng.model.Baggage;
import at.fhtw.bweng.model.BaggageType;
import at.fhtw.bweng.service.BaggageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaggageControllerTest {

    @Mock
    private BaggageService baggageService;

    @InjectMocks
    private BaggageController baggageController;

    private static final UUID BAGGAGE_ID = UUID.randomUUID();
    private static final UUID BAGGAGE_TYPE_ID = UUID.randomUUID();
    private static final BaggageType BAGGAGE_TYPE = new BaggageType(BAGGAGE_TYPE_ID, "Standard Baggage", new BigDecimal("50.00"));
    private static final BaggageDto BAGGAGE_DTO = new BaggageDto(BAGGAGE_TYPE_ID);
    private static final Baggage BAGGAGE = new Baggage(BAGGAGE_ID, BAGGAGE_TYPE);

    @Test
    void addBaggage_returnsCreatedResponse() {
        // arrange
        when(baggageService.addBaggage(BAGGAGE_DTO)).thenReturn(BAGGAGE_ID);

        // act
        ResponseEntity<Map<String, String>> response = baggageController.addBaggage(BAGGAGE_DTO);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(response.getBody()).containsEntry("message", "Baggage created successfully");
        assertThat(response.getBody()).containsEntry("id", BAGGAGE_ID.toString());
    }

    @Test
    void getBaggages_withoutIdAndType_returnsAllBaggages() {
        // arrange
        when(baggageService.getBaggages(null, null)).thenReturn(List.of(BAGGAGE));

        // act
        ResponseEntity<?> response = baggageController.getBaggages(null, null);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(List.of(BAGGAGE));
    }

    @Test
    void getBaggages_withId_returnsSpecificBaggage() {
        // arrange
        when(baggageService.getBaggages(BAGGAGE_ID, null)).thenReturn(BAGGAGE);

        // act
        ResponseEntity<?> response = baggageController.getBaggages(BAGGAGE_ID, null);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(BAGGAGE);
    }

    @Test
    void getBaggages_withBaggageTypeId_returnsFilteredBaggages() {
        // arrange
        when(baggageService.getBaggages(null, BAGGAGE_TYPE_ID)).thenReturn(List.of(BAGGAGE));

        // act
        ResponseEntity<?> response = baggageController.getBaggages(null, BAGGAGE_TYPE_ID);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(List.of(BAGGAGE));
    }

    @Test
    void updateBaggage_returnsOkResponse() {
        // arrange
        doNothing().when(baggageService).updateBaggage(BAGGAGE_ID, BAGGAGE_DTO);

        // act
        ResponseEntity<Map<String, String>> response = baggageController.updateBaggage(BAGGAGE_ID, BAGGAGE_DTO);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(response.getBody()).containsEntry("message", "Baggage updated successfully");
        assertThat(response.getBody()).containsEntry("id", BAGGAGE_ID.toString());
    }

    @Test
    void deleteBaggage_returnsOkResponse() {
        // arrange
        doNothing().when(baggageService).deleteBaggage(BAGGAGE_ID);

        // act
        ResponseEntity<Map<String, String>> response = baggageController.deleteBaggage(BAGGAGE_ID);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(response.getBody()).containsEntry("message", "Baggage deleted successfully");
        assertThat(response.getBody()).containsEntry("id", BAGGAGE_ID.toString());
    }
}
