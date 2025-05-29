package com.siakad.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class KomposisiPenilaianReqDto {
    private UUID siakTahunKurikulumId;
    private String nama;
    private BigDecimal persentase;
}
