package com.siakad.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class RiwayatKrsDto {
    private List<KrsDto> krs;
    private Integer totalSks;
    private Integer batasSks;
}
