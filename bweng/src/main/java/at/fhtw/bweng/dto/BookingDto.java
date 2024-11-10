package at.fhtw.bweng.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record BookingDto(
        @NotBlank String status,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal price,
        @NotBlank LocalDateTime bookingDate,
        @NotBlank UUID paymentMethodId,
        @NotBlank UUID flightId
        ) {
}
