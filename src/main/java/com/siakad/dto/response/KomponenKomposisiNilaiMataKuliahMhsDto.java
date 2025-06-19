package com.siakad.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class KomponenKomposisiNilaiMataKuliahMhsDto {
    private String namaKomposisi;
    private BigDecimal persentase;
    private BigDecimal nilai;
}
