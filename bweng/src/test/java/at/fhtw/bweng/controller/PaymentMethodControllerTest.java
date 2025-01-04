package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.PaymentMethodDto;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.service.PaymentMethodService;
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
class PaymentMethodControllerTest {

    @Mock
    private PaymentMethodService paymentMethodService;

    @InjectMocks
    private PaymentMethodController paymentMethodController;

    private static final UUID PAYMENT_METHOD_ID = UUID.randomUUID();
    private static final PaymentMethodDto PAYMENT_METHOD_DTO = new PaymentMethodDto("Credit Card");
    private static final PaymentMethod PAYMENT_METHOD = new PaymentMethod(PAYMENT_METHOD_ID, "Credit Card");

    @Test
    void addPaymentMethod_returnsCreatedResponse() {
        // Arrange
        when(paymentMethodService.addPaymentMethod(PAYMENT_METHOD_DTO)).thenReturn(PAYMENT_METHOD_ID);

        // Act
        ResponseEntity<?> response = paymentMethodController.addPaymentMethod(PAYMENT_METHOD_DTO);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Payment method added successfully");
        assertThat(responseBody).containsEntry("id", PAYMENT_METHOD_ID.toString());
    }

    @Test
    void getPaymentMethods_withoutId_returnsAllPaymentMethods() {
        // Arrange
        when(paymentMethodService.getPaymentMethods(null)).thenReturn(List.of(PAYMENT_METHOD));

        // Act
        ResponseEntity<?> response = paymentMethodController.getPaymentMethods(null);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(List.of(PAYMENT_METHOD));
    }

    @Test
    void getPaymentMethods_withId_returnsSpecificPaymentMethod() {
        // Arrange
        when(paymentMethodService.getPaymentMethods(PAYMENT_METHOD_ID)).thenReturn(PAYMENT_METHOD);

        // Act
        ResponseEntity<?> response = paymentMethodController.getPaymentMethods(PAYMENT_METHOD_ID);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(PAYMENT_METHOD);
    }

    @Test
    void updatePaymentMethod_returnsOkResponse() {
        // Arrange
        doNothing().when(paymentMethodService).updatePaymentMethod(PAYMENT_METHOD_ID, PAYMENT_METHOD_DTO);

        // Act
        ResponseEntity<?> response = paymentMethodController.updatePaymentMethod(PAYMENT_METHOD_ID, PAYMENT_METHOD_DTO);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Payment method updated successfully");
        assertThat(responseBody).containsEntry("id", PAYMENT_METHOD_ID.toString());
    }

    @Test
    void deletePaymentMethod_returnsOkResponse() {
        // Arrange
        doNothing().when(paymentMethodService).deletePaymentMethod(PAYMENT_METHOD_ID);

        // Act
        ResponseEntity<?> response = paymentMethodController.deletePaymentMethod(PAYMENT_METHOD_ID);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Payment method deleted successfully");
        assertThat(responseBody).containsEntry("id", PAYMENT_METHOD_ID.toString());
    }
}
