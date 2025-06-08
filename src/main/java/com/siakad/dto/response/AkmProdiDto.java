package com.siakad.dto.response;

import lombok.Data;

@Data
public class AkmProdiDto {
    private ProgramStudiResDto programStudiResDto;
    private Integer aktif;
    private Integer cuti;
    private Integer nonAktif;
    private Integer total;
}
