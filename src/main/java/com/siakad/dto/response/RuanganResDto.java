package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class RuanganResDto {
    private UUID id;
    private String namaRuangan;
    private String kapasitas;
    private String lantai;
}
