package com.siakad.dto.response;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class MahasiswaResDto {
    private UUID id;
    private String namaProgramStudi;
    private String nama;
    private String npm;
    private String periodeMasuk;
    private String kurikulum;
    private String sistemKuliah;
    private String kelas;
    private String jenisPendaftaran;
    private String jalurPendaftaran;
    private String gelombang;
    private String jenisKelamin;
    private String tempatLahir;
    private LocalDate tanggalLahir;
    private String noKk;
    private String nik;
    private String angkatan;
    private LocalDate tanggalMasuk;
    private Boolean kebutuhanKhusus;
    private String statusMahasiswa;
    private String alamatKtp;
    private Integer rtKtp;
    private Integer rwKtp;
    private Integer semester;
    private String desaKtp;
    private String provinsiKtp;
    private String kodePosKtp;
    private String statusTinggalKtp;
    private String alamatDomisili;
    private Integer rtDomisili;
    private Integer rwDomisili;
    private String desaDomisili;
    private String provinsiDomisili;
    private String kodePosDomisili;
    private String statusTinggalDomisili;
    private String noTelepon;
    private String noHp;
    @Email
    private String emailPribadi;

    @Email
    private String emailKampus;
    private String noTerdaftar;
    private String pendidikanAsal;
    private String provinsiSekolah;
    private String kotaKabSekolah;
    private String namaPendidikanAsal;
    private String alamatSekolah;
    private String teleponSekolah;
    private String noIjazahSekolah;
    private String jenjang;
    private String dusunRt;
    private String kotaRt;
    private String kecamatanRt;
    private String dusunDomisili;
    private String kotaDomisili;
    private String kecamatanDomisili;
    private String agama;
    private String beratBadan;
    private String tinggiBadan;
    private String golonganDarah;
    private String transportasi;
    private String kewarganegaraan;
    private String paspor;
    private String statusNikah;
    private String ukuranJasAlmamater;
    private String pekerjaan;
    private String instansiPekerjaan;
    private String penghasilan;
    private String noRekening;
    private String namaRekening;
    private String namaBank;
    private String nisn;
    private Integer sks;
    private Double ipk;
    private List<KeluargaMahasiswaResDto> keluargaMahasiswaList;
}
