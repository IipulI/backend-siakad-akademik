package com.siakad.dto.response;

import com.siakad.entity.PeriodeAkademik;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class DetailTagihanMahasiswaResDto {
    private UUID id;
    private String nama;
    private String kodeInvoice;
    private LocalDate tanggalBayar;
    private String metodeBayar;
    private PeriodeAkademik periodeAkademik;
    private InvoiceKomponenTerbayarDto komponenTerbayar;
}
