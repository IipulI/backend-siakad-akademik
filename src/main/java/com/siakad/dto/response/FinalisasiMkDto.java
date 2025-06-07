package com.siakad.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class FinalisasiMkDto {
    private String periodeAkademik;
    private String kurikulum;
    private String kodeMataKuliah;
    private String namaMatakuliah;
    private Integer sks;
    private Boolean opsiMataKuliah;
    private String grade;
    private String status;
    private Boolean dipakai;
    private Boolean transkip;
}
