package com.siakad.dto.request;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class JadwalKuliahReqDto {
    private UUID siakRuanganId;
    private String hari;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private String jenisPertemuan;
    private String metodePembelajaran;
}
