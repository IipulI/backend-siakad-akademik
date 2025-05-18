package com.siakad.service;

import com.siakad.dto.request.JadwalDosenReqDto;
import com.siakad.dto.response.JadwalDosenResDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface JadwalDosenService {
    void save(UUID id, JadwalDosenReqDto request, HttpServletRequest servletRequest);
}
