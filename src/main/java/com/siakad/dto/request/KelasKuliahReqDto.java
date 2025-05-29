package com.siakad.dto.request;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class KelasKuliahReqDto {
    private UUID siakMataKuliahId;
    private UUID siakProgramStudiId;
    private UUID siakPeriodeAkademikId;
    private String nama;
    private Integer kapasitas;
    private String sistemKuliah;
    private Integer jumlahPertemuan;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
    private List<JadwalKuliahReqDto> jadwalKuliah;
}
