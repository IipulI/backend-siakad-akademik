package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TagihanMahasiswaDto {
    private BigDecimal totalTagihan;
    private BigDecimal totalLunas;
    private BigDecimal tagihan;
    private LocalDate tenggatTagihan;
}
