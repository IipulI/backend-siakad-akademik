package com.siakad.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.siakad.entity.CapaianPembelajaranLulusan;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public static CapaianPembelajaranLulusanResDto fromEntity(CapaianPembelajaranLulusan entity) {
        CapaianPembelajaranLulusanResDto dto = new CapaianPembelajaranLulusanResDto();
        dto.setId(entity.getId());
        dto.setProgramStudi(entity.getSiakProgramStudi().getNamaProgramStudi()); // asumsi ada field ini
        dto.setTahunKurikulum(entity.getSiakTahunKurikulum().getTahun());       // asumsi ada field ini
        dto.setKodeCpl(entity.getKodeCpl());
        dto.setDeskripsiCpl(entity.getDeskripsiCpl());
        dto.setKategoriCpl(entity.getKategoriCpl());

        if (entity.getProfilLulusanList() != null) {
            dto.setProfilLulusanMapped(
                    entity.getProfilLulusanList().stream()
                            .map(ProfilLulusanResDto::fromEntity)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }
}
