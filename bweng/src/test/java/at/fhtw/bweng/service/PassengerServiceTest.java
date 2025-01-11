package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.PassengerDto;
import at.fhtw.bweng.dto.BaggageDto;
import at.fhtw.bweng.model.*;
import at.fhtw.bweng.repository.BaggageRepository;
import at.fhtw.bweng.repository.BaggageTypeRepository;
import at.fhtw.bweng.repository.PassengerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private BaggageRepository baggageRepository;

    @Mock
    private BaggageTypeRepository baggageTypeRepository;

    @InjectMocks
    private PassengerService passengerService;

    private static final UUID PASSENGER_ID = UUID.randomUUID();
    private static final UUID BAGGAGE_TYPE_ID = UUID.randomUUID();
    private static final UUID BOOKING_ID = UUID.randomUUID();
    private static final BaggageType BAGGAGE_TYPE = new BaggageType(BAGGAGE_TYPE_ID, "Checked Baggage", BigDecimal.valueOf(25.0));
    private static final Baggage BAGGAGE = new Baggage(UUID.randomUUID(), BAGGAGE_TYPE);
    private static final Passenger PASSENGER = new Passenger(PASSENGER_ID, "John", "Doe", LocalDate.of(1990, 1, 1), "12A", BAGGAGE);
    private static final PassengerDto PASSENGER_DTO = new PassengerDto(PASSENGER_ID, "John", "Doe", LocalDate.of(1990, 1, 1), "12A", new BaggageDto(BAGGAGE_TYPE_ID));
    private static final String STATUS = "Confirmed";
    private static final BigDecimal PRICE = BigDecimal.valueOf(500.00);
    private static final OffsetDateTime BOOKING_DATE = OffsetDateTime.now();
    private static final PaymentMethod PAYMENT_METHOD = new PaymentMethod(UUID.randomUUID(), "Credit Card");

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String GENDER = "Male";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String USERNAME = "johndoe";
    private static final String PASSWORD = "password123";
    private static final String EMAIL = "john.doe@example.com";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1990, 1, 1);
    private static final String ROLE = "USER";
    private static final String USER_STATUS = "ACTIVE";
    private static final Address ADDRESS = new Address(UUID.randomUUID(), "Main St", 123, 1001, "New York", "USA");

    private static final User USER = new User(
            USER_ID,
            GENDER,
            FIRST_NAME,
            LAST_NAME,
            USERNAME,
            PASSWORD,
            EMAIL,
            DATE_OF_BIRTH,
            ROLE,
            USER_STATUS,
            ADDRESS,
            PAYMENT_METHOD
    );

    private static final UUID FLIGHT_ID = UUID.randomUUID();
    private static final String FLIGHT_NUMBER = "AA123";
    private static final OffsetDateTime DEPARTURE_TIME = OffsetDateTime.now().plusDays(1);
    private static final OffsetDateTime ARRIVAL_TIME = DEPARTURE_TIME.plusHours(5);
    private static final Airport FLIGHT_ORIGIN = new Airport(UUID.randomUUID(), "JFK", "John F. Kennedy International Airport");
    private static final Airport FLIGHT_DESTINATION = new Airport(UUID.randomUUID(), "LAX", "Los Angeles International Airport");
    private static final Aircraft AIRCRAFT = new Aircraft(UUID.randomUUID(), "SN123", "Boeing", "737", 180, new Airline(UUID.randomUUID(), "Example Airline"));
    private static final BigDecimal FLIGHT_PRICE = BigDecimal.valueOf(300.00);
    private static final Flight FLIGHT = new Flight(
            FLIGHT_ID,
            FLIGHT_NUMBER,
            DEPARTURE_TIME,
            ARRIVAL_TIME,
            FLIGHT_ORIGIN,
            FLIGHT_DESTINATION,
            AIRCRAFT,
            FLIGHT_PRICE
    );

    private static final Booking BOOKING = new Booking(
            BOOKING_ID,
            STATUS,
            PRICE,
            BOOKING_DATE,
            USER,
            PAYMENT_METHOD,
            FLIGHT
    );


    @Test
    void addPassenger_savesPassengerAndReturnsId() {
        // arrange
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.of(BAGGAGE_TYPE));
        when(baggageRepository.save(any(Baggage.class))).thenReturn(BAGGAGE);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(PASSENGER);

        // act
        UUID result = passengerService.addPassenger(PASSENGER_DTO);

        // assert
        assertThat(result).isEqualTo(PASSENGER_ID);
    }

    @Test
    void addPassenger_throwsExceptionWhenBaggageTypeNotFound() {
        // arrange
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> passengerService.addPassenger(PASSENGER_DTO))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Baggage type with ID " + BAGGAGE_TYPE_ID + " not found");
    }

    @Test
    void addPassenger_throwsDataIntegrityViolationException() {
        // arrange
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.of(BAGGAGE_TYPE));
        when(baggageRepository.save(any(Baggage.class))).thenReturn(BAGGAGE);
        doThrow(new DataIntegrityViolationException("Passenger already exists"))
                .when(passengerRepository).save(any(Passenger.class));

        // act & assert
        assertThatThrownBy(() -> passengerService.addPassenger(PASSENGER_DTO))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Passenger already exists");

        // verify no additional interactions
        verify(baggageTypeRepository, times(1)).findById(BAGGAGE_TYPE_ID);
        verify(baggageRepository, times(1)).save(any(Baggage.class));
        verify(passengerRepository, times(1)).save(any(Passenger.class));
    }


    @Test
    void addPassenger_withBooking_savesPassengerAndReturnsId() {
        // arrange
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.of(BAGGAGE_TYPE));
        when(baggageRepository.save(any(Baggage.class))).thenReturn(BAGGAGE);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(PASSENGER);

        // act
        UUID result = passengerService.addPassenger(PASSENGER_DTO, BOOKING);

        // assert
        assertThat(result).isEqualTo(PASSENGER_ID);
        verify(passengerRepository, times(1)).save(any(Passenger.class));
    }

    @Test
    void addPassenger_withBooking_throwsDataIntegrityViolationException() {
        // arrange
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.of(BAGGAGE_TYPE));
        when(baggageRepository.save(any(Baggage.class))).thenReturn(BAGGAGE);
        doThrow(new DataIntegrityViolationException("Passenger already exists"))
                .when(passengerRepository).save(any(Passenger.class));

        // act & assert
        assertThatThrownBy(() -> passengerService.addPassenger(PASSENGER_DTO, BOOKING))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Passenger already exists");

        // verify no additional interactions
        verify(baggageTypeRepository, times(1)).findById(BAGGAGE_TYPE_ID);
        verify(baggageRepository, times(1)).save(any(Baggage.class));
        verify(passengerRepository, times(1)).save(any(Passenger.class));
    }

    @Test
    void getPassengers_withId_returnsPassengerById() {
        // arrange
        when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(PASSENGER));

        // act
        Object result = passengerService.getPassengers(PASSENGER_ID);

        // assert
        assertThat(result).isEqualTo(PASSENGER);
    }

    @Test
    void getPassengers_withoutId_returnsAllPassengers() {
        // arrange
        when(passengerRepository.findAll()).thenReturn(List.of(PASSENGER));

        // act
        Object result = passengerService.getPassengers(null);

        // assert
        assertThat(result).isEqualTo(List.of(PASSENGER));
    }

    @Test
    void getPassengerById_throwsExceptionWhenNotFound() {
        // arrange
        when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> passengerService.getPassengerById(PASSENGER_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Passenger with ID " + PASSENGER_ID + " not found");
    }

    @Test
    void updatePassenger_updatesPassenger() {
        // arrange
        when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(PASSENGER));
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.of(BAGGAGE_TYPE));
        when(baggageRepository.save(any(Baggage.class))).thenReturn(BAGGAGE);

        // act
        passengerService.updatePassenger(PASSENGER_ID, PASSENGER_DTO);

        // assert
        verify(passengerRepository, times(1)).save(any(Passenger.class));
    }

    @Test
    void updatePassenger_throwsExceptionWhenBaggageTypeNotFound() {
        // arrange
        when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(PASSENGER));
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> passengerService.updatePassenger(PASSENGER_ID, PASSENGER_DTO))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Baggage type with ID " + BAGGAGE_TYPE_ID + " not found");
    }

    @Test
    void updatePassenger_createsNewBaggageWhenBaggageIsNull() {
        // arrange
        Passenger passengerWithoutBaggage = new Passenger(
                PASSENGER_ID,
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "12A",
                null
        );

        when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(passengerWithoutBaggage));
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.of(BAGGAGE_TYPE));
        when(baggageRepository.save(any(Baggage.class))).thenReturn(BAGGAGE);

        // act
        passengerService.updatePassenger(PASSENGER_ID, PASSENGER_DTO);

        // assert
        verify(baggageRepository, times(1)).save(any(Baggage.class)); // Ensure baggage is created
        verify(passengerRepository, times(1)).save(any(Passenger.class)); // Ensure passenger is saved
    }

    @Test
    void updatePassenger_throwsDataIntegrityViolationException() {
        // arrange
        when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(PASSENGER));
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.of(BAGGAGE_TYPE));
        when(baggageRepository.save(any(Baggage.class))).thenReturn(BAGGAGE);
        doThrow(new DataIntegrityViolationException("Passenger with the same data already exists."))
                .when(passengerRepository).save(any(Passenger.class));

        // act & assert
        assertThatThrownBy(() -> passengerService.updatePassenger(PASSENGER_ID, PASSENGER_DTO))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Passenger with the same data already exists.");

        // verify correct interactions
        verify(baggageRepository, times(1)).save(any(Baggage.class)); // Ensure baggage was saved
        verify(passengerRepository, times(1)).save(any(Passenger.class)); // Ensure passenger save was attempted
    }

    @Test
    void deletePassenger_deletesPassenger() {
        // arrange
        when(passengerRepository.existsById(PASSENGER_ID)).thenReturn(true);

        // act
        passengerService.deletePassenger(PASSENGER_ID);

        // assert
        verify(passengerRepository, times(1)).deleteById(PASSENGER_ID);
    }

    @Test
    void deletePassenger_throwsExceptionWhenNotFound() {
        // arrange
        when(passengerRepository.existsById(PASSENGER_ID)).thenReturn(false);

        // act & assert
        assertThatThrownBy(() -> passengerService.deletePassenger(PASSENGER_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Passenger with ID " + PASSENGER_ID + " not found");
    }

    @Test
    void findPassengersByBookingId_returnsPassengers() {
        // arrange
        when(passengerRepository.findByBookingId(BOOKING_ID)).thenReturn(Optional.of(List.of(PASSENGER)));

        // act
        List<Passenger> result = passengerService.findPassengersByBookingId(BOOKING_ID);

        // assert
        assertThat(result).containsExactly(PASSENGER);
    }

    @Test
    void findPassengersByBookingId_returnsEmptyListWhenNoneFound() {
        // arrange
        when(passengerRepository.findByBookingId(BOOKING_ID)).thenReturn(Optional.empty());

        // act
        List<Passenger> result = passengerService.findPassengersByBookingId(BOOKING_ID);

        // assert
        assertThat(result).isEmpty();
    }
}
