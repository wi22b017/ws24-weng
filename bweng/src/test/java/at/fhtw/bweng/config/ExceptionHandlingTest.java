package at.fhtw.bweng.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;


class ExceptionHandlingTest {

    private ExceptionHandling exceptionHandling;

    @BeforeEach
    void setUp() {
        exceptionHandling = new ExceptionHandling();
    }

    @Test
    void handleNoSuchElementException_returnsNotFoundResponse() {
        // arrange
        NoSuchElementException exception = new NoSuchElementException("Resource not found");

        // act
        ResponseEntity<Map<String, String>> response = exceptionHandling.handleNoSuchElementException(exception);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).containsEntry("error", "Resource not found");
    }

    @Test
    void handleDataIntegrityViolationException_returnsConflictResponse() {
        // arrange
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Duplicate entry");

        // act
        ResponseEntity<Map<String, String>> response = exceptionHandling.handleDataIntegrityViolationException(exception);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(409);
        assertThat(response.getBody()).containsEntry("error", "Duplicate entry");
    }

    @Test
    void handleValidationExceptions_returnsBadRequestResponse() {
        // Arrange
        BindException bindException = new BindException(new Object(), "airlineDto");
        bindException.addError(new FieldError("airlineDto", "name", "must not be blank"));

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(null, bindException);

        // Act
        ResponseEntity<Map<String, String>> response = exceptionHandling.handleValidationExceptions(exception);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400); // HTTP 400
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).containsEntry("name", "must not be blank");
    }

    @Test
    void handleIllegalArgumentException_returnsBadRequestResponse() {
        // arrange
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        // act
        ResponseEntity<Map<String, String>> response = exceptionHandling.handleIllegalArgumentException(exception);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).containsEntry("error", "Invalid argument");
    }

    @Test
    void handleIllegalStateException_returnsConflictResponse() {
        // arrange
        IllegalStateException exception = new IllegalStateException("Illegal state");

        // act
        ResponseEntity<Map<String, String>> response = exceptionHandling.handleIllegalStateException(exception);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(409);
        assertThat(response.getBody()).containsEntry("error", "Illegal state");
    }

    @Test
    void handleSecurityException_returnsForbiddenResponse() {
        // arrange
        SecurityException exception = new SecurityException("Unauthorized action");

        // act
        ResponseEntity<Map<String, String>> response = exceptionHandling.handleSecurityException(exception);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(403);
        assertThat(response.getBody()).containsEntry("error", "Unauthorized action");
    }

    @Test
    void handleGenericException_returnsInternalServerErrorResponse() {
        // arrange
        Exception exception = new Exception("Unexpected error");

        // act
        ResponseEntity<Map<String, String>> response = exceptionHandling.handleGenericException(exception);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody()).containsEntry("error", "Unexpected error");
    }



}