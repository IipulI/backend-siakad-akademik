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

    private String nama;
    private String npm;
    private String angkatan;
    private String status;
    private String jenisKelamin;
    private String tempatLahir;
    private LocalDate tanggalLahir;
    @Email
    private String email;
    private String noTelepon;
    private String alamat;
    private String agama;
    private String statusNikah;
    private String nik;
    private String noKk;
    private String pendidikanAsal;
    private String namaPendidikanAsal;
    private String nisn;
    private String pekerjaan;
    private String instansiPekerjaan;
    private String penghasilan;

//    @Lob
//    @Column(name = "foto_profil", columnDefinition = "BYTEA")
//    private byte[] fotoProfil;
//
//    @Lob
//    @Column(name = "ijazah_sekolah", columnDefinition = "BYTEA")
//    private byte[] ijazahSekolah;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "siakMahasiswa", cascade = CascadeType.ALL)
    private List<KeluargaMahasiswa> keluarga;
}
