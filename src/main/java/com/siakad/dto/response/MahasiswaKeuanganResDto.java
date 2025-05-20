package com.siakad.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MahasiswaKeuanganResDto {
    private UUID id;
    private String npm;
    private String nama;
    private String namaFakultas;
    private String namaProgramStudi;
    private Integer semester;
    private String angkatan;
}
