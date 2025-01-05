package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.BaggageTypeDto;
import at.fhtw.bweng.model.BaggageType;
import at.fhtw.bweng.repository.BaggageTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaggageTypeServiceTest {

    @Mock
    private BaggageTypeRepository baggageTypeRepository;

    @InjectMocks
    private BaggageTypeService baggageTypeService;

    private static final UUID BAGGAGE_TYPE_ID = UUID.randomUUID();
    private static final BaggageType BAGGAGE_TYPE = new BaggageType(BAGGAGE_TYPE_ID, "Standard", BigDecimal.valueOf(25.0));
    private static final BaggageTypeDto BAGGAGE_TYPE_DTO = new BaggageTypeDto("Standard", BigDecimal.valueOf(25.0));

    @Test
    void addBaggageType_savesBaggageTypeAndReturnsId() {
        // arrange
        when(baggageTypeRepository.save(any(BaggageType.class))).thenReturn(BAGGAGE_TYPE);

        // act
        UUID result = baggageTypeService.addBaggageType(BAGGAGE_TYPE_DTO);

        // assert
        assertThat(result).isEqualTo(BAGGAGE_TYPE_ID);
    }

    @Test
    void addBaggageType_throwsExceptionWhenDuplicate() {
        // arrange
        when(baggageTypeRepository.save(any(BaggageType.class))).thenThrow(new DataIntegrityViolationException("Baggage Type with the same name already exists."));

        // act & assert
        assertThatThrownBy(() -> baggageTypeService.addBaggageType(BAGGAGE_TYPE_DTO))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Baggage Type with the same name already exists.");
    }

    @Test
    void getBaggageTypeById_returnsBaggageType() {
        // arrange
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.of(BAGGAGE_TYPE));

        // act
        BaggageType result = baggageTypeService.getBaggageTypeById(BAGGAGE_TYPE_ID);

        // assert
        assertThat(result).isEqualTo(BAGGAGE_TYPE);
    }

    @Test
    void getBaggageTypeById_throwsExceptionWhenNotFound() {
        // arrange
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> baggageTypeService.getBaggageTypeById(BAGGAGE_TYPE_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Baggage type with ID " + BAGGAGE_TYPE_ID + " not found");
    }

    @Test
    void getBaggageTypeByName_returnsBaggageType() {
        // arrange
        String name = "Standard";
        when(baggageTypeRepository.findByName(name)).thenReturn(Optional.of(BAGGAGE_TYPE));

        // act
        BaggageType result = baggageTypeService.getBaggageTypeByName(name);

        // assert
        assertThat(result).isEqualTo(BAGGAGE_TYPE);
    }

    @Test
    void getBaggageTypeByName_throwsExceptionWhenNotFound() {
        // arrange
        String name = "Nonexistent";
        when(baggageTypeRepository.findByName(name)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> baggageTypeService.getBaggageTypeByName(name))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Baggage type with name " + name + " not found");
    }

    @Test
    void getBaggageTypes_withId_returnsBaggageTypeById() {
        // arrange
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.of(BAGGAGE_TYPE));

        // act
        Object result = baggageTypeService.getBaggageTypes(BAGGAGE_TYPE_ID, null);

        // assert
        assertThat(result).isEqualTo(BAGGAGE_TYPE);
    }

    @Test
    void getBaggageTypes_withName_returnsBaggageTypeByName() {
        // arrange
        String name = "Standard";
        when(baggageTypeRepository.findByName(name)).thenReturn(Optional.of(BAGGAGE_TYPE));

        // act
        Object result = baggageTypeService.getBaggageTypes(null, name);

        // assert
        assertThat(result).isEqualTo(BAGGAGE_TYPE);
    }

    @Test
    void getBaggageTypes_withoutIdOrName_returnsAllBaggageTypes() {
        // arrange
        when(baggageTypeRepository.findAll()).thenReturn(List.of(BAGGAGE_TYPE));

        // act
        Object result = baggageTypeService.getBaggageTypes(null, null);

        // assert
        assertThat(result).isEqualTo(List.of(BAGGAGE_TYPE));
    }

    @Test
    void deleteBaggageType_deletesBaggageType() {
        // arrange
        when(baggageTypeRepository.existsById(BAGGAGE_TYPE_ID)).thenReturn(true);

        // act
        baggageTypeService.deleteBaggageType(BAGGAGE_TYPE_ID);

        // assert
        verify(baggageTypeRepository, times(1)).deleteById(BAGGAGE_TYPE_ID);
    }

    @Test
    void deleteBaggageType_throwsExceptionWhenNotFound() {
        // arrange
        when(baggageTypeRepository.existsById(BAGGAGE_TYPE_ID)).thenReturn(false);

        // act & assert
        assertThatThrownBy(() -> baggageTypeService.deleteBaggageType(BAGGAGE_TYPE_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Baggage type with ID " + BAGGAGE_TYPE_ID + " not found");
    }

    @Test
    void updateBaggageType_updatesBaggageType() {
        // arrange
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.of(BAGGAGE_TYPE));

        // act
        baggageTypeService.updateBaggageType(BAGGAGE_TYPE_ID, BAGGAGE_TYPE_DTO);

        // assert
        verify(baggageTypeRepository, times(1)).save(any(BaggageType.class));
    }

    @Test
    void updateBaggageType_throwsExceptionWhenNotFound() {
        // arrange
        when(baggageTypeRepository.findById(BAGGAGE_TYPE_ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> baggageTypeService.updateBaggageType(BAGGAGE_TYPE_ID, BAGGAGE_TYPE_DTO))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Baggage type with ID " + BAGGAGE_TYPE_ID + " not found");
    }
}