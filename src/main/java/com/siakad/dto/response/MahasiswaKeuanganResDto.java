package com.siakad.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MahasiswaKeuanganResDto {
    private String npm;
    private String nama;
    private String namaFakultas;
    private String namaProgramStudi;
    private Integer semester;
}
