    package com.siakad.dto.request;

    import jakarta.validation.constraints.Email;
    import lombok.Data;

    import java.time.LocalDate;
    import java.util.UUID;

    @Data
    public class MahasiswaReqDto {
        private UUID siakProgramStudiId;
        private String nama;
        private String npm;
        private String periodeMasuk;
        private String sistemKuliah;
        private String kelas;
        private String jenisPendaftaran;
        private String jalurPendaftaran;
        private String gelombang;
        private String angkatan;
        private String jenisKelamin;
        private String tempatLahir;
        private LocalDate tanggalLahir;
        private String noKk;
        private String nik;
        private LocalDate tanggalMasuk;
        private Boolean kebutuhanKhusus;
        private String statusMahasiswa;
        private String alamatKtp;
        private Integer rtKtp;
        private Integer rwKtp;
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
        private Integer semester;
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
    }
