package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class MataKuliahRpsResDto {
    private UUID id;
    private String kodeMataKuliah;
    private String namaMataKuliah;
    private Integer sks;
    private Integer semester;
}
