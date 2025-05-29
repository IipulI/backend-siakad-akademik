package com.siakad.dto.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CapaianPembelajaranLulusanReqDto {
    private UUID siakProgramStudiId;
    private UUID siakTahunKurikulumId;
    private String kodeCpl;
    private String deskripsiCpl;
    private String kategoriCpl;
    private List<UUID> profilLulusanIds;
}
