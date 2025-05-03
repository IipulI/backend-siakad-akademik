package com.siakad.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class TahunKurikulumResDto {
    private UUID id;
    private String mulaiBerlaku;
    private String tahun;
    private String keterangan;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
}
