package com.siakad.service;

import com.siakad.dto.request.TahunKurikulumReqDto;
import com.siakad.dto.response.TahunKurikulumResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TahunKurikulumService {
    TahunKurikulumResDto create(TahunKurikulumReqDto dto, HttpServletRequest servletRequest);
    Page<TahunKurikulumResDto> search(String keyword, Pageable pageable);
    TahunKurikulumResDto getOne(UUID id);
    TahunKurikulumResDto update(UUID id, TahunKurikulumReqDto dto, HttpServletRequest servletRequest);
    void delete(UUID id, HttpServletRequest servletRequest);
}
