package com.siakad.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "siak_skala_penilaian")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkalaPenilaian {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "siak_tahun_ajaran_id")
    private TahunAjaran siakTahunAjaran;

    @ManyToOne
    @JoinColumn(name = "siak_program_studi_id")
    private ProgramStudi siakProgramStudi;

    private String angkaMutu;
    private BigDecimal nilaiMutu;
    private BigDecimal nilaiMin;
    private BigDecimal nilaiMax;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
