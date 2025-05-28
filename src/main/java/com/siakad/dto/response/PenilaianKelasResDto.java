package com.siakad.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PenilaianKelasResDto {
    private List<KomponenPenilaianResDto> komponenPenilaian;
}
