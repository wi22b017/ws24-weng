package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;

public record AirportDto(
        @NotBlank(message = "Airport code cannot be blank")
        String airportCode,
        @NotBlank(message = "Airport name cannot be blank")
        String airportText) {
}
