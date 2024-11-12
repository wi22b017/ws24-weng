package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.Valid;

public record AircraftDto(
        @NotBlank(message = "Manufacturer cannot be blank")
        String manufacturer,
        @NotBlank(message = "Model cannot be blank")
        String model,
        @Positive(message = "Capacity must be a positive number")
        int capacity,
        @Valid
        @NotNull
        AirlineDto airline,
        @NotBlank(message = "Serial number cannot be blank")
        String serialNumber
) {
}
