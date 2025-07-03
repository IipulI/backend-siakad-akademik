package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TagihanMhsDto {
    private String kodeInvoice;
    private String metodeBayar;
    private String namaPeriode;
    private LocalDate tanggalTenggat;
    private LocalDate tanggalBayar;
    private String kodeKomponen;
    private String namaTagihan;
    private BigDecimal nominalTagihan;
    private String lunas;
}
