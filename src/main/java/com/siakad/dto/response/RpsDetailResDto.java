package com.siakad.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class RpsDetailResDto {
    private UUID id;
    private TahunKurikulumDto tahunKurikulum;
    private PeriodeAkademikDto periodeAkademik;
    private ProgramStudiResDto programStudi; // Changed to ProgramStudiResDto
    private LocalDate tanggalPenyusun;
    private String deskripsiMataKuliah;
    private String tujuanMataKuliah;
    private String materiPembelajaran;
    private String pustakaUtama;
    private String pustakaPendukung;

    // Updated constructor to include Jenjang fields, passed to ProgramStudiResDto
    public RpsDetailResDto(
            UUID rpsId,
            UUID tkId, String tkTahun,
            UUID paId, String paNamaPeriode, String paKodePeriode,
            UUID psId, String psNamaProgramStudi,
            UUID jenjangId, String jenjangNama, String jenjangJenjang, // Jenjang fields
            LocalDate tanggalPenyusun, String deskripsiMataKuliah,
            String tujuanMataKuliah, String materiPembelajaran,
            String pustakaUtama, String pustakaPendukung
    ) {
        this.id = rpsId;
        this.tahunKurikulum = new TahunKurikulumDto(tkId, tkTahun);
        this.periodeAkademik = new PeriodeAkademikDto(paId, paNamaPeriode, paKodePeriode);
        this.programStudi = new ProgramStudiResDto(psId, psNamaProgramStudi, jenjangId, jenjangNama, jenjangJenjang);
        this.tanggalPenyusun = tanggalPenyusun;
        this.deskripsiMataKuliah = deskripsiMataKuliah;
        this.tujuanMataKuliah = tujuanMataKuliah;
        this.materiPembelajaran = materiPembelajaran;
        this.pustakaUtama = pustakaUtama;
        this.pustakaPendukung = pustakaPendukung;
    }

    public RpsDetailResDto() {} // Default constructor
}