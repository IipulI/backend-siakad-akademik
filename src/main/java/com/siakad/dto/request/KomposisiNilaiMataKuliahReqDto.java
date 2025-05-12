package com.siakad.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KomposisiNilaiMataKuliahReqDto {
    private UUID siakMataKuliahId;
    private UUID siakKomposisiNilaiId;
}

