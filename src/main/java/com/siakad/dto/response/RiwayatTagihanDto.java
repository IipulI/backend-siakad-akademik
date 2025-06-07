package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class RiwayatTagihanDto {
    private UUID id;
    private String kodeInvoice;
    private String periodeAkademik;
    private BigDecimal totalPembayaran;
    private String metodeBayar;
    private LocalDate tanggalBayar;
}
