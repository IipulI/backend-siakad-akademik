package com.siakad.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "siak_krs_mahasiswa")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KrsMahasiswa {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "siak_mahasiswa_id", nullable = false)
    private Mahasiswa siakMahasiswa;

    @ManyToOne
    @JoinColumn(name = "siak_periode_akademik_id", nullable = false)
    private PeriodeAkademik siakPeriodeAkademik;

    private String status;
    private Integer jumlahSksDiambil;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
