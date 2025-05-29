package com.siakad.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "siak_invoice_pembayaran_komponen",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"siak_invoice_mahasiswa_id", "siak_invoice_komponen_mahasiswa_id"}
        )
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoicePembayaranKomponenMahasiswa {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "siak_invoice_mahasiswa_id", nullable = false)
    @JsonIgnore
    private InvoiceMahasiswa invoiceMahasiswa;

    @ManyToOne
    @JoinColumn(name = "siak_invoice_komponen_mahasiswa_id", nullable = false)
    private InvoiceKomponen invoiceKomponen;

    @ManyToOne
    @JoinColumn(name = "siak_krs_mahasiswa_id", nullable = false)
    private KrsMahasiswa krsMahasiswa;

    @ManyToOne
    @JoinColumn(name = "siak_rincian_krs_mahasiswa_id", nullable = false)
    private KrsRincianMahasiswa rincianKrsMahasiswa;

    private BigDecimal tagihan;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
