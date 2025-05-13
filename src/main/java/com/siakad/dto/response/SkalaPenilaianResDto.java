package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class SkalaPenilaianResDto {
    private UUID id;
    private String tahunAjaran;
    private String programStudi;
    private String angkaMutu;
    private BigDecimal nilaiMutu;
    private BigDecimal nilaiMin;
    private BigDecimal nilaiMax;
}
