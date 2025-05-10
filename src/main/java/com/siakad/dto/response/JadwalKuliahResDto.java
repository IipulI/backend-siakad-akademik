package com.siakad.dto.response;

import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class JadwalKuliahResDto {
    private UUID id;
    private String hari;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private String jenisPertemuan;
    private String metodePembelajaran;
    private String namaRuangan;
}
