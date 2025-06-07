package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatusSemesterDto {
    private String kodePeriode;
    private Integer semester;
    private String status;
    private Integer sks;
    private Integer sksTempuh;
    private Integer sksLulus;
    private Integer sksTotal;
    private BigDecimal ips;
    private BigDecimal ipk;
    private String dosen;
}
