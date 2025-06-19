package com.siakad.dto.response.chart;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DistribusiNilaiChartDto {
    private List<DetailNilaiDto> detail;
}