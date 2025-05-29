package com.siakad.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class PembimbingAkademikReqDto {
    private UUID periodeAkademikId;
    private List<UUID> mahasiswaIds;
    private UUID dosenId;
    private String noSk;
    private LocalDate tanggalSk;
}
