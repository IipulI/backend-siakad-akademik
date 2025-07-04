package com.siakad.service;

import com.siakad.dto.request.GetJadwalReqDto;
import com.siakad.dto.request.GetJadwalResDto;
import com.siakad.dto.request.JadwalDosenReqDto;
import com.siakad.dto.request.JadwalUjianReqDto;
import com.siakad.dto.response.JadwalDto;
import com.siakad.dto.response.JadwalUjianResDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface JadwalDosenService {
    void save(UUID id, JadwalDosenReqDto request, HttpServletRequest servletRequest);
    List<JadwalDto> getAll(UUID id);
    List<JadwalDto> getByDosenId(UUID id, UUID dosenId);
    List<GetJadwalResDto> getJadwalHarian(GetJadwalReqDto reqDto);

    List<JadwalUjianResDto> getAllJadwalUjian(UUID id);
    void saveJadwalUjian(UUID id, JadwalUjianReqDto request, HttpServletRequest servletRequest);
    boolean deleteJadwalUjian(UUID kelasId, UUID id, HttpServletRequest servletRequest);
}
