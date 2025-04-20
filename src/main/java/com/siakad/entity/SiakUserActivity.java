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
@Table(name = "siak_user_activity")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiakUserActivity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siak_user_id", nullable = false)
    private User siakUser;

    @Column(columnDefinition = "TEXT")
    private String activity;

    @Column(name = "ip_address", length = 15)
    private String ipAddress;

    @Column(name = "waktu", nullable = false, updatable = false)
    private LocalDateTime waktu;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
