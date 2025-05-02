package com.siakad.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
public class TahunAjaranResDto {
    private UUID id;
    private String tahun;
    private String nama;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
}
