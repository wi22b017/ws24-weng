package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.PaymentMethodDto;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.repository.PaymentMethodRepository;
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
class PaymentMethodServiceTest {

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @InjectMocks
    private PaymentMethodService paymentMethodService;

    private static final UUID PAYMENT_METHOD_ID = UUID.randomUUID();
    private static final PaymentMethod PAYMENT_METHOD = new PaymentMethod(PAYMENT_METHOD_ID, "Credit Card");
    private static final PaymentMethodDto PAYMENT_METHOD_DTO = new PaymentMethodDto("Credit Card");

    @Test
    void addPaymentMethod_savesPaymentMethodAndReturnsId() {
        // arrange
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(PAYMENT_METHOD);

        // act
        UUID result = paymentMethodService.addPaymentMethod(PAYMENT_METHOD_DTO);

        // assert
        assertThat(result).isEqualTo(PAYMENT_METHOD_ID);
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    void addPaymentMethod_throwsExceptionWhenDuplicate() {
        // arrange
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenThrow(new DataIntegrityViolationException("Payment method with the same data already exists."));

        // act & assert
        assertThatThrownBy(() -> paymentMethodService.addPaymentMethod(PAYMENT_METHOD_DTO))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Payment method with the same data already exists.");
    }

    @Test
    void getPaymentMethods_withId_returnsPaymentMethodById() {
        // arrange
        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(PAYMENT_METHOD));

        // act
        Object result = paymentMethodService.getPaymentMethods(PAYMENT_METHOD_ID);

        // assert
        assertThat(result).isEqualTo(PAYMENT_METHOD);
    }

    @Test
    void getPaymentMethods_withoutId_returnsAllPaymentMethods() {
        // arrange
        when(paymentMethodRepository.findAll()).thenReturn(List.of(PAYMENT_METHOD));

        // act
        Object result = paymentMethodService.getPaymentMethods(null);

        // assert
        assertThat(result).isEqualTo(List.of(PAYMENT_METHOD));
    }

    @Test
    void getPaymentMethodById_throwsExceptionWhenNotFound() {
        // arrange
        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> paymentMethodService.getPaymentMethodById(PAYMENT_METHOD_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Payment method with id " + PAYMENT_METHOD_ID + " not found.");
    }

    @Test
    void updatePaymentMethod_updatesPaymentMethod() {
        // arrange
        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(PAYMENT_METHOD));

        // act
        paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, PAYMENT_METHOD_DTO);

        // assert
        verify(paymentMethodRepository, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    void updatePaymentMethod_throwsExceptionWhenDuplicateName() {
        // arrange
        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(PAYMENT_METHOD));
        doThrow(new DataIntegrityViolationException("Payment method with the same name already exists."))
                .when(paymentMethodRepository).save(any(PaymentMethod.class));

        // act & assert
        assertThatThrownBy(() -> paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, PAYMENT_METHOD_DTO))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Payment method with the same name already exists.");
    }

    @Test
    void deletePaymentMethod_deletesPaymentMethod() {
        // arrange
        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(PAYMENT_METHOD));

        // act
        paymentMethodService.deletePaymentMethod(PAYMENT_METHOD_ID);

        // assert
        verify(paymentMethodRepository, times(1)).delete(PAYMENT_METHOD);
    }

    @Test
    void deletePaymentMethod_throwsExceptionWhenNotFound() {
        // arrange
        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> paymentMethodService.deletePaymentMethod(PAYMENT_METHOD_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Payment method with id " + PAYMENT_METHOD_ID + " not found.");
    }
}