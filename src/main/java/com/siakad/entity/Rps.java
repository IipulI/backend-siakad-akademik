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
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "siak_rps")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rps {
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

    @ManyToOne
    @JoinColumn(name = "siak_mata_kuliah_id", nullable = false)
    private MataKuliah siakMataKuliah;

    @ManyToOne
    @JoinColumn(name = "siak_periode_akademik_id", nullable = false)
    private PeriodeAkademik siakPeriodeAkademik;

    private LocalDate tanggalPenyusun;

    @Column(columnDefinition = "TEXT")
    private String deskripsiMataKuliah;

    @Column(columnDefinition = "TEXT")
    private String tujuanMataKuliah;

    @Column(columnDefinition = "TEXT")
    private String materiPembelajaran;

    @Column(columnDefinition = "TEXT")
    private String pustakaUtama;

    @Column(columnDefinition = "TEXT")
    private String pustakaPendukung;

    @Column(columnDefinition = "bytea")
    private byte[] dokumenRps;

    @ManyToMany
    @JoinTable(
            name = "siak_team_penyusun_rps",
            joinColumns = @JoinColumn(name = "siak_rps_id"),
            inverseJoinColumns = @JoinColumn(name = "siak_dosen_id")
    )
    private List<Dosen> dosenList;

    @ManyToMany
    @JoinTable(
            name = "siak_kelas_rps",
            joinColumns = @JoinColumn(name = "siak_rps_id"),
            inverseJoinColumns = @JoinColumn(name = "siak_kelas_kuliah_id")
    )
    private List<KelasKuliah> kelasKuliahList;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
