package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AddressDto(
        @NotBlank(message = "Street name cannot be blank")
        String street,
        @Positive(message = "Street number must be a positive number")
        int number,
        @Positive(message = "ZIP code must be a positive number")
        int zip,
        @NotBlank(message = "city cannot be blank")
        String city,
        @NotBlank(message = "country cannot be blank")
        String country
) {
}
