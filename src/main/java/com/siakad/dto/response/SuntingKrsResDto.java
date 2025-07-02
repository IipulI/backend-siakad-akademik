package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SuntingKrsResDto {
    private Integer kurikulum;
    private String kodeMataKuliah;
    private String namaMataKuliah;
    private String namaKelas;
    private Integer sks;
    private BigDecimal nilaiNumerik;
    private String nilaiHuruf;
    private BigDecimal nilaiMutu;
    private Boolean valid;
    private Boolean lulus;
}
