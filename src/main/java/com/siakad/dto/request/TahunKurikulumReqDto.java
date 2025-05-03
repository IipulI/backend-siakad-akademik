package com.siakad.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class TahunKurikulumReqDto {
    private UUID siakPeriodeAkademikId;
    private String tahun;
    private String keterangan;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
}
