package at.fhtw.bweng.dto;

import at.fhtw.bweng.model.Baggage;
import at.fhtw.bweng.model.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BookingDto(
        @NotBlank String status,
        @NotBlank BigDecimal price,
        @NotBlank String seatNumber,
        @NotBlank LocalDateTime bookingDate,
        @NotBlank UUID paymentMethodId,
        @NotBlank UUID flightId,
        @NotBlank List<BaggageDto> baggages
        ) {
}
