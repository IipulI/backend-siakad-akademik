package com.siakad.dto.request;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class InvoiceMahasiswaReqDto {
    private List<UUID> siakMahasiswaIds;
    private LocalDate tanggalTenggat;
    private String tahap;
    private List<InvoiceKomponenReqDto> komponen;
}
