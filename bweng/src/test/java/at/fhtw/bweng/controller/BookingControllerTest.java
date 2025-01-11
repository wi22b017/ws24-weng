package at.fhtw.bweng.controller;


import at.fhtw.bweng.dto.BaggageDto;
import at.fhtw.bweng.dto.BookingDto;
import at.fhtw.bweng.dto.PassengerDto;
import at.fhtw.bweng.model.*;
import at.fhtw.bweng.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private User mockUser() {
        User user = new User(
                UUID.randomUUID(),
                "Male",
                "John",
                "Doe",
                "john.doe",
                "password123",
                "john.doe@example.com",
                LocalDate.of(1990, 1, 1),
                "USER",
                "ACTIVE",
                new Address(UUID.randomUUID(), "Example Street", 10, 10, "Vienna", "Austria"),
                mockPaymentMethod()
        );

        return user;
    }

    private Flight mockFlight() {
        Airport origin = new Airport(UUID.randomUUID(), "Origin Airport", "ORIG");
        Airport destination = new Airport(UUID.randomUUID(), "Destination Airport", "DEST");
        Airline airline = new Airline(UUID.randomUUID(), "Test Airline");
        Aircraft aircraft = new Aircraft(UUID.randomUUID(), "SN123", "Boeing", "737", 200, airline);
        Flight flight = new Flight(UUID.randomUUID(), "FL123", OffsetDateTime.now(), OffsetDateTime.now(), origin, destination, aircraft, new BigDecimal(150.0));
        return flight;
    }

    private PaymentMethod mockPaymentMethod() {
        PaymentMethod paymentMethod = new PaymentMethod(UUID.randomUUID(), "Credit Card");
        return paymentMethod;
    }

    private PassengerDto mockPassengerDto() {
        return new PassengerDto(
                UUID.randomUUID(),
                "John",
                "Doe",
                LocalDate.of(1990,1,1),
                "",
                new BaggageDto(UUID.randomUUID())
        );
    }

    private Booking mockBooking() {
        User user = mockUser();
        PaymentMethod paymentMethod = mockPaymentMethod();
        Flight flight = mockFlight();
        Booking booking = new Booking(
                UUID.randomUUID(), "Confirmed", new BigDecimal(150), OffsetDateTime.now(), user, paymentMethod,flight);

        return booking;
    }

    private BookingDto mockBookingDto() {
        return new BookingDto(
                "Confirmed",
                new BigDecimal(150),
                "2024-11-11T14:30:00+02:00",
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                List.of(mockPassengerDto())
        );
    }

    @Test
    void addBookingNew_ReturnsCreatedResponse() {
        //Arrange
        BookingDto bookingDto = mockBookingDto();
        UUID bookingId = UUID.randomUUID();
        when(bookingService.addBooking(bookingDto)).thenReturn(bookingId);

        //Act
        ResponseEntity<Map<String, String>> response = bookingController.addBookingNew(bookingDto);

        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Booking created successfully");
        assertThat(responseBody).containsEntry("id", bookingId.toString());

    }

    @Test
    void getBookings_WithIdAndReturnsSpecificBooking() {
        //Arrange
        Booking booking = mockBooking();
        UUID bookingId = UUID.randomUUID();
        booking.setId(bookingId);
        when(bookingService.getBookings(bookingId)).thenReturn(booking);

        //Act
        ResponseEntity<?> response = bookingController.getBookings(bookingId);

        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(booking);

    }

    @Test
    void getBookings_WithoutIdAndReturnsAllBookings() {
        //Arrange
        Booking booking = mockBooking();
        when(bookingService.getBookings(null)).thenReturn(List.of(booking));

        //Act
        ResponseEntity<?> response = bookingController.getBookings(null);

        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(List.of(booking));

    }

    @Test
    void getBookingsByUserId_ReturnsUsersBookings() {
        //Arrange
        UUID userId = UUID.randomUUID();
        Booking booking = mockBooking();
        when(bookingService.getBookingsByUserId(userId)).thenReturn(List.of(booking));

        //Act
        ResponseEntity<?> response = bookingController.getBookingsByUserId(userId);

        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(List.of(booking));

    }

    @Test
    void updateBooking_ReturnsOkResponse() {
        //Arrange
        Booking booking = mockBooking();
        BookingDto bookingDto = mockBookingDto();
        UUID bookingId = UUID.randomUUID();

        //Act
        ResponseEntity<Map<String, String>> response = bookingController.updateBooking(bookingId,bookingDto);

        //Assert
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Booking updated successfully");
        assertThat(responseBody).containsEntry("id", bookingId.toString());
    }

    @Test
    void deleteBooking_ReturnsOkResponse() {
        //Arrange
        UUID bookingId = UUID.randomUUID();

        //Act
        ResponseEntity<Map<String, String>> response = bookingController.deleteBooking(bookingId);

        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Booking deleted successfully");
        assertThat(responseBody).containsEntry("id", bookingId.toString());

    }

    @Test
    void updateBookingStatus_ReturnsOkResponse() {
        //Arrange
        UUID bookingId = UUID.randomUUID();
        Map<String, String> statusUpdate = Map.of("status", "Cancelled");

        //Act
        ResponseEntity<Map<String, String>> response = bookingController.updateBookingStatus(bookingId,statusUpdate);

        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Booking status updated successfully");
        assertThat(responseBody).containsEntry("id", bookingId.toString());

    }

}
