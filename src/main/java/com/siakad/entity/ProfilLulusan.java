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
@Table(name = "siak_profil_lulusan")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfilLulusan {

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

    private String kodePl;
    private String profil;
    private String profesi;

    @Column(columnDefinition = "TEXT")
    private String deskripsiPl;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
