package at.fhtw.bweng.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;

import java.math.BigDecimal;

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
        AircraftDto aircraft,
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price
) {
}
