package com.siakad.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class JadwalUjianReqDto {
    private UUID siakDosenId;
    private UUID siakRuanganId;
    private LocalDate tanggalJadwal;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private String jenisUjian;
}
