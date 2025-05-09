package com.siakad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DosenDto {
    private UUID siakDosenId;
    private String nama;
    private List<JadwalDto> jadwal;
}
