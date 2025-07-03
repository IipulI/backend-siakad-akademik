package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InfoTagihanResDto {
    private BigDecimal totalTagihan;
    private BigDecimal totalLunas;
    private BigDecimal sisaTagihan;
    private LocalDate tanggalTenggat;
}
