package com.siakad.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "siak_mahasiswa")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mahasiswa {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "siak_user_id", nullable = false, unique = true)
    private User siakUser;

    @ManyToOne
    @JoinColumn(name = "siak_program_studi_id", nullable = false)
    private ProgramStudi siakProgramStudi;

    private String nama;
    private String npm;
    private String periodeMasuk;
    private String sistemKuliah;
    private String kelas;
    private String jenisPendaftaran;
    private String jalurPendaftaran;
    private String gelombang;
    private String jenisKelamin;
    private String tempatLahir;
    private String nik;
    private String noKk;
    private String angkatan;
    private LocalDate tanggalLahir;
    private LocalDate tanggalMasuk;
    private Boolean kebutuhanKhusus;
    private String statusMahasiswa;
    private String alamatKtp;
    private Integer rtKtp;
    private Integer rwKtp;
    private String desaKtp;
    private String noTerdaftar;
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
    private Integer semester;

    @Email
    private String emailPribadi;

    @Email
    private String emailKampus;
    private String pendidikanAsal;
    private String provinsiSekolah;
    private String kotaKabSekolah;
    private String namaPendidikanAsal;
    private String alamatSekolah;
    private String teleponSekolah;
    private String noIjazahSekolah;

    @Column(columnDefinition = "bytea")
    private byte[] ijazahSekolah;

    @Column(columnDefinition = "bytea")
    private byte[] fotoProfil;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "siakMahasiswa", cascade = CascadeType.ALL)
    private List<KeluargaMahasiswa> keluarga;

    @OneToMany(mappedBy = "siakMahasiswa", cascade = CascadeType.ALL)
    private List<InvoiceMahasiswa> invoiceMahasiswa;

    @OneToMany(mappedBy = "siakMahasiswa", cascade = CascadeType.ALL)
    private List<HasilStudi> hasilStudi;

    @OneToMany(mappedBy = "siakMahasiswa", cascade = CascadeType.ALL)
    private List<KrsMahasiswa> krsMahasiswa;
}
