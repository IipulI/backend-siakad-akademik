package com.siakad.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class MataKuliahReqDto {
    private UUID siakProgramStudiId;
    private UUID siakTahunKurikulumId;
    private Integer sksTatapMuka;
    private Integer sksPraktikum;
    private Integer semester;
    private Boolean adaPraktikum;
    private String nilaiMin;
    private String kodeMataKuliah;
    private String namaMataKuliah;
    private String jenisMataKuliah;
    private UUID prasyaratMataKuliah1Id;
    private UUID prasyaratMataKuliah2Id;
    private UUID prasyaratMataKuliah3Id;
}
