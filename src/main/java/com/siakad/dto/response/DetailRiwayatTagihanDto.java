package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class DetailRiwayatTagihanDto {
    private String kodeInvoice;
    private String periodeAkademik;
    private String metodeBayar;
    private LocalDate tanggalBayar;
    private BigDecimal totalBayar;
    private String npm;
    private String nama;
    private ProgramStudiResDto programStudiResDto;
    List<TagihanKomponenDto> tagihanKomponenDtos;
}
