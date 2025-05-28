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
@Table(name = "siak_pembimbing_akademik")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PembimbingAkademik {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "siak_dosen_id", nullable = false)
    private Dosen siakDosen;

    @ManyToOne
    @JoinColumn(name = "siak_periode_akademik_id", nullable = false)
    private PeriodeAkademik siakPeriodeAkademik;

    @ManyToOne
    @JoinColumn(name = "siak_mahasiswa_id", nullable = false)
    private Mahasiswa siakMahasiswa;

    private String noSk;
    private LocalDate tanggalSk;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
