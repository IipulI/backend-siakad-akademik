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
@Table(name = "siak_mata_kuliah")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MataKuliah {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "siak_program_studi_id", nullable = false)
    private ProgramStudi siakProgramStudi;

    @ManyToOne
    @JoinColumn(name = "siak_tahun_kurikulum_id", nullable = false)
    private TahunKurikulum siakTahunKurikulum;

    private Integer semester;
    private String kodeMataKuliah;
    private String namaMataKuliah;
    private String jenisMataKuliah;
    private Integer sks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prasyarat_mata_kuliah_1", nullable = true)
    private MataKuliah prasyaratMataKuliah1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prasyarat_mata_kuliah_2", nullable = true)
    private MataKuliah prasyaratMataKuliah2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prasyarat_mata_kuliah_3", nullable = true)
    private MataKuliah prasyaratMataKuliah3;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
