package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;

public record AirlineDto(@NotBlank String name) {
}
