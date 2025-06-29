package com.siakad.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.siakad.entity.CapaianMataKuliah;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CapaianMataKuliahResDto {
    private UUID id;
    private String mataKuliah;
    private String kodeCpmk;
    private String deskripsiCpmk;
    private List<CapaianPembelajaranLulusanResDto> capaianPembelajaranMapped;

    public static CapaianMataKuliahResDto fromEntity(CapaianMataKuliah entity) {
        return CapaianMataKuliahResDto.builder()
                .id(entity.getId())
                .mataKuliah(entity.getSiakMataKuliah().getNamaMataKuliah())
                .kodeCpmk(entity.getKodeCpmk())
                .deskripsiCpmk(entity.getDeskripsiCpmk())
                .build();
    }

}
