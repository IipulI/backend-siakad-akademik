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
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "siak_periode_akademik")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PeriodeAkademik {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "siak_tahun_ajaran_id", nullable = false)
    private TahunAjaran siakTahunAjaran;

    private String namaPeriode;
    private String kodePeriode;
    private String jenis;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
