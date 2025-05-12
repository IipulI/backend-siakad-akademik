package com.siakad.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BatasSksReqDto {
    private UUID siakJenjangId;
    private BigDecimal ipsMin;
    private BigDecimal ipsMax;
    private Integer batasSks;
}
