package com.siakad.service;

import com.siakad.dto.request.SkalaPenilaianReqDto;
import com.siakad.dto.response.SkalaPenilaianResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SkalaPenilaianService {
    SkalaPenilaianResDto save(SkalaPenilaianReqDto reqDto, HttpServletRequest servletRequest);
    Page<SkalaPenilaianResDto> getPaginate(String tahunAjaran, String programStudi, Pageable pageable);
    SkalaPenilaianResDto getOne(UUID id);
    SkalaPenilaianResDto update(UUID id, SkalaPenilaianReqDto reqDto, HttpServletRequest servletRequest);
    void delete(UUID id, HttpServletRequest servletRequest);
}
