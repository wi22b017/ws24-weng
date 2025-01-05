package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.AirlineDto;
import at.fhtw.bweng.model.Airline;
import at.fhtw.bweng.repository.AirlineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirlineServiceTest {

    @Mock
    private AirlineRepository airlineRepository;

    @InjectMocks
    private AirlineService airlineService;

    private static final UUID AIRLINE_ID = UUID.randomUUID();
    private static final Airline AIRLINE = new Airline(AIRLINE_ID,"Example Airline");
    private static final AirlineDto AIRLINE_DTO = new AirlineDto("Example Airline");


    @Test
    void addAirline_savesAirlineAndReturnsId() {
        // arrange
        when(airlineRepository.save(any(Airline.class))).thenReturn(AIRLINE);

        // act
        UUID result = airlineService.addAirline(AIRLINE_DTO);

        // assert
        assertThat(result).isEqualTo(AIRLINE_ID);
        //verify(airlineRepository, times(1)).save(any(Airline.class));
    }

    @Test
    void addAirline_throwsExceptionWhenDuplicate() {
        // arrange
        when(airlineRepository.save(any(Airline.class))).thenThrow(new DataIntegrityViolationException("Airline with the same data already exists."));

        // act & assert
        assertThatThrownBy(() -> airlineService.addAirline(AIRLINE_DTO))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Airline with the same data already exists.");
    }

    @Test
    void getAirlineById_returnsAirline() {
        // arrange
        when(airlineRepository.findById(AIRLINE_ID)).thenReturn(Optional.of(AIRLINE));

        // act
        Airline result = airlineService.getAirlineById(AIRLINE_ID);

        // assert
        assertThat(result).isEqualTo(AIRLINE);
    }

    @Test
    void getAirlineById_throwsExceptionWhenNotFound() {
        // arrange
        when(airlineRepository.findById(AIRLINE_ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> airlineService.getAirlineById(AIRLINE_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Airline with id " + AIRLINE_ID + " not found");
    }

    @Test
    void getAirlines_withId_returnsAirline() {
        // arrange
        when(airlineRepository.findById(AIRLINE_ID)).thenReturn(Optional.of(AIRLINE));

        // act
        Object result = airlineService.getAirlines(AIRLINE_ID);

        // assert
        assertThat(result).isEqualTo(AIRLINE);
    }

    @Test
    void getAirlines_withoutId_returnsAllAirlines() {
        // arrange
        when(airlineRepository.findAll()).thenReturn(List.of(AIRLINE));

        // act
        Object result = airlineService.getAirlines(null);

        // assert
        assertThat(result).isEqualTo(List.of(AIRLINE));
    }

    @Test
    void getAllAirlines_returnsAllAirlines() {
        // arrange
        when(airlineRepository.findAll()).thenReturn(List.of(AIRLINE));

        // act
        List<Airline> result = airlineService.getAllAirlines();

        // assert
        assertThat(result).containsExactly(AIRLINE);
    }

    @Test
    void getAirlineByName_returnsAirline() {
        // arrange
        String airlineName = "Example Airline";
        when(airlineRepository.findByName(airlineName)).thenReturn(Optional.of(AIRLINE));

        // act
        Airline result = airlineService.getAirlineByName(airlineName);

        // assert
        assertThat(result).isEqualTo(AIRLINE);
    }

    @Test
    void getAirlineByName_throwsExceptionWhenNotFound() {
        // arrange
        String airlineName = "Nonexistent Airline";
        when(airlineRepository.findByName(airlineName)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> airlineService.getAirlineByName(airlineName))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Airline with name " + airlineName + " not found");
    }

    @Test
    void updateAirline_updatesAirline() {
        // arrange
        when(airlineRepository.findById(AIRLINE_ID)).thenReturn(Optional.of(AIRLINE));
        when(airlineRepository.save(any(Airline.class))).thenReturn(AIRLINE);

        // act
        airlineService.updateAirline(AIRLINE_ID, AIRLINE_DTO);

        // assert
        verify(airlineRepository, times(1)).save(any(Airline.class));
    }

    @Test
    void updateAirline_throwsExceptionWhenDuplicateName() {
        // arrange
        when(airlineRepository.findById(AIRLINE_ID)).thenReturn(Optional.of(AIRLINE));
        doThrow(new DataIntegrityViolationException("Airline with the same name already exists."))
                .when(airlineRepository).save(any(Airline.class));

        // act & assert
        assertThatThrownBy(() -> airlineService.updateAirline(AIRLINE_ID, AIRLINE_DTO))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Airline with the same name already exists.");
    }

    @Test
    void deleteAirline_deletesAirline() {
        // arrange
        when(airlineRepository.existsById(AIRLINE_ID)).thenReturn(true);

        // act
        airlineService.deleteAirline(AIRLINE_ID);

        // assert
        verify(airlineRepository, times(1)).deleteById(AIRLINE_ID);
    }

    @Test
    void deleteAirline_throwsExceptionWhenNotFound() {
        // arrange
        when(airlineRepository.existsById(AIRLINE_ID)).thenReturn(false);

        // act & assert
        assertThatThrownBy(() -> airlineService.deleteAirline(AIRLINE_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Airline with id " + AIRLINE_ID + " not found");
    }



}