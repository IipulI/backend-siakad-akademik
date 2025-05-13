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
    @Table(name = "siak_capaian_mata_kuliah")
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class CapaianMataKuliah {

        @Id
        @GeneratedValue
        @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
        private UUID id;

        @ManyToOne
        @JoinColumn(name = "siak_mata_kuliah_id", nullable = false)
        private MataKuliah siakMataKuliah;

        private String kodeCpmk;
        @Column(columnDefinition = "TEXT")
        private String deskripsiCpmk;

        @ManyToMany
        @JoinTable(
                name = "siak_pemetaan_cpl_cpmk",
                joinColumns = @JoinColumn(name = "siak_capaian_mata_kuliah_id"),
                inverseJoinColumns = @JoinColumn(name = "siak_capaian_pembelajaran_id"))
        private List<CapaianPembelajaranLulusan> capaianPembelajaranLulusanList;

        @Column(name = "is_deleted", nullable = false)
        private Boolean isDeleted = false;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;
    }
