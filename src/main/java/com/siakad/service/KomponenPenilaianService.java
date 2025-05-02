package com.siakad.service;

import com.siakad.dto.request.KomponenPenilaianReqDto;
import com.siakad.dto.response.KomponenPenilaianResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface KomponenPenilaianService {
    KomponenPenilaianResDto create(KomponenPenilaianReqDto request, HttpServletRequest httpServletRequest);
    Page<KomponenPenilaianResDto> getPaginated(int page, int size);
    KomponenPenilaianResDto getOne(UUID id);
    KomponenPenilaianResDto update(UUID id, KomponenPenilaianReqDto request, HttpServletRequest httpServletRequest);
    void delete(UUID id, HttpServletRequest httpServletRequest);
}
