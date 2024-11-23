package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequestDto(@NotBlank String usernameOrEmail, @NotBlank String password) {
}
