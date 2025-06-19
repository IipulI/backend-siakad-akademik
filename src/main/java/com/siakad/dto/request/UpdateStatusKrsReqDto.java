package com.siakad.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UpdateStatusKrsReqDto {
    @NotEmpty(message = "List of mahasiswaIds cannot be empty.")
    private List<UUID> mahasiswaIds;

    @NotNull(message = "periodeAkademikId cannot be null.")
    private UUID periodeAkademikId;
}
