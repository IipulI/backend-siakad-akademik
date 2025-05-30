package com.siakad.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "siak_kelas_rps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KelasRps {

    @EmbeddedId
    private KelasRpsId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("siakKelasKuliahId")
    @JoinColumn(name = "siak_kelas_kuliah_id")
    private KelasKuliah siakKelasKuliah;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("siakRpsId")
    @JoinColumn(name = "siak_rps_id")
    private Rps siakRps;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
