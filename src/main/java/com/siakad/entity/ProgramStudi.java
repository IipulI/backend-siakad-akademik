    package com.siakad.entity;

    import jakarta.persistence.*;
    import lombok.*;
    import org.hibernate.annotations.CreationTimestamp;
    import org.hibernate.annotations.UpdateTimestamp;

    import java.time.LocalDateTime;
    import java.util.UUID;

    @Entity
    @Table(name = "siak_program_studi")
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class ProgramStudi {

        @Id
        @GeneratedValue
        @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
        private UUID id;

        @ManyToOne
        @JoinColumn(name = "siak_fakultas_id", nullable = false)
        private Fakultas siakFakultas;

        @ManyToOne
        @JoinColumn(name = "siak_jenjang_id", nullable = false)
        private Jenjang siakJenjang;

        @Column(name = "kode_program_studi")
        private String kodeProgramStudi;

        @Column(name = "nama_program_studi", nullable = false)
        private String namaProgramStudi;

        @Column(name = "is_deleted", nullable = false)
        private Boolean isDeleted = false;

        @CreationTimestamp
        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;
    }
