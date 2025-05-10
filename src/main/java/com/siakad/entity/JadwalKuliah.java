package com.siakad.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "siak_jadwal_kuliah ")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JadwalKuliah {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "siak_dosen_id", nullable = false)
    private Dosen siakDosen;

    @ManyToOne
    @JoinColumn(name = "siak_kelas_kuliah_id", nullable = false)
    private KelasKuliah siakKelasKuliah;

    @ManyToOne
    @JoinColumn(name = "siak_ruangan_id", nullable = false)
    private Ruangan siakRuangan;

    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private String hari;
    private String jenisPertemuan;
    private String metodePembelajaran;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
