package com.siakad.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CapaianMataKuliahResDto {
    private UUID id;
    private String mataKuliah;
    private String kodeCpmk;
    private String deskripsiCpmk;
    private List<CapaianPembelajaranLulusanResDto> capaianPembelajaranMapped;
}
