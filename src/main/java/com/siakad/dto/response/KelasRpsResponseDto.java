package com.siakad.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class KelasRpsResponseDto {
    private UUID rpsId;
    private List<KelasKuliahDto> kelasKuliah;
}
