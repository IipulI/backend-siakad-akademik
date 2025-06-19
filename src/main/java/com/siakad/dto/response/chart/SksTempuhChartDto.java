package com.siakad.dto.response.chart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SksTempuhChartDto {
    private int lulus;
    private int belumLulus;
    private int total;
}