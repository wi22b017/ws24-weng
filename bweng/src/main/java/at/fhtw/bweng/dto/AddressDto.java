package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AddressDto(
        @NotBlank(message = "Street name cannot be blank")
        String street,
        @Positive int number,
        @Positive int zip,
        @NotBlank(message = "city cannot be blank")
        @NotBlank String city,
        @NotBlank(message = "country cannot be blank")
        @NotBlank String country
) {
}
