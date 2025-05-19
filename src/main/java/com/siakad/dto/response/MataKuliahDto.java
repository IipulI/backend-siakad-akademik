package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class MataKuliahDto {
    private UUID id;
    private String tahunKurikulum;
    private String namaMataKuliah;
    private String kodeMataKuliah;
}