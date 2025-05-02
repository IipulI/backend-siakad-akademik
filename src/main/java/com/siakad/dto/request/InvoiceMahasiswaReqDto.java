package com.siakad.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class InvoiceMahasiswaReqDto {
    private String siakMahasiswaId;
    private String kodeInvoice;
    private Date tanggalTenggat;
    private String status;
    private String tahap;
    private Date tanggalBayar;
    private String metodeBayar;
    private List<InvoiceKomponenReqDto> komponen;
}
