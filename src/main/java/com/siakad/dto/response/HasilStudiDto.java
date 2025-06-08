package com.siakad.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class    HasilStudiDto {
    private List<RincianKrsDto> rincianKrsDto;
    private Integer ips;
}
