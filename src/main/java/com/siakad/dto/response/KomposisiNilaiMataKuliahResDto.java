package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class KomposisiNilaiMataKuliahResDto {
    private UUID siakKomposisiNilaiId;
    private String nama;
    private BigDecimal persentase;
}
