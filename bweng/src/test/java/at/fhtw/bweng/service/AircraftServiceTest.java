package at.fhtw.bweng.service;

import static org.junit.jupiter.api.Assertions.*;

import at.fhtw.bweng.dto.AircraftDto;
import at.fhtw.bweng.dto.AirlineDto;
import at.fhtw.bweng.model.Aircraft;
import at.fhtw.bweng.model.Airline;
import at.fhtw.bweng.repository.AircraftRepository;
import at.fhtw.bweng.repository.AirlineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AircraftServiceTest {

    @Mock
    private AircraftRepository aircraftRepository;

    @Mock
    private AirlineRepository airlineRepository;

    @InjectMocks
    private AircraftService aircraftService;

    private static final UUID AIRCRAFT_ID = UUID.randomUUID();
    private static final UUID AIRLINE_ID = UUID.randomUUID();
    private static final Airline AIRLINE = new Airline(AIRLINE_ID, "Example Airline");
    private static final Aircraft AIRCRAFT = new Aircraft(AIRCRAFT_ID, "SN123", "Boeing", "737", 180, AIRLINE);
    private static final AircraftDto AIRCRAFT_DTO = new AircraftDto("Boeing", "737", 180, new AirlineDto("Example Airline"), "SN123");

    @Test
    void addAircraft_savesAircraftAndReturnsId() {
        // arrange
        when(airlineRepository.findByName(AIRCRAFT_DTO.airline().name())).thenReturn(Optional.of(AIRLINE));
        when(aircraftRepository.save(any(Aircraft.class))).thenReturn(AIRCRAFT);

        // act
        UUID result = aircraftService.addAircraft(AIRCRAFT_DTO);

        // assert
        assertThat(result).isEqualTo(AIRCRAFT_ID);
    }

    @Test
    void addAircraft_createsNewAirlineIfNotFound() {
        // arrange
        when(airlineRepository.findByName(AIRCRAFT_DTO.airline().name())).thenReturn(Optional.empty());
        when(airlineRepository.save(any(Airline.class))).thenReturn(AIRLINE);
        when(aircraftRepository.save(any(Aircraft.class))).thenReturn(AIRCRAFT);

        // act
        UUID result = aircraftService.addAircraft(AIRCRAFT_DTO);

        // assert
        assertThat(result).isEqualTo(AIRCRAFT_ID);
        verify(airlineRepository, times(1)).save(any(Airline.class));
        verify(aircraftRepository, times(1)).save(any(Aircraft.class));
    }

    @Test
    void addAircraft_throwsExceptionWhenDuplicate() {
        // arrange
        when(airlineRepository.findByName(AIRCRAFT_DTO.airline().name())).thenReturn(Optional.of(AIRLINE));
        when(aircraftRepository.save(any(Aircraft.class))).thenThrow(new DataIntegrityViolationException("Aircraft with the same data already exists."));

        // act & assert
        assertThatThrownBy(() -> aircraftService.addAircraft(AIRCRAFT_DTO))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Aircraft with the same data already exists.");
    }

    @Test
    void getAircrafts_withId_returnsAircraftById() {
        // arrange
        when(aircraftRepository.findById(AIRCRAFT_ID)).thenReturn(Optional.of(AIRCRAFT));

        // act
        Object result = aircraftService.getAircrafts(AIRCRAFT_ID, null);

        // assert
        assertThat(result).isEqualTo(AIRCRAFT);
    }

    @Test
    void getAircrafts_withSerialNumber_returnsAircraftBySerialNumber() {
        // arrange
        when(aircraftRepository.findBySerialNumber(AIRCRAFT_DTO.serialNumber())).thenReturn(Optional.of(AIRCRAFT));

        // act
        Object result = aircraftService.getAircrafts(null, AIRCRAFT_DTO.serialNumber());

        // assert
        assertThat(result).isEqualTo(AIRCRAFT);
    }

    @Test
    void getAircrafts_withoutIdOrSerialNumber_returnsAllAircrafts() {
        // arrange
        when(aircraftRepository.findAll()).thenReturn(List.of(AIRCRAFT));

        // act
        Object result = aircraftService.getAircrafts(null, null);

        // assert
        assertThat(result).isEqualTo(List.of(AIRCRAFT));
    }

    @Test
    void getAircraftById_throwsExceptionWhenNotFound() {
        // arrange
        when(aircraftRepository.findById(AIRCRAFT_ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> aircraftService.getAircraftById(AIRCRAFT_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Aircraft with ID " + AIRCRAFT_ID + " not found");
    }

    @Test
    void getAircraftBySerialNumber_throwsExceptionWhenNotFound() {
        // arrange
        when(aircraftRepository.findBySerialNumber(AIRCRAFT_DTO.serialNumber())).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> aircraftService.getAircraftBySerialNumber(AIRCRAFT_DTO.serialNumber()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Aircraft with serial number " + AIRCRAFT_DTO.serialNumber() + " not found");
    }

    @Test
    void updateAircraft_updatesAircraft() {
        // arrange
        when(aircraftRepository.findById(AIRCRAFT_ID)).thenReturn(Optional.of(AIRCRAFT));
        when(airlineRepository.findByName(AIRCRAFT_DTO.airline().name())).thenReturn(Optional.of(AIRLINE));

        // act
        aircraftService.updateAircraft(AIRCRAFT_ID, AIRCRAFT_DTO);

        // assert
        verify(aircraftRepository, times(1)).save(any(Aircraft.class));
    }

    @Test
    void updateAircraft_createsNewAirlineIfNotFound() {
        // arrange
        when(aircraftRepository.findById(AIRCRAFT_ID)).thenReturn(Optional.of(AIRCRAFT));
        when(airlineRepository.findByName(AIRCRAFT_DTO.airline().name())).thenReturn(Optional.empty());
        when(airlineRepository.save(any(Airline.class))).thenReturn(AIRLINE);

        // act
        aircraftService.updateAircraft(AIRCRAFT_ID, AIRCRAFT_DTO);

        // assert
        verify(airlineRepository, times(1)).save(any(Airline.class));
        verify(aircraftRepository, times(1)).save(any(Aircraft.class));
    }

    @Test
    void updateAircraft_throwsExceptionWhenDuplicate() {
        // arrange
        when(aircraftRepository.findById(AIRCRAFT_ID)).thenReturn(Optional.of(AIRCRAFT));
        when(airlineRepository.findByName(AIRCRAFT_DTO.airline().name())).thenReturn(Optional.of(AIRLINE));
        doThrow(new DataIntegrityViolationException("Aircraft with the same data already exists."))
                .when(aircraftRepository).save(any(Aircraft.class));

        // act & assert
        assertThatThrownBy(() -> aircraftService.updateAircraft(AIRCRAFT_ID, AIRCRAFT_DTO))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Aircraft with the same data already exists.");
    }

    @Test
    void deleteAircraft_deletesAircraft() {
        // arrange
        when(aircraftRepository.existsById(AIRCRAFT_ID)).thenReturn(true);

        // act
        aircraftService.deleteAircraft(AIRCRAFT_ID);

        // assert
        verify(aircraftRepository, times(1)).deleteById(AIRCRAFT_ID);
    }

    @Test
    void deleteAircraft_throwsExceptionWhenNotFound() {
        // arrange
        when(aircraftRepository.existsById(AIRCRAFT_ID)).thenReturn(false);

        // act & assert
        assertThatThrownBy(() -> aircraftService.deleteAircraft(AIRCRAFT_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Aircraft with ID " + AIRCRAFT_ID + " not found");
    }
}