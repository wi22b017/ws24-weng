package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AircraftDto(@NotBlank String manufacturer,
                          @NotBlank String model,
                          @Positive int capacity,
                          @NotNull AirlineDto airline,
                          @NotBlank String serialNumber) {}
