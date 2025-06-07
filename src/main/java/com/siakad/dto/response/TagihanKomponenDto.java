package com.siakad.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TagihanKomponenDto {
    private String kodeKomponen;
    private String namaKomponen;
    private BigDecimal tagihan;
    private LocalDate tanggalTenggat;
}
