package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.BaggageDto;
import at.fhtw.bweng.dto.BookingDto;
import at.fhtw.bweng.dto.PassengerDto;
import at.fhtw.bweng.model.*;
import at.fhtw.bweng.repository.BookingRepository;
import at.fhtw.bweng.repository.FlightRepository;
import at.fhtw.bweng.repository.PaymentMethodRepository;
import at.fhtw.bweng.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private PaymentMethodRepository paymentMethodRepository;
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PassengerService passengerService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private BookingService bookingService;

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
    void getBookingById_Success() {
        //Arrange
        UUID bookingId = UUID.randomUUID();
        Booking booking = mockBooking();
        booking.setId(bookingId);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        //Act
        Booking result = bookingService.getBookingById(bookingId);

        //Assert
        assertNotNull(result);
        assertEquals(bookingId,result.getId());
        assertEquals("Confirmed", result.getStatus());
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    void getBookingById_ThrowsExceptionWhenNotFound() {
        // Arrange
        UUID bookingId = UUID.randomUUID();
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> bookingService.getBookingById(bookingId)
        );

        assertEquals("Booking with ID " + bookingId + " not found", exception.getMessage());
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    void getAllBookings_ReturnsBookings() {
        //Arrange
        Booking booking_one = mockBooking();
        Booking booking_two = mockBooking();
        Booking booking_three = mockBooking();
        List<Booking> bookings = List.of(booking_one, booking_two, booking_three);

        when(bookingRepository.findAll()).thenReturn(bookings);

        //Act
        List<Booking> result = bookingService.getAllBookings();

        //Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void getAllBookings_ReturnsEmptyList(){
        //Arrange
        when(bookingRepository.findAll()).thenReturn(List.of());

        //Act
        List<Booking> result = bookingService.getAllBookings();

        //Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findAll();
    }


    @Test
    void getBookings_ById() {
        //Arrange
        UUID bookingId = UUID.randomUUID();
        Booking booking = mockBooking();
        booking.setId(bookingId);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        //Act
        Object result = bookingService.getBookings(bookingId);

        //Assert
        assertNotNull(result);
        assertTrue(result instanceof Booking);
        assertEquals(bookingId,((Booking)result).getId());
        assertEquals("Confirmed", ((Booking)result).getStatus());
        verify(bookingRepository, times(1)).findById(bookingId);

    }

    @Test
    void getBookings_ReturnsAllBookings() {
        //Arrange
        Booking booking_one = mockBooking();
        Booking booking_two = mockBooking();
        Booking booking_three = mockBooking();
        List<Booking> bookings = List.of(booking_one, booking_two, booking_three);

        when(bookingRepository.findAll()).thenReturn(bookings);

        //Act
        Object result = bookingService.getBookings(null);

        //Assert
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertEquals(3, ((List<?>) result).size());
        verify(bookingRepository, times(1)).findAll();

    }


    @Test
    void getBookingsByUserId_ReturnsCorrectBookings() {
        //Arrange
        UUID userId= UUID.randomUUID();
        Booking booking_one = mockBooking();
        Booking booking_two = mockBooking();
        Booking booking_three = mockBooking();
        List<Booking> bookings = List.of(booking_one, booking_two, booking_three);
        when(bookingRepository.findBookingByUserId(userId)).thenReturn(bookings);

        //Act
        List<Booking> result = bookingService.getBookingsByUserId(userId);

        //Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(bookingRepository, times(1)).findBookingByUserId(userId);

    }

    @Test
    void getBookingsByUserId_ThrowsExceptionWhenUserIdNotValid() {
        // Arrange
        UUID userId = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.getBookingsByUserId(userId)
        );

        assertEquals("User ID must not be null.", exception.getMessage());
        verifyNoInteractions(bookingRepository);

    }

    @Test
    void deleteBooking_Success() {
        //Arrange
        UUID bookingId = UUID.randomUUID();

        when(bookingRepository.existsById(bookingId)).thenReturn(true);

        //Act
        bookingService.deleteBooking(bookingId);

        //Assert
        verify(bookingRepository, times(1)).existsById(bookingId);
        verify(bookingRepository, times(1)).deleteById(bookingId);

    }

    @Test
    void deleteBooking_BookingNotFound() {
        //Arrange
        UUID bookingId = UUID.randomUUID();

        when(bookingRepository.existsById(bookingId)).thenReturn(false);

        //Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> bookingService.deleteBooking(bookingId)
        );
        assertEquals("Booking with ID " + bookingId + " not found.", exception.getMessage());
        verify(bookingRepository, times(1)).existsById(bookingId);
        verify(bookingRepository, times(0)).deleteById(bookingId);
    }

    @Test
    void updateBookingStatus_UserCancelsBooking() {
        // Arrange
        UUID bookingId = UUID.randomUUID();
        Booking existingBooking = mockBooking();
        existingBooking.setId(bookingId);

        Map<String, String> statusUpdate = Map.of("status", "Cancelled");

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        bookingService.updateBookingStatus(bookingId, statusUpdate);

        // Assert
        assertEquals("Cancelled", existingBooking.getStatus());
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingRepository, times(1)).save(existingBooking);
    }

    @Test
    void updateBookingStatus_AdminConfirmsBooking() {
        // Arrange
        UUID bookingId = UUID.randomUUID();
        Booking existingBooking = mockBooking();
        existingBooking.setId(bookingId);

        Map<String, String> statusUpdate = Map.of("status", "Confirmed");

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getAuthorities())
                .thenReturn((List) List.of(new SimpleGrantedAuthority("ADMIN")));
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Act
        bookingService.updateBookingStatus(bookingId, statusUpdate);

        // Assert
        assertEquals("Confirmed", existingBooking.getStatus());
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingRepository, times(1)).save(existingBooking);
    }
    @Test
    void updateBookingStatus_BookingNotFound() {
        // Arrange
        UUID bookingId = UUID.randomUUID();
        Map<String, String> statusUpdate = Map.of("status", "Cancelled");

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> bookingService.updateBookingStatus(bookingId, statusUpdate)
        );

        assertEquals("Booking with ID " + bookingId + " not found.", exception.getMessage());
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingRepository, times(0)).save(any(Booking.class));
    }

    @Test
    void updateBookingStatus_MissingStatus() {
        // Arrange
        UUID bookingId = UUID.randomUUID();
        Booking existingBooking = mockBooking();
        existingBooking.setId(bookingId);
        Map<String, String> statusUpdate = Map.of();
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.updateBookingStatus(bookingId, statusUpdate)
        );

        assertEquals("Status field is required.", exception.getMessage());
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingRepository, times(0)).save(any(Booking.class));
    }

    @Test
    void addBooking_ReturnsCorrectId() {
        // Arrange
        BookingDto bookingDto = mockBookingDto();
        User mockUser = mockUser();
        PaymentMethod mockPaymentMethod = mockPaymentMethod();
        Flight mockFlight = mockFlight();
        UUID expectedBookingId = UUID.randomUUID();

        when(userRepository.findById(bookingDto.userId())).thenReturn(Optional.of(mockUser));
        when(paymentMethodRepository.findById(bookingDto.paymentMethodId())).thenReturn(Optional.of(mockPaymentMethod));
        when(flightRepository.findById(bookingDto.flightId())).thenReturn(Optional.of(mockFlight));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            booking.setId(expectedBookingId);
            return booking;
        });

        // Act
        UUID result = bookingService.addBooking(bookingDto);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(bookingDto.userId());
        verify(paymentMethodRepository, times(1)).findById(bookingDto.paymentMethodId());
        verify(flightRepository, times(1)).findById(bookingDto.flightId());
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(passengerService, times(bookingDto.passengers().size())).addPassenger(any(PassengerDto.class), any(Booking.class));
        assertEquals(expectedBookingId, result);
    }

    @Test
    void addBooking_UserNotFound() {
        // Arrange
        BookingDto bookingDto = mockBookingDto();

        when(userRepository.findById(bookingDto.userId())).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> bookingService.addBooking(bookingDto)
        );

        assertEquals("User with ID " + bookingDto.userId() + " not found.", exception.getMessage());
        verify(userRepository, times(1)).findById(bookingDto.userId());
        verifyNoInteractions(paymentMethodRepository, flightRepository, bookingRepository, passengerService);

    }

    @Test
    void updateBooking_Success() {
        //Arrange
            //create booking
        Booking existingBooking = mockBooking();
        UUID bookingId = UUID.randomUUID();
        existingBooking.setId(bookingId);

            //create bookingDto
        BookingDto bookingDto = mockBookingDto();
        User mockUser = mockUser();
        PaymentMethod mockPaymentMethod = mockPaymentMethod();
        Flight mockFlight = mockFlight();
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));
        when(userRepository.findById(bookingDto.userId())).thenReturn(Optional.of(mockUser));
        when(paymentMethodRepository.findById(bookingDto.paymentMethodId())).thenReturn(Optional.of(mockPaymentMethod));
        when(flightRepository.findById(bookingDto.flightId())).thenReturn(Optional.of(mockFlight));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        bookingService.updateBooking(bookingId, bookingDto);

        // Assert
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(userRepository, times(1)).findById(bookingDto.userId());
        verify(paymentMethodRepository, times(1)).findById(bookingDto.paymentMethodId());
        verify(flightRepository, times(1)).findById(bookingDto.flightId());
        verify(bookingRepository, times(1)).save(existingBooking);
        verify(passengerService, times(bookingDto.passengers().size())).updatePassenger(any(UUID.class), any(PassengerDto.class));


    }

    @Test
    void updateBooking_BookingNotFound() {
        // Arrange
        UUID bookingId = UUID.randomUUID();
        BookingDto bookingDto = mockBookingDto();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> bookingService.updateBooking(bookingId, bookingDto)
        );

        assertEquals("Booking with ID " + bookingId + " not found.", exception.getMessage());
        verify(bookingRepository, times(1)).findById(bookingId);
        verifyNoInteractions(userRepository, paymentMethodRepository, flightRepository, passengerService);
    }





}
