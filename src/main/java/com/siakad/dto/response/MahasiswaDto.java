package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MahasiswaDto {
    private String npm;
    private String nama;
    private BigDecimal tugas;
    private BigDecimal uts;
    private BigDecimal kehadiran;
    private BigDecimal uas;
    private BigDecimal nilai;
    private String grade;
}
