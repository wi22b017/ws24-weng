package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;

public record UserDto(
        @NotBlank String gender,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String email,
        @NotBlank String role,
        @NotBlank String status,
        @Valid @NotNull AddressDto address,
        @NotNull PaymentMethodDto paymentMethod
) {
}
