package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record PassengerDto(
        UUID id, // Only needed for updating a passenger, which is normally not made
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull LocalDate birthday,
        @NotBlank String seatNumber,
        @NotNull UUID baggageId
) {
}
