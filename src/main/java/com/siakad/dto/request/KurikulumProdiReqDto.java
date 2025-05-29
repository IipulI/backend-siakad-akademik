package com.siakad.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class KurikulumProdiReqDto {
    private UUID siakProgramStudiId;
    private UUID siakTahunKurikulumId;
    private Integer semester;
    private Boolean opsiMataKuliah;
}
