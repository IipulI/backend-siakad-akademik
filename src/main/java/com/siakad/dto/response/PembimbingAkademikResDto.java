package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PembimbingAkademikResDto {
    private String mahasiswa;
    private String angkatan;
    private String statusMahasiswa;
    private Integer semester;
    private BigDecimal batasSks;
    private BigDecimal totalSks;
    private BigDecimal ipk;
    private BigDecimal ips;
    private Boolean statusDiajukan = false;
    private Boolean statusDisetujiu = false;
    private String pembimbingAkademik;
    private String unitKerja;
}
