package com.siakad.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class JadwalUjianResDto {
    private UUID id;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private LocalDate tanggal;
    private String jenisUjian;
    private RuanganResDto siakRuangan;
}
