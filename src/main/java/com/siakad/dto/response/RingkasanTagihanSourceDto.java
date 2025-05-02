package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RingkasanTagihanSourceDto {
    private BigDecimal totalTagihan;
    private BigDecimal totalTerbayar;
}
