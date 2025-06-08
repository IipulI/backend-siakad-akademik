package com.siakad.dto.response;

import lombok.Data;

@Data
public class CardDto {
    private Integer mahasiswaBaruPeriodeIni;
    private Integer mahasiswaAktif;
    private Integer mahasiswaTerdaftar;
    private String periodeSaatIni;
}
