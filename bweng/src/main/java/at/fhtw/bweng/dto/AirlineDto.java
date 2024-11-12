package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;

public record AirlineDto(
        @NotBlank(message = "Airline name cannot be blank")
        String name) {
}
