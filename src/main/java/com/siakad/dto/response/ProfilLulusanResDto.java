package com.siakad.dto.response;

import com.siakad.entity.ProfilLulusan;
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

    public static ProfilLulusanResDto fromEntity(ProfilLulusan entity) {
        ProfilLulusanResDto dto = new ProfilLulusanResDto();
        dto.setId(entity.getId());

        dto.setProgramStudi(entity.getSiakProgramStudi().getNamaProgramStudi());
        dto.setTahunKurikulum(entity.getSiakTahunKurikulum().getTahun());

        dto.setKodePl(entity.getKodePl());
        dto.setDeskripsiPl(entity.getDeskripsiPl());

        dto.setProfil(entity.getProfil());   // Asumsi field ada
        dto.setProfesi(entity.getProfesi()); // Asumsi field ada

        return dto;
    }
}
