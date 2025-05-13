package com.siakad.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "siak_capaian_pembelajaran_lulusan")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CapaianPembelajaranLulusan {

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

    private String kodeCpl;
    private String kategoriCpl;
    @Column(columnDefinition = "TEXT")
    private String deskripsiCpl;

    @ManyToMany
    @JoinTable(
            name = "siak_pemetaan_pl_cpl",
            joinColumns = @JoinColumn(name = "siak_capaian_pembelajaran_id"),
            inverseJoinColumns = @JoinColumn(name = "siak_profil_lulusan_id")
    )
    private List<ProfilLulusan> profilLulusanList;


    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
