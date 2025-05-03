package com.siakad.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class MataKuliahReqDto {
    private UUID siakProgramStudiId;
    private UUID siakTahunKurikulumId;
    private Integer semester;
    private String kodeMataKuliah;
    private String namaMataKuliah;
    private String jenisMataKuliah;
    private Integer sks;
    private UUID prasyaratMataKuliah1Id;
    private UUID prasyaratMataKuliah2Id;
    private UUID prasyaratMataKuliah3Id;
}
