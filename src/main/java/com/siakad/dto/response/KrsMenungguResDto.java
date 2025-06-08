package com.siakad.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class KrsMenungguResDto {
    private List<KrsResDto> krs;
    private Integer totalSks;
}
