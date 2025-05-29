package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class PengumumanResDto {
    private UUID id;
    private String user;
    private String judul;
    private String isi;
    private Boolean isActive;
    private Boolean isPriority;
}
