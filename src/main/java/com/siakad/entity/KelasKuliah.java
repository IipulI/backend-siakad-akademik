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
@Table(name = "siak_kelas_kuliah")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KelasKuliah {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "siak_mata_kuliah_id", nullable = false)
    private MataKuliah siakMataKuliah;

    @ManyToOne
    @JoinColumn(name = "siak_program_studi_id", nullable = false)
    private ProgramStudi siakProgramStudi;

    @ManyToOne
    @JoinColumn(name = "siak_periode_akademik_id", nullable = false)
    private PeriodeAkademik siakPeriodeAkademik;

    @OneToMany(mappedBy = "siakKelasKuliah", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JadwalKuliah> siakJadwalKuliah;

    private String nama;
    private Integer kapasitas;
    private String sistemKuliah;
    private String statusKelas;
    private Integer jumlahPertemuan;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
