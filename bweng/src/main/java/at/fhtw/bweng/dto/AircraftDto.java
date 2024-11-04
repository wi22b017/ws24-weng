package at.fhtw.bweng.dto;

import jakarta.validation.constraints.*;

public record AircraftDto(@NotBlank String manufacturer,
                          @NotBlank String model,
                          @Positive int capacity,
                          @NotNull AirlineDto airline,
                          @NotBlank String serialNumber) {}
