package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class MataKuliahResDto {
    private UUID id;
    private String programStudi;
    private String tahunKurikulum;
    private String semester;
    private String kodeMataKuliah;
    private String namaMataKuliah;
    private Integer sks;
    private String jenisMataKuliah;
    private String prasyaratMataKuliah1;
    private String prasyaratMataKuliah2;
    private String prasyaratMataKuliah3;
}
