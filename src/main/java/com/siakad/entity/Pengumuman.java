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
@Table(name = "siak_pengumuman")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pengumuman {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "siak_user_id", nullable = false)
    private User siakUser;

    private String judul;

    @Column(columnDefinition = "TEXT")
    private String isi;

    private Boolean isActive;

    private Boolean isPriority;

    @Column(columnDefinition = "bytea")
    private byte[] banner;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
