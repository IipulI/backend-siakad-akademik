package com.siakad.dto.response;

import lombok.Data;

@Data
public class MahasiswaBaruDto {
    private ProgramStudiResDto programStudiResDto;
    private PendaftaranDto pendaftaranDto;
    private JenisKelaminDto jenisKelaminDto;
    private Integer total;
}
