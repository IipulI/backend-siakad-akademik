package com.siakad.dto.response;

import com.siakad.dto.response.chart.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MahasiswaChartDto { // <-- Renamed from DashboardAkademikDto
    private PerkuliahanChartDto perkuliahan;
    private ProgresSksChartDto progresSks;
    private SksTempuhChartDto sksTempuh;
    private IndeksPrestasiChartDto indeksPrestasi;
    private DistribusiNilaiChartDto distribusiNilai;
}