package com.siakad.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class SkalaPenilaianReqDto {
    private UUID siakTahunAjaranId;
    private UUID siakProgramStudiId;
    private String hurufMutu;
    private BigDecimal angkaMutu;
    private BigDecimal nilaiMin;
    private BigDecimal nilaiMax;
}
