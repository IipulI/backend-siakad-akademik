package com.siakad.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PengumumanResDto {
    private UUID id;
    private String user;
    private String judul;
    private LocalDate tanggal;
    private String isi;
    private Boolean isActive;
    private Boolean isPriority;
}
