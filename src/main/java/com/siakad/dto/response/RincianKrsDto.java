package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RincianKrsDto {
    private String namaMataKuliah;
    private String kodeMataKuliah;
    private Integer sks;
    private Integer semester;
    private String hurufMutu;
    private BigDecimal angkaMutu;
    private BigDecimal jumlahAngkaMutu;
}
