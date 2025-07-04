package com.siakad.service;

import com.siakad.dto.response.JadwalMingguanResDto;
import com.siakad.dto.response.JadwalUjianResDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface JadwalKuliahService {
    public Map<String, List<JadwalMingguanResDto>> getJadwalMingguanMahasiswa(UUID mahasiswaId, String namaPeriode);
    public List<JadwalMingguanResDto> getJadwalHarianMahasiswa(UUID mahasiswaId, String namaPeriode, String hari);
//    public List<JadwalUjianResDto> getJadwalUjianMahasiswa(UUID mahasiswaId);

    public List<JadwalMingguanResDto> getJadwalHarianDosen(UUID dosenId, UUID periodeAkademikId, String hari);
    public Map<String, List<JadwalMingguanResDto>> getJadwalMingguanDosen(UUID dosenId, UUID periodeAkademikId);
}
