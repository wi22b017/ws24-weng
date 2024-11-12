package at.fhtw.bweng.dto;


import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BaggageDto(
        @NotNull(message = "Baggage type ID cannot be null")
        UUID baggageTypeId
) {

}
