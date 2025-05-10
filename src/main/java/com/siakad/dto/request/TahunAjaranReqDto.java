package com.siakad.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class TahunAjaranReqDto {
    private String tahun;
    private String nama;
}
