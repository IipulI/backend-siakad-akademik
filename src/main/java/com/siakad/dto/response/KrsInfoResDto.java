package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class KrsInfoResDto {
    private String statusKrs;
    private Integer semester;
    private Integer batasSks;
    private String periodeAkademik;
    private String pembimbingAkademik;
}
