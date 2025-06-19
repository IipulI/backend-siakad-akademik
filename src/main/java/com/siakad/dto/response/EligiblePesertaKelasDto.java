package com.siakad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EligiblePesertaKelasDto {
    private UUID mahasiswaId;
    private String npm;
    private String nama;
    private Integer semester;
    private ProgramStudiResDto programStudi;
    private Integer batasSks;
    private Integer sksDiambil;
}
