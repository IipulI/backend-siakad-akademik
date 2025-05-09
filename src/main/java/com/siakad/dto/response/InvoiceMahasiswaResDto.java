package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class InvoiceMahasiswaResDto {
    private UUID id;
    private UUID siakMahasiswaId;
    private String kodeInvoice;
    private BigDecimal totalTagihan;
    private LocalDate tanggalTenggat;
    private String status;
    private String tahap;
    private BigDecimal totalBayar;
    private LocalDate tanggalBayar;
    private String metodeBayar;
    private List<InvoiceKomponenResDto> komponen;
}
