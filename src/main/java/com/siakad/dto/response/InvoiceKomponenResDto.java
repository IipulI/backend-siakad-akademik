package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceKomponenResDto {
    private String kodeKomponen;
    private String nama;
    private BigDecimal nominal;
}
