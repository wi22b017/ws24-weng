package at.fhtw.bweng.dto;


import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BaggageDto(
        @NotNull UUID baggageTypeId
) {

}
