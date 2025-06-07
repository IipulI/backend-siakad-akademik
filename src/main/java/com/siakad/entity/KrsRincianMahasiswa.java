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
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "siak_rincian_krs_mahasiswa")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KrsRincianMahasiswa {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "siak_kelas_kuliah_id", nullable = false)
    private KelasKuliah siakKelasKuliah;

    @ManyToOne
    @JoinColumn(name = "siak_krs_mahasiswa_id", nullable = false)
    private KrsMahasiswa siakKrsMahasiswa;

    private String kategori;
    private String status;
    private BigDecimal kehadiran;
    private BigDecimal tugas;
    private BigDecimal uas;
    private BigDecimal uts;
    private BigDecimal nilai;
    private String hurufMutu;
    private BigDecimal angkaMutu;
    private BigDecimal nilaiAkhir;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
