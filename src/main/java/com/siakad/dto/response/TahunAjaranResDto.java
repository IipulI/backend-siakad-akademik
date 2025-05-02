package com.siakad.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class TahunAjaranResDto {
    private UUID id;
    private String nama;
    private Date tanggalMulai;
    private Date tanggalSelesai;
}
