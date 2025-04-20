package com.siakad.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "siak_keluarga_mahasiswa")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KeluargaMahasiswa {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "siak_mahasiswa_id", nullable = false)
    private Mahasiswa siakMahasiswa;

    private String hubungan;
    private String nama;
    private String nik;
    private LocalDate tanggalLahir;
    private String statusHidup;
    private String statusKerabat;
    private String pendidikan;
    private String pekerjaan;
    private String penghasilan;
    private String alamat;
    private String noTelepon;
    private String email;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
