package com.siakad.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class JadwalDto {
    private UUID id;
    private String hari;
    private String jamMulai;
    private String jamSelesai;
    private String jenisPertemuan;
    private String metodePembelajaran;
    private RuanganResDto siakRuangan;
    private DosenDto siakDosen;
}
