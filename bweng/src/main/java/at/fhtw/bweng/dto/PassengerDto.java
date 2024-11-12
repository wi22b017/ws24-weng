package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;

public record PassengerDto(
        UUID id, // Only needed for updating a passenger, which is normally not made
        @NotBlank(message = "First name cannot be blank")
        String firstName,
        @NotBlank(message = "Last name cannot be blank")
        String lastName,
        @NotNull(message = "Birthday cannot be null")
        LocalDate birthday,
        String seatNumber,
        @NotNull(message = "Baggage information cannot be null")
        @Valid
        BaggageDto baggage
) {
}
