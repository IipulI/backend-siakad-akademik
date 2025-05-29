package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class CapaianPembelajaranLulusanDto {
    private UUID id;
    private String programStudi;
    private String tahunKurikulum;
    private String kodeCpl;
    private String deskripsiCpl;
    private String kategoriCpl;
}
