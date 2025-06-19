package com.siakad.dto.response.chart;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class IndeksPrestasiChartDto {
    private BigDecimal ipMinimum;
    private List<RiwayatIpDto> riwayat;
}