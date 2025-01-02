package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;

public record PaymentMethodDto(
        @NotBlank(message = "Payment method name cannot be blank")
        String name,
        String id
) {

        // Overloaded constructor
        public PaymentMethodDto(String name) {
                this(name, null); // Default id to null
        }
}
