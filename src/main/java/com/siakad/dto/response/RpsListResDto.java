package com.siakad.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RpsListResDto {
    private UUID id;
    private MataKuliahRpsResDto mataKuliah;
    private ProgramStudiResDto programStudi;
    private String namaKelas;
    private List<String> pengajar;
    private List<String> jadwalMingguan;
    private Integer kapasitas;
    private Integer peserta;
    private String statusPenilaian;
}
