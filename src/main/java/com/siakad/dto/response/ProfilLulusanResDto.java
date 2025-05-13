package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class ProfilLulusanResDto {
    private UUID id;
    private String programStudi;
    private String tahunKurikulum;
    private String profil;
    private String profesi;
    private String kodePl;
    private String deskripsiPl;
}
