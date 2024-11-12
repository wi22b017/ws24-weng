package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public record UserDto(
        @NotBlank(message = "Gender cannot be blank")
        String gender,
        @NotBlank(message = "Firstname name cannot be blank")
        String firstName,
        @NotBlank(message = "Lastname name cannot be blank")
        String lastName,
        @NotBlank(message = "Username name cannot be blank")
        @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
        String username,
        @NotBlank(message = "Password name cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,
        @NotBlank(message = "Email name cannot be blank")
        @Email(message = "Email must be a valid email address")
        String email,
        @NotBlank(message = "Role name cannot be blank")
        String role,
        @NotBlank(message = "Status name cannot be blank")
        String status,
        @Valid
        @NotNull
        AddressDto address,
        @Valid
        @NotNull
        PaymentMethodDto paymentMethod
) {
}
