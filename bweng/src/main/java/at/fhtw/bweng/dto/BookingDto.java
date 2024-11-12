package at.fhtw.bweng.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

public record BookingDto(
        @NotBlank(message = "Booking status cannot be blank")
        String status,
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price,
        @NotNull(message = "Booking date cannot be null")
        LocalDateTime bookingDate,
        @NotNull(message = "User ID cannot be null")
        UUID userId,
        @NotNull(message = "Payment method ID cannot be null")
        UUID paymentMethodId,
        @NotNull(message = "Flight ID cannot be null")
        UUID flightId,
        @NotNull(message = "Passenger list cannot be null")
        @Size(min = 1, message = "At least one passenger is required")
        @Valid
        List<PassengerDto> passengers
        ) {
}
