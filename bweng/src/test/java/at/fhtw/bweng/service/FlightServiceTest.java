package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.AircraftDto;
import at.fhtw.bweng.dto.AirlineDto;
import at.fhtw.bweng.dto.AirportDto;
import at.fhtw.bweng.dto.FlightDto;
import at.fhtw.bweng.model.Aircraft;
import at.fhtw.bweng.model.Airline;
import at.fhtw.bweng.model.Airport;
import at.fhtw.bweng.model.Flight;
import at.fhtw.bweng.repository.AircraftRepository;
import at.fhtw.bweng.repository.AirlineRepository;
import at.fhtw.bweng.repository.AirportRepository;
import at.fhtw.bweng.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private AirportRepository airportRepository;
    @Mock
    private AirlineRepository airlineRepository;
    @Mock
    private AircraftRepository aircraftRepository;

    @InjectMocks
    private FlightService flightService;

    @Test
    void addFlight_ReturnsCorrectId() {
        //arrange
        FlightDto flightDto = new FlightDto(  "FL123",
                "2024-01-01T10:00:00+00:00",
                "2024-01-01T14:00:00+00:00",
                new AirportDto("ORIG", "Origin Airport"),
                new AirportDto("DEST", "Destination Airport"),
                new AircraftDto("Boeing", "737", 180, new AirlineDto("Test Airline"), "SN123"),
                new BigDecimal(150.0));
        Airport origin = new Airport(UUID.randomUUID(), "Origin Airport", "ORIG");
        Airport destination = new Airport(UUID.randomUUID(), "Destination Airport", "DEST");
        Airline airline = new Airline(UUID.randomUUID(), "Test Airline");
        Aircraft aircraft = new Aircraft(UUID.randomUUID(), "SN123", "Boeing", "737", 180, airline);
        UUID expectedFlightId = UUID.randomUUID();
        when(airportRepository.findByCode("ORIG")).thenReturn(Optional.of(origin));
        when(airportRepository.findByCode("DEST")).thenReturn(Optional.of(destination));
        when(airlineRepository.findByName("Test Airline")).thenReturn(Optional.of(airline));
        when(aircraftRepository.findBySerialNumber("SN123")).thenReturn(Optional.of(aircraft));
        when(flightRepository.save(any(Flight.class))).thenAnswer(invocation -> {
            Flight flight = invocation.getArgument(0); //retrieves Flight object passed to the save method
            flight.setId(expectedFlightId);
            return flight;
        });

        // Act
        UUID flightId = flightService.addFlight(flightDto);

        // Assert
        assertNotNull(flightId);
        verify(airportRepository, times(1)).findByCode("ORIG");
        verify(airportRepository, times(1)).findByCode("DEST");
        verify(airlineRepository, times(1)).findByName("Test Airline");
        verify(aircraftRepository, times(1)).findBySerialNumber("SN123");
        assertEquals(expectedFlightId, flightId);
    }

    @Test
    void addFlight_ThrowsDataIntegrityViolationExceptionWhenDuplicate() {
        //arrange
        FlightDto flightDto = new FlightDto(  "FL123",
                "2024-01-01T10:00:00+00:00",
                "2024-01-01T14:00:00+00:00",
                new AirportDto("ORIG", "Origin Airport"),
                new AirportDto("DEST", "Destination Airport"),
                new AircraftDto("Boeing", "737", 180, new AirlineDto("Test Airline"), "SN123"),
                new BigDecimal(150.0));
        Airport origin = new Airport(UUID.randomUUID(), "Origin Airport", "ORIG");
        Airport destination = new Airport(UUID.randomUUID(), "Destination Airport", "DEST");
        Airline airline = new Airline(UUID.randomUUID(), "Test Airline");
        Aircraft aircraft = new Aircraft(UUID.randomUUID(), "SN123", "Boeing", "737", 180, airline);

        when(airportRepository.findByCode("ORIG")).thenReturn(Optional.of(origin));
        when(airportRepository.findByCode("DEST")).thenReturn(Optional.of(destination));
        when(airlineRepository.findByName("Test Airline")).thenReturn(Optional.of(airline));
        when(aircraftRepository.findBySerialNumber("SN123")).thenReturn(Optional.of(aircraft));
        when(flightRepository.save(any(Flight.class))).thenThrow(new DataIntegrityViolationException("Flight with the same data already exists."));

        //Act & Assert
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> flightService.addFlight(flightDto)
        );

        assertEquals("Flight with the same data already exists.", exception.getMessage());

    }

    @Test
    void getFlightById_ReturnsCorrectFlight() {
        //Arrange
        Airport origin = new Airport(UUID.randomUUID(), "Origin Airport", "ORIG");
        Airport destination = new Airport(UUID.randomUUID(), "Destination Airport", "DEST");
        Airline airline = new Airline(UUID.randomUUID(), "Test Airline");
        Aircraft aircraft = new Aircraft(UUID.randomUUID(), "SN123", "Boeing", "737", 200, airline);
        Flight flight = new Flight(null, "FL123", OffsetDateTime.now(), OffsetDateTime.now(), origin, destination, aircraft, new BigDecimal(150.0));
        UUID flightId = UUID.randomUUID();
        flight.setId(flightId);

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        //Act
        Flight result = flightService.getFlightById(flightId);

        //Assert
        assertNotNull(result);
        assertEquals(flightId, result.getId());
        verify(flightRepository, times(1)).findById(flightId);
    }

    @Test
    void getFlightById_ThrowsExceptionWhenNotFound() {
        // Arrange
        UUID flightId = UUID.randomUUID();
        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> flightService.getFlightById(flightId)
        );
        assertEquals("Flight with ID " + flightId + " not found", exception.getMessage());
        verify(flightRepository, times(1)).findById(flightId);
    }

    @Test
    void getFlightByFlightNumber_ReturnsCorrectFlight() {
       // Arrange
        Airport origin = new Airport(UUID.randomUUID(), "Origin Airport", "ORIG");
        Airport destination = new Airport(UUID.randomUUID(), "Destination Airport", "DEST");
        Airline airline = new Airline(UUID.randomUUID(), "Test Airline");
        Aircraft aircraft = new Aircraft(UUID.randomUUID(), "SN123", "Boeing", "737", 200, airline);
        Flight flight = new Flight(UUID.randomUUID(), "", OffsetDateTime.now(), OffsetDateTime.now(), origin, destination, aircraft, new BigDecimal(150.0));
        String flightNumber = "FL123";
        flight.setFlightNumber(flightNumber);
        when(flightRepository.findByFlightNumber(flightNumber)).thenReturn(Optional.of(flight));

        // Act
        Flight result = flightService.getFlightByFlightNumber(flightNumber);

        // Assert
        assertNotNull(result);
        assertEquals(flightNumber, result.getFlightNumber());
        verify(flightRepository, times(1)).findByFlightNumber(flightNumber);

    }

    @Test
    void getFlightByFlightNumber_ThrowsExceptionWhenNofFound() {
        // Arrange
        String flightNumber = "FL000";
        when(flightRepository.findByFlightNumber(flightNumber)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> flightService.getFlightByFlightNumber(flightNumber)
        );
        assertEquals("Flight with flight number " + flightNumber + " not found", exception.getMessage());
        verify(flightRepository, times(1)).findByFlightNumber(flightNumber);

    }


    @Test
    void getAllFlights_ReturnsFlights() {
        //Arrange
        Airport origin = new Airport(UUID.randomUUID(), "Origin Airport", "ORIG");
        Airport destination = new Airport(UUID.randomUUID(), "Destination Airport", "DEST");
        Airline airline = new Airline(UUID.randomUUID(), "Test Airline");
        Aircraft aircraft = new Aircraft(UUID.randomUUID(), "SN123", "Boeing", "737", 200, airline);
        Flight flight_one = new Flight(UUID.randomUUID(), "FL123", OffsetDateTime.now(), OffsetDateTime.now(), origin, destination, aircraft, new BigDecimal(150.0));
        Flight flight_two = new Flight(UUID.randomUUID(), "FL456", OffsetDateTime.now(), OffsetDateTime.now(), origin, destination, aircraft, new BigDecimal(250.0));
        Flight flight_three = new Flight(UUID.randomUUID(), "FL789", OffsetDateTime.now(), OffsetDateTime.now(), origin, destination, aircraft, new BigDecimal(350.0));
        List<Flight> flights = List.of(flight_one, flight_two, flight_three);
        when(flightRepository.findAll()).thenReturn(flights);

        // Act
        List<Flight> result = flightService.getAllFlights();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(flightRepository, times(1)).findAll();

    }

    @Test
    void getAllFlights_ReturnsEmptyList() {
        // Arrange
        when(flightRepository.findAll()).thenReturn(List.of());

        // Act
        List<Flight> result = flightService.getAllFlights();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(flightRepository, times(1)).findAll();
    }

    @Test
    void getFlights_WithId() {
        //Arrange
        Airport origin = new Airport(UUID.randomUUID(), "Origin Airport", "ORIG");
        Airport destination = new Airport(UUID.randomUUID(), "Destination Airport", "DEST");
        Airline airline = new Airline(UUID.randomUUID(), "Test Airline");
        Aircraft aircraft = new Aircraft(UUID.randomUUID(), "SN123", "Boeing", "737", 200, airline);
        Flight flight = new Flight(null, "FL123", OffsetDateTime.now(), OffsetDateTime.now(), origin, destination, aircraft, new BigDecimal(150.0));
        UUID flightId = UUID.randomUUID();
        flight.setId(flightId);

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        //Act
        Object result = flightService.getFlights(flightId, null);

        //Assert
        assertNotNull(result);
        assertTrue(result instanceof Flight);
        assertEquals(flightId, ((Flight) result).getId());
        verify(flightRepository, times(1)).findById(flightId);
    }
    @Test
    void getFlights_WithFlightNumber() {
        // Arrange
        Airport origin = new Airport(UUID.randomUUID(), "Origin Airport", "ORIG");
        Airport destination = new Airport(UUID.randomUUID(), "Destination Airport", "DEST");
        Airline airline = new Airline(UUID.randomUUID(), "Test Airline");
        Aircraft aircraft = new Aircraft(UUID.randomUUID(), "SN123", "Boeing", "737", 200, airline);
        Flight flight = new Flight(UUID.randomUUID(), "", OffsetDateTime.now(), OffsetDateTime.now(), origin, destination, aircraft, new BigDecimal(150.0));
        String flightNumber = "FL123";
        flight.setFlightNumber(flightNumber);
        when(flightRepository.findByFlightNumber(flightNumber)).thenReturn(Optional.of(flight));

        // Act
        Object result = flightService.getFlights(null, flightNumber);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Flight);
        assertEquals(flightNumber, ((Flight) result).getFlightNumber());
        verify(flightRepository, times(1)).findByFlightNumber(flightNumber);
    }

    @Test
    void getFlights_AllFlights() {
        //Arrange
        Airport origin = new Airport(UUID.randomUUID(), "Origin Airport", "ORIG");
        Airport destination = new Airport(UUID.randomUUID(), "Destination Airport", "DEST");
        Airline airline = new Airline(UUID.randomUUID(), "Test Airline");
        Aircraft aircraft = new Aircraft(UUID.randomUUID(), "SN123", "Boeing", "737", 200, airline);
        Flight flight_one = new Flight(UUID.randomUUID(), "FL123", OffsetDateTime.now(), OffsetDateTime.now(), origin, destination, aircraft, new BigDecimal(150.0));
        Flight flight_two = new Flight(UUID.randomUUID(), "FL456", OffsetDateTime.now(), OffsetDateTime.now(), origin, destination, aircraft, new BigDecimal(250.0));
        Flight flight_three = new Flight(UUID.randomUUID(), "FL789", OffsetDateTime.now(), OffsetDateTime.now(), origin, destination, aircraft, new BigDecimal(350.0));
        List<Flight> flights = List.of(flight_one, flight_two, flight_three);
        when(flightRepository.findAll()).thenReturn(flights);

        //Act
        Object result = flightService.getFlights(null, null);

        //Assert
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertEquals(3, ((List<?>) result).size());
        verify(flightRepository, times(1)).findAll();
    }

    @Test
    void updateFlight_Success() {
        //Arrange
            //create flight
        Airport origin = new Airport(UUID.randomUUID(), "Origin Airport", "ORIG");
        Airport destination = new Airport(UUID.randomUUID(), "Destination Airport", "DEST");
        Airline airline = new Airline(UUID.randomUUID(), "Test Airline");
        Aircraft aircraft = new Aircraft(UUID.randomUUID(), "SN123", "Boeing", "737", 200, airline);
        Flight existingFlight = new Flight(null, "FL123", OffsetDateTime.now(), OffsetDateTime.now(), origin, destination, aircraft, new BigDecimal(150.0));
        UUID flightId = UUID.randomUUID();
        existingFlight.setId(flightId);

            //create flightDto
        FlightDto flightDto = new FlightDto(  "FL123",
                "2024-01-01T10:00:00+00:00",
                "2024-01-01T14:00:00+00:00",
                new AirportDto("ORIGNEW", "New Origin Airport"),
                new AirportDto("DESTNEW", "New Destination Airport"),
                new AircraftDto("Boeing", "737", 180, new AirlineDto("New Test Airline"), "SN1234"),
                new BigDecimal(150.0));
        Airport newOrigin = new Airport(UUID.randomUUID(), "New Origin Airport", "ORIGNEW");
        Airport newDestination = new Airport(UUID.randomUUID(), "New Destination Airport", "DESTNEW");
        Airline newAirline = new Airline(UUID.randomUUID(), "New Test Airline");
        Aircraft newAircraft = new Aircraft(UUID.randomUUID(), "SN1234", "Boeing", "737", 180, newAirline);

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(existingFlight));
        when(airportRepository.findByCode("ORIGNEW")).thenReturn(Optional.of(newOrigin));
        when(airportRepository.findByCode("DESTNEW")).thenReturn(Optional.of(newDestination));
        when(airlineRepository.findByName("New Test Airline")).thenReturn(Optional.of(newAirline));
        when(aircraftRepository.findBySerialNumber("SN1234")).thenReturn(Optional.of(newAircraft));
        when(flightRepository.save(any(Flight.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        flightService.updateFlight(flightId, flightDto);

        // Assert
        verify(flightRepository, times(1)).findById(flightId);
        verify(airportRepository, times(1)).findByCode("ORIGNEW");
        verify(airportRepository, times(1)).findByCode("DESTNEW");
        verify(airlineRepository, times(1)).findByName("New Test Airline");
        verify(aircraftRepository, times(1)).findBySerialNumber("SN1234");
        verify(flightRepository, times(1)).save(any(Flight.class));
    }

    @Test
    void updateFlight_FlightNotFound() {
        //Arrange
        FlightDto flightDto = new FlightDto(  "FL123",
                "2024-01-01T10:00:00+00:00",
                "2024-01-01T14:00:00+00:00",
                new AirportDto("ORIGNEW", "New Origin Airport"),
                new AirportDto("DESTNEW", "New Destination Airport"),
                new AircraftDto("Boeing", "737", 180, new AirlineDto("New Test Airline"), "SN1234"),
                new BigDecimal(150.0));
        Airport newOrigin = new Airport(UUID.randomUUID(), "New Origin Airport", "ORIGNEW");
        Airport newDestination = new Airport(UUID.randomUUID(), "New Destination Airport", "DESTNEW");
        Airline newAirline = new Airline(UUID.randomUUID(), "New Test Airline");
        Aircraft newAircraft = new Aircraft(UUID.randomUUID(), "SN1234", "Boeing", "737", 180, newAirline);
        UUID flightId = UUID.randomUUID();
        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> flightService.updateFlight(flightId, flightDto)
        );

        assertEquals("Flight with ID " + flightId + " not found", exception.getMessage());
        verify(flightRepository, times(1)).findById(flightId);
        verifyNoInteractions(airportRepository, airlineRepository, aircraftRepository);
    }

    @Test
    void deleteFlight_Success() {
        // Arrange
        UUID flightId = UUID.randomUUID();

        when(flightRepository.existsById(flightId)).thenReturn(true);

        // Act
        flightService.deleteFlight(flightId);

        // Assert
        verify(flightRepository, times(1)).existsById(flightId);
        verify(flightRepository, times(1)).deleteById(flightId);
    }

    @Test
    void deleteFlight_FlightNotFound() {
        // Arrange
        UUID flightId = UUID.randomUUID();

        when(flightRepository.existsById(flightId)).thenReturn(false);

        // Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> flightService.deleteFlight(flightId)
        );

        assertEquals("Flight with ID " + flightId + " not found", exception.getMessage());
        verify(flightRepository, times(1)).existsById(flightId);
        verify(flightRepository, times(0)).deleteById(flightId);

    }

}
