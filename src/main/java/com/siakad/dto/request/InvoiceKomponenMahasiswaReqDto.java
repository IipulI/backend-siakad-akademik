package com.siakad.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceKomponenMahasiswaReqDto {
    private String kodeKomponen;
    private String nama;
    private BigDecimal nominal;
}
