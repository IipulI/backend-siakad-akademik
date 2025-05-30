package com.siakad.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "siak_invoice_mahasiswa")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceMahasiswa {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "siak_mahasiswa_id", nullable = false)
    private Mahasiswa siakMahasiswa;

    @ManyToOne
    @JoinColumn(name = "siak_periode_akademik_id", nullable = false)
    private PeriodeAkademik siakPeriodeAkademik;

    private String kodeInvoice;
    private BigDecimal totalTagihan;
    private LocalDate tanggalTenggat;
    private String status;
    private String tahap;
    private BigDecimal totalBayar;
    private LocalDate tanggalBayar;
    private String metodeBayar;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "invoiceMahasiswa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoicePembayaranKomponenMahasiswa> invoicePembayaranKomponenMahasiswaList;

}
