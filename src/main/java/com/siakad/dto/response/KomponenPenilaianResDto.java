package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class KomponenPenilaianResDto {
    private UUID id;
    private String nama;
    private BigDecimal persentase;
}
