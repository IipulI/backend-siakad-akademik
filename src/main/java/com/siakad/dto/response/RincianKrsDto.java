package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RincianKrsDto {
    private String namaMataKuliah;
    private String kodeMataKuliah;
    private Integer sks;
    private String hurufMutu;
    private BigDecimal nilaiMutu;
    private BigDecimal bobot;
}
