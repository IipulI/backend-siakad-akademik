package com.siakad.service;

import com.siakad.dto.request.CapaianPembelajaranLulusanReqDto;
import com.siakad.dto.response.CapaianPembelajaranLulusanResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CapaianPembelajaranLulusanService {
    CapaianPembelajaranLulusanResDto save(CapaianPembelajaranLulusanReqDto dto, HttpServletRequest servletRequest);
    CapaianPembelajaranLulusanResDto getOne(UUID id);
    Page<CapaianPembelajaranLulusanResDto> getPaginate(String tahunKurukulum, Pageable pageable);
    CapaianPembelajaranLulusanResDto update(UUID id, CapaianPembelajaranLulusanReqDto dto, HttpServletRequest servletRequest);
    void delete(UUID id, HttpServletRequest servletRequest);
}
