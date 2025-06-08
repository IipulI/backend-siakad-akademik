package com.siakad.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PenilaianKelasKuliahReqDto {
    private UUID mahasiswaId;
    private BigDecimal tugas;
    private BigDecimal uas;
    private BigDecimal uts;
    private BigDecimal kehadiran;
}
