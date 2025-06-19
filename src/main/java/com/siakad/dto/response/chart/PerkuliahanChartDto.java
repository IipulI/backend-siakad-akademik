package com.siakad.dto.response.chart;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PerkuliahanChartDto {
    private List<SksPerSemesterDto> sksDiambilPerSemester;
    private ZonaStudiDto zonaPeringatan;
    private ZonaStudiDto zonaDropOut;
}