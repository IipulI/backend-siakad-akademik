package com.siakad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a single schedule item in a student's weekly schedule.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JadwalMingguanResDto {
    private String namaMataKuliah;
    private String kodeMataKuliah;
    private String jamMulai;
    private String jamSelesai;
    private String kelas;
    private String ruangan;
    private String dosen;
}