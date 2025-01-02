package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.BaggageDto;
import at.fhtw.bweng.model.Baggage;
import at.fhtw.bweng.model.BaggageType;
import at.fhtw.bweng.repository.BaggageRepository;
import at.fhtw.bweng.repository.BaggageTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaggageServiceTest {

    @Mock
    private BaggageRepository baggageRepository;

    @Mock
    private BaggageTypeRepository baggageTypeRepository;

    @InjectMocks
    private BaggageService baggageService;

    private static final UUID BAGGAGE_ID = UUID.randomUUID();
    private static final UUID BAGGAGE_TYPE_ID = UUID.randomUUID();
    private static final BaggageType BAGGAGE_TYPE = new BaggageType(BAGGAGE_TYPE_ID, "Standard", null);
    private static final Baggage BAGGAGE = new Baggage(BAGGAGE_ID, BAGGAGE_TYPE);
    private static final BaggageDto BAGGAGE_DTO = new BaggageDto(BAGGAGE_TYPE_ID);

    @Test
    void getAllBaggages_returnsAllBaggages() {
        // arrange
        when(baggageRepository.findAll()).thenReturn(List.of(BAGGAGE));

        // act
        List<Baggage> result = baggageService.getAllBaggages();

        // assert
        assertThat(result).containsExactly(BAGGAGE);
    }

    @Test
    void getBaggagesByType_returnsBaggages() {
        // arrange
        when(baggageRepository.findByBaggageTypeId(BAGGAGE_TYPE_ID)).thenReturn(List.of(BAGGAGE));

        // act
        List<Baggage> result = baggageService.getBaggagesByType(BAGGAGE_TYPE_ID);

        // assert
        assertThat(result).containsExactly(BAGGAGE);
    }

    @Test
    void getBaggagesByType_throwsExceptionWhenEmpty() {
        // arrange
        when(baggageRepository.findByBaggageTypeId(BAGGAGE_TYPE_ID)).thenReturn(List.of());

        // act & assert
        assertThatThrownBy(() -> baggageService.getBaggagesByType(BAGGAGE_TYPE_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No baggages found for baggage type ID " + BAGGAGE_TYPE_ID);
    }

    @Test
    void getBaggagesByType_throwsExceptionWhenNull() {
        // act & assert
        assertThatThrownBy(() -> baggageService.getBaggagesByType(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("baggageTypeId must not be null.");
    }

    @Test
    void getBaggageById_returnsBaggage() {
        // arrange
        when(baggageRepository.findById(BAGGAGE_ID)).thenReturn(Optional.of(BAGGAGE));

        // act
        Baggage result = baggageService.getBaggageById(BAGGAGE_ID);

        // assert
        assertThat(result).isEqualTo(BAGGAGE);
    }

    @Test
    void getBaggageById_throwsExceptionWhenNotFound() {
        // arrange
        when(baggageRepository.findById(BAGGAGE_ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> baggageService.getBaggageById(BAGGAGE_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Baggage with ID " + BAGGAGE_ID + " not found");
    }

    @Test
    void deleteBaggage_deletesBaggage() {
        // arrange
        when(baggageRepository.existsById(BAGGAGE_ID)).thenReturn(true);

        // act
        baggageService.deleteBaggage(BAGGAGE_ID);

        // assert
        verify(baggageRepository, times(1)).deleteById(BAGGAGE_ID);
    }

    @Test
    void deleteBaggage_throwsExceptionWhenNotFound() {
        // arrange
        when(baggageRepository.existsById(BAGGAGE_ID)).thenReturn(false);

        // act & assert
        assertThatThrownBy(() -> baggageService.deleteBaggage(BAGGAGE_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Baggage with ID " + BAGGAGE_ID + " not found.");
    }

    @Test
    void addBaggage_savesBaggageAndReturnsId() {
        // arrange
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.of(BAGGAGE_TYPE));
        when(baggageRepository.save(any(Baggage.class))).thenReturn(BAGGAGE);

        // act
        UUID result = baggageService.addBaggage(BAGGAGE_DTO);

        // assert
        assertThat(result).isEqualTo(BAGGAGE_ID);
        verify(baggageRepository, times(1)).save(any(Baggage.class));
    }

    @Test
    void addBaggage_throwsExceptionWhenBaggageTypeNotFound() {
        // arrange
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> baggageService.addBaggage(BAGGAGE_DTO))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Baggage type with ID " + BAGGAGE_TYPE_ID + " not found");
    }

    @Test
    void updateBaggage_updatesBaggage() {
        // arrange
        when(baggageRepository.findById(BAGGAGE_ID)).thenReturn(Optional.of(BAGGAGE));
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.of(BAGGAGE_TYPE));

        // act
        baggageService.updateBaggage(BAGGAGE_ID, BAGGAGE_DTO);

        // assert
        verify(baggageRepository, times(1)).save(any(Baggage.class));
    }

    @Test
    void updateBaggage_throwsExceptionWhenBaggageNotFound() {
        // arrange
        when(baggageRepository.findById(BAGGAGE_ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> baggageService.updateBaggage(BAGGAGE_ID, BAGGAGE_DTO))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Baggage with ID " + BAGGAGE_ID + " not found");
    }

    @Test
    void updateBaggage_throwsExceptionWhenBaggageTypeNotFound() {
        // arrange
        when(baggageRepository.findById(BAGGAGE_ID)).thenReturn(Optional.of(BAGGAGE));
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> baggageService.updateBaggage(BAGGAGE_ID, BAGGAGE_DTO))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Baggage type with ID " + BAGGAGE_TYPE_ID + " not found");
    }

    @Test
    void getBaggages_withId_returnsBaggageById() {
        // arrange
        when(baggageRepository.findById(BAGGAGE_ID)).thenReturn(Optional.of(BAGGAGE));

        // act
        Object result = baggageService.getBaggages(BAGGAGE_ID, null);

        // assert
        assertThat(result).isEqualTo(BAGGAGE);
    }

    @Test
    void getBaggages_withBaggageTypeId_returnsBaggagesByType() {
        // arrange
        when(baggageRepository.findByBaggageTypeId(BAGGAGE_TYPE_ID)).thenReturn(List.of(BAGGAGE));

        // act
        Object result = baggageService.getBaggages(null, BAGGAGE_TYPE_ID);

        // assert
        assertThat(result).isEqualTo(List.of(BAGGAGE));
    }

    @Test
    void getBaggages_withoutIdOrBaggageTypeId_returnsAllBaggages() {
        // arrange
        when(baggageRepository.findAll()).thenReturn(List.of(BAGGAGE));

        // act
        Object result = baggageService.getBaggages(null, null);

        // assert
        assertThat(result).isEqualTo(List.of(BAGGAGE));
    }
}