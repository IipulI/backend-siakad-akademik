package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class KrsResDto {
    private MataKuliahResDto mataKuliah;
    private UUID id;
    private String namaKelas;
    private String hari;
    private String jamMulai;
    private String jamSelesai;
    private String dosenPengajar;
    private String riwayatMatakuliah;
}
