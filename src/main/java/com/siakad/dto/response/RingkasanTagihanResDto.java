package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RingkasanTagihanResDto {
    private BigDecimal totalTagihan;
    private BigDecimal totalTerbayar;
    private BigDecimal totalBelumBayar;
}
