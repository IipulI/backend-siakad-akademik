package com.siakad.service;

import com.siakad.dto.response.JadwalMingguanResDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface JadwalKuliahService {
    public Map<String, List<JadwalMingguanResDto>> getJadwalMingguanMahasiswa(UUID mahasiswaId, UUID periodeAkademikId);
}
