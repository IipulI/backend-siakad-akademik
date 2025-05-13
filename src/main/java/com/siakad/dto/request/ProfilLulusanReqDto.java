package com.siakad.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ProfilLulusanReqDto {
    private UUID siakProgramStudiId;
    private UUID siakTahunKurikulumId;
    private String profil;
    private String profesi;
    private String kodePl;
    private String deskripsiPl;
}
