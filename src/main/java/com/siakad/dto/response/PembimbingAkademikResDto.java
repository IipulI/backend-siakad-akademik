package com.siakad.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PembimbingAkademikResDto {
    private String mahasiswa;
    private String angkatan;
    private String statusMahasiswa;
    private Integer semester;
    private Integer batasSks;
    private Integer totalSks;
    private BigDecimal ipk;
    private BigDecimal ips;
    private Boolean statusDiajukan = false;
    private Boolean statusDisetujui = false;
    private String pembimbingAkademik;

    @JsonIgnore
    private String programStudi;
}
