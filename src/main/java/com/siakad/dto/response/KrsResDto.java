package com.siakad.dto.response;

import lombok.Data;

@Data
public class KrsResDto {
    private MataKuliahResDto mataKuliah;
    private String namaKelas;
    private String hari;
    private String jamMulai;
    private String jamSelesai;
    private String dosenPengajar;
    private String riwayatMatakuliah;
}
