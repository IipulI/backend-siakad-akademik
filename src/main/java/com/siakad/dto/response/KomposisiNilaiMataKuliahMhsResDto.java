package com.siakad.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class KomposisiNilaiMataKuliahMhsResDto {
    private String tahunKurikulum;
    private String kodeMataKuliah;
    private String namaMataKuliah;
    private String namaKelas;
    private List<KomponenKomposisiNilaiMataKuliahMhsDto> komposisiNilaiMataKuliahResDto;
    private BigDecimal nilaiAkhir;
}
