package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record PassengerDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull LocalDate birthday,
        @NotBlank String seatNumber,
        @NotNull BaggageDto baggage
) {
    public record BaggageDto(
            @NotNull UUID baggageTypeId
    ) {}
}
