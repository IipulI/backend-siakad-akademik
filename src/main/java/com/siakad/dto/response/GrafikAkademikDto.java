package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GrafikAkademikDto {
    private BigDecimal ipk;
    private List<BigDecimal> ips;
    private Integer mataKuliahKumulatif;
    private Integer sksKumulatif;
}
