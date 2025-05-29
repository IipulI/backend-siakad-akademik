package com.siakad.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "siak_komposisi_nilai_mata_kuliah")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KomposisiNilaiMataKuliah {

    @EmbeddedId
    private KomposisiNilaiMataKuliahId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("siakMataKuliahId")
    @JoinColumn(name = "siak_mata_kuliah_id")
    private MataKuliah siakMataKuliah;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("siakKomposisiNilaiId")
    @JoinColumn(name = "siak_komposisi_nilai_id")
    private KomposisiPenilaian siakKomposisiNilai;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

