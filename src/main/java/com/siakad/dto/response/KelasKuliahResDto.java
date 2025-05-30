package com.siakad.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class KelasKuliahResDto {
    private UUID id;
    private String nama;
    private Integer kapasitas;
    private String sistemKuliah;
    private Integer jumlahPertemuan;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
    private String periodeAkademik;
    private MataKuliahDto mataKuliah;
    private ProgramStudiResDto programStudi;
    private List<String> dosen;
    private List<String> jadwalMingguan;
    private Integer peserta;
    private String statusPenilaian;
}
