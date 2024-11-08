package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.UUID;

public record BaggageDto(
        @NotBlank String type,
        @NotBlank BigDecimal fee,
        @NotBlank Float weight,
        @NotBlank Float length,
        @NotBlank Float width,
        @NotBlank Float height,
        @NotBlank UUID bookingId
        ) {

}
