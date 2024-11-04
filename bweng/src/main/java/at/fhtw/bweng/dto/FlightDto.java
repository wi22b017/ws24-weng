package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FlightDto(@NotBlank String flightNumber,
                        @NotBlank String departureTime,
                        @NotBlank String arrivalTime,
                        @NotNull AirportDto flightOrigin,
                        @NotNull AirportDto flightDestination,
                        @NotNull AircraftDto aircraft) {}
