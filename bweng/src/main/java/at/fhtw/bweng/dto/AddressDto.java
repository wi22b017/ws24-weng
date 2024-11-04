package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressDto(
        @NotBlank String street,
        @NotBlank int number,
        @NotBlank int zip,
        @NotBlank String city,
        @NotBlank String country
) {
}
