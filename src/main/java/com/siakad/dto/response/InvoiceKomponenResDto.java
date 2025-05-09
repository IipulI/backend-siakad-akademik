package com.siakad.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class InvoiceKomponenResDto {
    private UUID id;
    private String kodeKomponen;
    private String nama;
    private BigDecimal nominal;
}
