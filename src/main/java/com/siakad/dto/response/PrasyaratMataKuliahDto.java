package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class PrasyaratMataKuliahDto {
    private UUID id;
    private String kodeMataKuliah;
    private String namaMataKuliah;
}
