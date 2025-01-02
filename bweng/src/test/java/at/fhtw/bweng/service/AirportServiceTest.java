package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.AirportDto;
import at.fhtw.bweng.model.Airport;
import at.fhtw.bweng.repository.AirportRepository;
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
class AirportServiceTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private AirportService airportService;

    private static final UUID AIRPORT_ID = UUID.randomUUID();
    private static final Airport AIRPORT = new Airport(AIRPORT_ID, "Example Airport", "EXA");
    private static final AirportDto AIRPORT_DTO = new AirportDto("EXA", "Example Airport");

    @Test
    void addAirport_savesAirportAndReturnsId() {
        // arrange
        when(airportRepository.save(any(Airport.class))).thenReturn(AIRPORT);

        // act
        UUID result = airportService.addAirport(AIRPORT_DTO);

        // assert
        assertThat(result).isEqualTo(AIRPORT_ID);
    }

    @Test
    void addAirport_throwsExceptionWhenDuplicate() {
        // arrange
        when(airportRepository.save(any(Airport.class))).thenThrow(new DataIntegrityViolationException("Airport with the same data already exists."));

        // act & assert
        assertThatThrownBy(() -> airportService.addAirport(AIRPORT_DTO))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Airport with the same data already exists.");
    }

    @Test
    void getAirportById_returnsAirport() {
        // arrange
        when(airportRepository.findById(AIRPORT_ID)).thenReturn(Optional.of(AIRPORT));

        // act
        Airport result = airportService.getAirportById(AIRPORT_ID);

        // assert
        assertThat(result).isEqualTo(AIRPORT);
    }

    @Test
    void getAirportById_throwsExceptionWhenNotFound() {
        // arrange
        when(airportRepository.findById(AIRPORT_ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> airportService.getAirportById(AIRPORT_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Airport with ID " + AIRPORT_ID + " not found");
    }

    @Test
    void getAirports_withId_returnsAirport() {
        // arrange
        when(airportRepository.findById(AIRPORT_ID)).thenReturn(Optional.of(AIRPORT));

        // act
        Object result = airportService.getAirports(AIRPORT_ID);

        // assert
        assertThat(result).isEqualTo(AIRPORT);
    }

    @Test
    void getAirports_withoutId_returnsAllAirports() {
        // arrange
        when(airportRepository.findAll()).thenReturn(List.of(AIRPORT));

        // act
        Object result = airportService.getAirports(null);

        // assert
        assertThat(result).isEqualTo(List.of(AIRPORT));
    }

    @Test
    void getAllAirports_returnsAllAirports() {
        // arrange
        when(airportRepository.findAll()).thenReturn(List.of(AIRPORT));

        // act
        List<Airport> result = airportService.getAllAirports();

        // assert
        assertThat(result).containsExactly(AIRPORT);
    }

    @Test
    void updateAirport_updatesAirport() {
        // arrange
        when(airportRepository.findById(AIRPORT_ID)).thenReturn(Optional.of(AIRPORT));
        when(airportRepository.save(any(Airport.class))).thenReturn(AIRPORT);

        // act
        airportService.updateAirport(AIRPORT_ID, AIRPORT_DTO);

        // assert
        verify(airportRepository, times(1)).save(any(Airport.class));
    }

    @Test
    void updateAirport_throwsExceptionWhenDuplicate() {
        // arrange
        when(airportRepository.findById(AIRPORT_ID)).thenReturn(Optional.of(AIRPORT));
        doThrow(new DataIntegrityViolationException("Airport with the same data already exists."))
                .when(airportRepository).save(any(Airport.class));

        // act & assert
        assertThatThrownBy(() -> airportService.updateAirport(AIRPORT_ID, AIRPORT_DTO))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Airport with the same data already exists.");
    }

    @Test
    void deleteAirport_deletesAirport() {
        // arrange
        when(airportRepository.existsById(AIRPORT_ID)).thenReturn(true);

        // act
        airportService.deleteAirport(AIRPORT_ID);

        // assert
        verify(airportRepository, times(1)).deleteById(AIRPORT_ID);
    }

    @Test
    void deleteAirport_throwsExceptionWhenNotFound() {
        // arrange
        when(airportRepository.existsById(AIRPORT_ID)).thenReturn(false);

        // act & assert
        assertThatThrownBy(() -> airportService.deleteAirport(AIRPORT_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Airport with ID " + AIRPORT_ID + " not found");
    }
}