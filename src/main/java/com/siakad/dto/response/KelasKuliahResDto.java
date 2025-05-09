package com.siakad.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class KelasKuliahResDto {
    private String mataKuliah;
    private String programStudi;
    private String periodeAkademik;
    private String nama;
    private Integer kapasitas;
    private String sistemKuliah;
    private Integer jumlahPertemuan;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
}
