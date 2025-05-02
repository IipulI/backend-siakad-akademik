package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class KomponenPenilaianResDto {
    private UUID id;
    private String namaKomponen;
    private Double nilaiKomponen;
}
