package at.fhtw.bweng.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookDto(@NotBlank String title, @NotNull AuthorDto author, @Positive int releaseYear){}
