package com.siakad.service;

import com.siakad.dto.request.PengumumanReqDto;
import com.siakad.dto.response.PengumumanResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface PengumumanService {
    PengumumanResDto save(PengumumanReqDto dto, MultipartFile file, HttpServletRequest servletRequest) throws IOException;

    Page<PengumumanResDto> search(String keyword, String status, Pageable pageable);
    PengumumanResDto getOne(UUID id);
    PengumumanResDto update(UUID id, PengumumanReqDto dto, MultipartFile file, HttpServletRequest servletRequest) throws IOException;
    void delete(UUID id, HttpServletRequest servletRequest);
    byte[] getBanner(UUID id);
}
