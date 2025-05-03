package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RiwayatPembayaranResDto {
    private String namaMahasiswa;
    private String metodePembayaran;
    private LocalDate tanggal;
    private BigDecimal jumlahBayar;
}
