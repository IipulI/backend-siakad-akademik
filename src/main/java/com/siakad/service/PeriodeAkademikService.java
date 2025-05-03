package com.siakad.service;

import com.siakad.dto.request.PeriodeAkademikReqDto;
import com.siakad.dto.response.PeriodeAkademikResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PeriodeAkademikService {
    PeriodeAkademikResDto create(PeriodeAkademikReqDto request, HttpServletRequest servletRequest);
    Page<PeriodeAkademikResDto> search(String keyword, Pageable pageable);
    PeriodeAkademikResDto getOne(UUID id);
    PeriodeAkademikResDto update(UUID id, PeriodeAkademikReqDto request, HttpServletRequest servletRequest);
    void delete(UUID id, HttpServletRequest servletRequest);
}
