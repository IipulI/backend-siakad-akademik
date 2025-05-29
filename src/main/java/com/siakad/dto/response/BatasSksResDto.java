package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BatasSksResDto {
    private UUID id;
    private String jenjang;
    private BigDecimal ipsMin;
    private BigDecimal ipsMax;
    private Integer batasSks;
}
