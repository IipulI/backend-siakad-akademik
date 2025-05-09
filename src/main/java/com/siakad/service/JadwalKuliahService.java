package com.siakad.service;

import com.siakad.dto.request.JadwalKuliahReqDto;
import com.siakad.dto.response.DetailKelasDosenPengajarResDto;
import com.siakad.dto.response.JadwalKuliahResDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface JadwalKuliahService {
    JadwalKuliahResDto create(JadwalKuliahReqDto request, HttpServletRequest httpServletRequest);
    List<JadwalKuliahResDto> getAll();
    DetailKelasDosenPengajarResDto getDetailKelasDosenPengajar(UUID kelasId);
    JadwalKuliahResDto update(JadwalKuliahReqDto request, UUID id, HttpServletRequest httpServletRequest);
    void delete(UUID id, HttpServletRequest httpServletRequest);
}
