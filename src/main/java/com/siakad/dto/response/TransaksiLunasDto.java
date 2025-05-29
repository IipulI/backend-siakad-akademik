package com.siakad.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class TransaksiLunasDto {
    private UUID id;
    private String namaMahasiswa;
    private String metodePembayaran;
    private LocalDate tanggal;
    private BigDecimal jumlahBayar;
}
