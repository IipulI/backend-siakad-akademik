package com.siakad.service;

import com.siakad.dto.request.PengumumanReqDto;
import com.siakad.dto.response.PengumumanResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface PengumumanService {
    PengumumanResDto save(PengumumanReqDto dto, MultipartFile file, HttpServletRequest servletRequest) throws IOException;
    PengumumanResDto getOne(UUID id);
    PengumumanResDto update(UUID id, PengumumanReqDto dto, MultipartFile file, HttpServletRequest servletRequest) throws IOException;
    void delete(UUID id, HttpServletRequest servletRequest);
    byte[] getBanner(UUID id);
}
