package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthorDto (@NotBlank String firstName,@NotBlank String lastName) { }
