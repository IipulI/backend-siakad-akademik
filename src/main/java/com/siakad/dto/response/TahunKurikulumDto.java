package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class TahunKurikulumDto {
    private UUID id;
    private String tahun;

    public TahunKurikulumDto(UUID id, String tahun) {
        this.id = id;
        this.tahun = tahun;
    }

    public TahunKurikulumDto() {}
}
