package com.siakad.dto.response.chart;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SksPerSemesterDto {
    private int semester;
    private int sks;
}
