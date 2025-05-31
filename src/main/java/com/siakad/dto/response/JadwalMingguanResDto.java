package com.siakad.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JadwalMingguanResDto {
    private String namaMataKuliah;
    private String kodeMataKuliah;
    private String jamMulai;       // e.g., "08:00"
    private String jamSelesai;     // e.g., "09:40"
    private String kelas;          // e.g., "Reguler B" (from KelasKuliah.nama)
    private String ruangan;        // e.g., "FTS Lantai 3" (from Ruangan.namaRuangan)
    private String dosen;          // e.g., "Nama Dosen" (from Dosen.namaDosen - assuming Dosen entity)
}