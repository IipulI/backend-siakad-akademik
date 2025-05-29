package com.siakad.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CapaianPembelajaranLulusanResDto {
    private UUID id;
    private String programStudi;
    private String tahunKurikulum;
    private String kodeCpl;
    private String deskripsiCpl;
    private String kategoriCpl;
    private List<ProfilLulusanResDto> profilLulusanMapped;
}
