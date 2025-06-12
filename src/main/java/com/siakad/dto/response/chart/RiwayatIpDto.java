package com.siakad.dto.response.chart;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RiwayatIpDto {
    private int semester;
    private BigDecimal ips;
    private BigDecimal ipk;
}