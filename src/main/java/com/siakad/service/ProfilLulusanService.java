package com.siakad.service;

import com.siakad.dto.request.ProfilLulusanReqDto;
import com.siakad.dto.response.ProfilLulusanResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProfilLulusanService {
    ProfilLulusanResDto save(ProfilLulusanReqDto reqDto, HttpServletRequest servletRequest);
    Page<ProfilLulusanResDto> getPaginate(String tahunKurkulum, Pageable pageable);
    ProfilLulusanResDto getOne(UUID id);
    ProfilLulusanResDto update (UUID id, ProfilLulusanReqDto reqDto, HttpServletRequest servletRequest);
    void delete(UUID id, HttpServletRequest servletRequest);
}
