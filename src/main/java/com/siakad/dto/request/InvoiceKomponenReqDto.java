package com.siakad.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class InvoiceKomponenReqDto {
    private UUID komponenId;
    private BigDecimal tagihan;
}
