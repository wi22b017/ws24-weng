package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;

public record AirportDto(@NotBlank String airportCode, @NotBlank String airportText) {
}
