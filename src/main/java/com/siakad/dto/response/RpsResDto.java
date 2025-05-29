package com.siakad.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class RpsResDto {
    private UUID id;
    private TahunKurikulumDto tahunKurikulum;
    private PeriodeAkademikDto periodeAkademik;
    private ProgramStudiDto programStudi;
    private LocalDate tanggalPenyusun;
    private String deskripsiMataKuliah;
    private String tujuanMataKuliah;
    private String materiPembelajaran;
    private String pustakaUtama;
    private String pustakaPendukung;
    private List<RpsDosenResDto> dosenPenyusun;
    private MataKuliahRpsResDto mataKuliah;
    private List<KelasKuliahDto> kelas;
}
