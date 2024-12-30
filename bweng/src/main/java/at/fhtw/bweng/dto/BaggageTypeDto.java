package at.fhtw.bweng.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BaggageTypeDto(
        @NotBlank(message = "Baggage type name cannot be blank")
        String name,
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false, message = "Fee must be greater than 0")
        BigDecimal fee
) {
}
