package com.siakad.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class GetJadwalReqDto {
    private UUID siakPeriodeAkademikId;
    private LocalDate tanggal;
}