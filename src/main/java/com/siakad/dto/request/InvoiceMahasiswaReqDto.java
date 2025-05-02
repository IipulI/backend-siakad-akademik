package com.siakad.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class InvoiceMahasiswaReqDto {
    private String siakMahasiswaId;
    private String kodeInvoice;
    private LocalDate tanggalTenggat;
    private String status;
    private String tahap;
    private LocalDate tanggalBayar;
    private String metodeBayar;
    private List<InvoiceKomponenReqDto> komponen;
}
