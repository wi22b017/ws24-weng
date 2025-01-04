package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.BaggageTypeDto;
import at.fhtw.bweng.model.BaggageType;
import at.fhtw.bweng.service.BaggageTypeService;
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
class BaggageTypeControllerTest {

    @Mock
    private BaggageTypeService baggageTypeService;

    @InjectMocks
    private BaggageTypeController baggageTypeController;

    private static final UUID BAGGAGE_TYPE_ID = UUID.randomUUID();
    private static final BaggageTypeDto BAGGAGE_TYPE_DTO = new BaggageTypeDto("Standard Baggage", new BigDecimal("50.00"));
    private static final BaggageType BAGGAGE_TYPE = new BaggageType(BAGGAGE_TYPE_ID, "Standard Baggage", new BigDecimal("50.00"));

    @Test
    void addBaggageType_returnsCreatedResponse() {
        // arrange
        when(baggageTypeService.addBaggageType(BAGGAGE_TYPE_DTO)).thenReturn(BAGGAGE_TYPE_ID);

        // act
        ResponseEntity<Map<String, String>> response = baggageTypeController.addBaggageType(BAGGAGE_TYPE_DTO);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(response.getBody()).containsEntry("message", "Baggage type created successfully");
        assertThat(response.getBody()).containsEntry("id", BAGGAGE_TYPE_ID.toString());
    }

    @Test
    void getBaggageTypes_withoutIdAndName_returnsAllBaggageTypes() {
        // arrange
        when(baggageTypeService.getBaggageTypes(null, null)).thenReturn(List.of(BAGGAGE_TYPE));

        // act
        ResponseEntity<?> response = baggageTypeController.getBaggageTypes(null, null);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(List.of(BAGGAGE_TYPE));
    }

    @Test
    void getBaggageTypes_withId_returnsSpecificBaggageType() {
        // arrange
        when(baggageTypeService.getBaggageTypes(BAGGAGE_TYPE_ID, null)).thenReturn(BAGGAGE_TYPE);

        // act
        ResponseEntity<?> response = baggageTypeController.getBaggageTypes(BAGGAGE_TYPE_ID, null);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(BAGGAGE_TYPE);
    }

    @Test
    void getBaggageTypes_withName_returnsFilteredBaggageTypes() {
        // arrange
        String name = "Standard Baggage";
        when(baggageTypeService.getBaggageTypes(null, name)).thenReturn(List.of(BAGGAGE_TYPE));

        // act
        ResponseEntity<?> response = baggageTypeController.getBaggageTypes(null, name);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(List.of(BAGGAGE_TYPE));
    }

    @Test
    void updateBaggageType_returnsOkResponse() {
        // arrange
        doNothing().when(baggageTypeService).updateBaggageType(BAGGAGE_TYPE_ID, BAGGAGE_TYPE_DTO);

        // act
        ResponseEntity<Map<String, String>> response = baggageTypeController.updateBaggageType(BAGGAGE_TYPE_ID, BAGGAGE_TYPE_DTO);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(response.getBody()).containsEntry("message", "Baggage type updated successfully");
        assertThat(response.getBody()).containsEntry("id", BAGGAGE_TYPE_ID.toString());
    }

    @Test
    void deleteBaggageType_returnsOkResponse() {
        // arrange
        doNothing().when(baggageTypeService).deleteBaggageType(BAGGAGE_TYPE_ID);

        // act
        ResponseEntity<Map<String, String>> response = baggageTypeController.deleteBaggageType(BAGGAGE_TYPE_ID);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(response.getBody()).containsEntry("message", "Baggage type deleted successfully");
        assertThat(response.getBody()).containsEntry("id", BAGGAGE_TYPE_ID.toString());
    }
}
