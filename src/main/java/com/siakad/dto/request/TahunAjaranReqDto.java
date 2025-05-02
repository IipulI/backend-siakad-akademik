package com.siakad.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class TahunAjaranReqDto {
    private String nama;
    private Date tanggalMulai;
    private Date tanggalSelesai;
}
