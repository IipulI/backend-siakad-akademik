package com.siakad.service;

import com.siakad.dto.request.TahunAjaranReqDto;
import com.siakad.dto.response.TahunAjaranResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TahunAjaranService {
    TahunAjaranResDto create(TahunAjaranReqDto request, HttpServletRequest servletRequest);
    Page<TahunAjaranResDto> search(String keyword, Pageable pageable);
    TahunAjaranResDto getOne(UUID id);
    TahunAjaranResDto update(UUID id, TahunAjaranReqDto request, HttpServletRequest servletRequest);
    void delete(UUID id, HttpServletRequest servletRequest);
}
