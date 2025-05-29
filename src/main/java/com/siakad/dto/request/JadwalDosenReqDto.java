package com.siakad.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class JadwalDosenReqDto {
    private List<JadwalDosenDto> jadwal;
}
