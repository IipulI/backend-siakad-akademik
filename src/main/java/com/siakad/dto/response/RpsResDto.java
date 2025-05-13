package com.siakad.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class RpsResDto {
    private UUID id;
    private String tahunKurikulum;
    private String periodeAkademik;
    private String programStudi;
    private LocalDate tanggalPenyusun;
    private String deskripsiMataKuliah;
    private String tujuanMataKuliah;
    private String materiPembelajaran;
    private String pustakaUtama;
    private String pustakaPendukung;
    private List<RpsDosenResDto> dosenPenyusun;
    private List<MataKuliahResDto> mataKuliah;
    private List<KelasKuliahResDto> kelas;
}
