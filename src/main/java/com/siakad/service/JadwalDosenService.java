package com.siakad.service;

import com.siakad.dto.request.JadwalDosenReqDto;
import com.siakad.dto.response.JadwalDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface JadwalDosenService {
    void save(UUID id, JadwalDosenReqDto request, HttpServletRequest servletRequest);
    List<JadwalDto> getAll(UUID id);
}
