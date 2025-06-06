package com.siakad.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TagihanKomponenDto {
    private String kodeKomponen;
    private String namaKomponen;
    private BigDecimal tagihan;
}
