package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;

public record FlightDto(
        @NotBlank(message = "Flight number code cannot be blank")
        String flightNumber,
        @NotBlank(message = "Departure time cannot be blank")
        String departureTime,
        @NotBlank(message = "Departure time cannot be blank")
        String arrivalTime,
        @Valid
        @NotNull
        AirportDto flightOrigin,
        @Valid
        @NotNull
        AirportDto flightDestination,
        @Valid
        @NotNull
        AircraftDto aircraft
) {
}
