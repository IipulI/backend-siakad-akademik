package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TranskipDto {
    private List<RincianKrsDto> rincianKrsDto;
    private BigDecimal ipk;
    private Integer totalSks;
}
