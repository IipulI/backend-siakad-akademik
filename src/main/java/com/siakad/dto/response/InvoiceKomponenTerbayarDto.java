package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class InvoiceKomponenTerbayarDto {
    private UUID id;
    private String kodeKomponen;
    private String nama;
    private BigDecimal tagihan;
}
