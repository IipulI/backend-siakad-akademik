package com.siakad.service;

import com.siakad.dto.response.KomposisiNilaiMataKuliahMhsResDto;

import java.util.List;
import java.util.UUID;

public interface KomposisiNilaiMataKuliahMhsService {
    List<KomposisiNilaiMataKuliahMhsResDto> getKomposisiMataKuliah(UUID mahasiswaId, UUID periodeAkademikId);
}
