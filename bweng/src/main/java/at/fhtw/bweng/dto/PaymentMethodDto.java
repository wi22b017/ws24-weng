package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;

public record PaymentMethodDto(
        @NotBlank String name
) {
}
