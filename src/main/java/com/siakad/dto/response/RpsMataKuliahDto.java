package com.siakad.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RpsMataKuliahDto {
    private UUID id;
    private PeriodeAkademikDto periodeAkademik;
    private List<RpsDosenResDto> dosenPenyusun;
    private List<KelasKuliahDto> kelas;
}
