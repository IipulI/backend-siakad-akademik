package com.siakad.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class RpsReqDto {
    private UUID siakProgramStudiId;
    private UUID siakPeriodeAkademikId;
    private UUID siakTahunKurikulumId;
    private UUID siakMataKuliahId;
    private LocalDate tanggalPenyusun;
    private String deskripsiMataKuliah;
    private String tujuanMataKuliah;
    private String materiPembelajaran;
    private String pustakaUtama;
    private String pustakaPendukung;
    private List<UUID> dosenIds;
}
