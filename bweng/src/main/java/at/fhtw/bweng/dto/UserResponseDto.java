package at.fhtw.bweng.dto;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String gender,
        String firstName,
        String lastName,
        String username,
        String email,
        String dateOfBirth,
        String role,
        String status,
        AddressDto address,
        PaymentMethodDto paymentMethod,
        String profilePictureUrl
) {
}
