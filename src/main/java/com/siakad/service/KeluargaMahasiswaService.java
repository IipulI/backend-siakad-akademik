package com.siakad.service;

import com.siakad.dto.request.KeluargaMahasiswaReqDto;
import com.siakad.dto.response.KeluargaMahasiswaResDto;
import com.siakad.entity.KeluargaMahasiswa;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface KeluargaMahasiswaService {
    KeluargaMahasiswaResDto create(UUID idMahasiwa, KeluargaMahasiswaReqDto request, HttpServletRequest servletRequest);
    Page<KeluargaMahasiswaResDto> getPaginated(UUID idMahasiswa, int page, int size);
    KeluargaMahasiswaResDto getOne(UUID idMahasiswa, UUID id);
    KeluargaMahasiswaResDto update(UUID idMahasiswa, UUID id, KeluargaMahasiswaReqDto request, HttpServletRequest servletRequest);
    void delete(UUID idMahasiswa, UUID id, HttpServletRequest servletRequest);
}
